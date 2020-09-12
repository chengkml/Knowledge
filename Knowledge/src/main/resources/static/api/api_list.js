var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            formHeight:400,
            formLabelWidth:'90px',
            inputFormRules:{
                url: [
                    {required: true, message: '请输入请求url', trigger: 'blur'}
                ],
                name: [
                    {required: true, message: '请输入方法名', trigger: 'blur'}
                ],
                method: [
                    {required: true, message: '请选择请求类型', trigger: 'change'}
                ],
                note: [
                    {required: true, message: '请输入方法提示', trigger: 'blur'}
                ]
            },
            inputForm:{
                url:'',
                name:'',
                method:'',
                note:''
            },
            code:'',
            codemirror:null,
            treeHeight:400,
            codemirrorHeight:430,
            filterText:'',
            currApi:[],
            apiTree:[],
            defaultProps: {
                children: 'children',
                label: 'label'
            }
        }
    },
    methods: {
        generateInputApi(){
            this.$refs.inputForm.validate((valid)=>{
                if(valid){
                    axios.get(_contextPath + '/api/input',{
                        params:{
                            name:this.inputForm.name,
                            url:this.inputForm.url,
                            method:this.inputForm.method,
                            note:this.inputForm.note
                        }
                    }).then( (resp)=> {
                        if(resp&&resp.data&&resp.data.success){
                            this.changeCode(resp.data.data);
                        }else if(resp&&resp.data&&resp.data.msg){
                            this.$message({
                                type:'error',
                                showClose:true,
                                message:'通过输入信息生成js方法失败，失败原因：'+resp.data.msg
                            });
                            console.error(resp);
                        }else{
                            this.$message({
                                type:'error',
                                showClose:true,
                                message:'通过输入信息生成js方法失败!'
                            });
                            console.error(resp);
                        }
                    }).catch((err)=>{
                        this.$message({
                            type:'error',
                            showClose:true,
                            message:'通过输入信息生成js方法失败!'
                        });
                        console.error(err);
                    });
                }
            });
        },

        generateApiSelected:function(params,func,errFunc){
            var _self = this;
            axios.post(_contextPath + '/api/context/selected',params).then(function (resp) {
                if(resp&&resp.data&&resp.data.success){
                    if(func&&func instanceof Function){
                        func(resp.data.data);
                    }
                }else if(resp&&resp.data&&resp.data.msg){
                    if(errFunc&&errFunc instanceof Function){
                        errFunc();
                    }
                    _self.$({
                        type:'error',
                        showClose:true,
                        message:'批量指定Context中服务的方法生成js方法失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    if(errFunc&&errFunc instanceof Function){
                        errFunc();
                    }
                    _self.$message({
                        type:'error',
                        showClose:true,
                        message:'批量指定Context中服务的方法生成js方法失败!'
                    });
                    console.error(resp);
                }
            }).catch(function(err){
                if(errFunc&&errFunc instanceof Function){
                    errFunc();
                }
                _self.$message({
                    type:'error',
                    showClose:true,
                    message:'批量指定Context中服务的方法生成js方法失败!'
                });
                console.error(err);
            });
        },

        handleCheck:function(){
            var _self = this;
            var selectedMethods = this.$refs.tree.getCheckedKeys();
            if(selectedMethods.length===0){
                return;
            }
            _self.generateApiSelected(selectedMethods,function (data) {
                _self.changeCode(data);
            });
        },

        changeCode:function(value){
            this.code = value;
            this.codemirror.setValue(this.code);
        },

        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },

        getApiTree:function(func,errFunc){
            var _self = this;
            axios.get(_contextPath + '/api/context/apiTree').then(function (resp) {
                if(resp&&resp.data&&resp.data.success){
                    if(func&&func instanceof Function){
                        func();
                    }
                    _self.apiTree = resp.data.data;
                }else if(resp&&resp.data&&resp.data.msg){
                    if(errFunc&&errFunc instanceof Function){
                        errFunc();
                    }
                    _self.$({
                        type:'error',
                        showClose:true,
                        message:'获取Context中服务树失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    if(errFunc&&errFunc instanceof Function){
                        errFunc();
                    }
                    _self.$message({
                        type:'error',
                        showClose:true,
                        message:'获取Context中服务树失败!'
                    });
                    console.error(resp);
                }
            }).catch(function(err){
                if(errFunc&&errFunc instanceof Function){
                    errFunc();
                }
                _self.$message({
                    type:'error',
                    showClose:true,
                    message:'获取Context中服务树失败!'
                });
                console.error(err);
            });
        }
    },
    watch:{
        filterText(val) {
            this.$refs.tree.filter(val);
        }
    },
    computed: {},
    mounted:function(){
        this.codemirror = CodeMirror.fromTextArea(this.$refs.codemirror, {
            mode:"text/javascript",
            theme: "eclipse",
            lineNumbers: true
        });
    },
    created: function () {
        this.getApiTree();
        addLayoutListen( (width,height)=> {
            this.treeHeight = height - 130;
            this.codemirrorHeight = height;
            this.formHeight = height - 70;
        });
    }
})
