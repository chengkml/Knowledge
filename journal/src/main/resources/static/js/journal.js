var vm = new Vue({
    el:'#main',
    data:{
        currRow:null,
        currDetail:'',
        contentDialog:false,
        nodeArr:[],
        formLabelWidth:'80px',
        journalForm:{
            title:'',
            content:''
        },
        journalDialog:false,
        journals:[],
        filter:{
            keyWord:'',
            dateRange:[],
            category:'',
            startTime:0,
            endTime:0
        },
        page:{
            pageSize:10,
            pageNum:1,
            total:0
        },
        yearOptions:[],
        currentYear:'2019',
        currentMonth:1,
        currentDay:1,
        categoryTree:[],
        defaultProps: {
            children: 'children',
            label: 'label'
        }
    },
    methods:{
        handleCurrentChange:function(val){
            this.page.pageNum = val;
            this.search();
        },
        save:function(){
            var _self = this;
            axios.post(_contextPath+'/journal/save',_self.saveParams,{
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then(function(resp){
                if(resp.data){
                    _self.journalDialog = false;
                    _self.$notify({
                        title: '操作成功',
                        message: '新增日志成功',
                        type: 'success'
                    });
                    _self.journalForm.title='';
                    _self.journalForm.content='';
                    _self.ckeditor.setData('');
                    _self.search();
                }
            }).catch(function(e){
                console.log(e);
            });
        },
        handleView:function(index,row){
            this.currDetail = row.content;
            this.contentDialog = true;
        },
        handleEdit:function(index,row){
            var _self = this;
            this.journalDialog = true;
            this.currRow = row;
            this.$nextTick(function () {
                if(!_self.ckeditor){
                    _self.ckeditor = CKEDITOR.replace('ckeditor',
                        {
                            language:'zh-cn',
                            toolbar:[
                                { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike'] },
                                { name: 'paragraph', items: [ 'Outdent', 'Indent', 'JustifyLeft', 'JustifyCenter', 'JustifyRight' ] },
                                { name: 'insert', items: [ 'Image'] },
                                { name: 'styles', items: [ 'Format', 'FontSize' ] },
                                { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
                                { name: 'document', items: [ 'Preview' ] },
                                { name: 'tools', items: [ 'Maximize'] },
                                { name: 'clipboard', items: [ 'Undo', 'Redo' ] }
                            ],
                            filebrowserUploadUrl:"${mvcPath}/res/uploadRichText/file?path=richTextFile",
                            filebrowserImageUploadUrl:"${mvcPath}/res/uploadRichText/file?path=richTextImage",
                            filebrowserFlashUploadUrl:'${mvcPath}/res/uploadRichText/file?path=richTextFlash',
                            beforeUpload:function(){_self.uploadingNums++;},
                            afterUpload:function(){_self.uploadingNums--;},
                            height:150
                        });
                }
                _self.journalForm.title = row.title;
                _self.ckeditor.setData(row.content);
            });
        },
        search:function(){
            var _self = this;
            axios.get(_contextPath+'/journal/search',{params:_self.searchParams}).then(function (resp) {
                _self.journals = resp.data.content;
                _self.page.total = resp.data.totalElements;
            }).catch(function(e){
                console.log(e);
                _self.$message({
                    type:'error',
                    showClose: true,
                    message: '查询日志失败！'
                });
            });
        },
        newJournal:function(){
            this.journalDialog=true;
            var _self = this;
            this.journalForm.title = '';
            this.$nextTick(function () {
                if(!_self.ckeditor){
                    _self.ckeditor = CKEDITOR.replace('ckeditor',
                        {
                            language:'zh-cn',
                            toolbar:[
                                { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike'] },
                                { name: 'paragraph', items: [ 'Outdent', 'Indent', 'JustifyLeft', 'JustifyCenter', 'JustifyRight' ] },
                                { name: 'insert', items: [ 'Image'] },
                                { name: 'styles', items: [ 'Format', 'FontSize' ] },
                                { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
                                { name: 'document', items: [ 'Preview' ] },
                                { name: 'tools', items: [ 'Maximize'] },
                                { name: 'clipboard', items: [ 'Undo', 'Redo' ] }
                            ],
                            filebrowserUploadUrl:"${mvcPath}/res/uploadRichText/file?path=richTextFile",
                            filebrowserImageUploadUrl:"${mvcPath}/res/uploadRichText/file?path=richTextImage",
                            filebrowserFlashUploadUrl:'${mvcPath}/res/uploadRichText/file?path=richTextFlash',
                            beforeUpload:function(){_self.uploadingNums++;},
                            afterUpload:function(){_self.uploadingNums--;},
                            height:150
                        });
                }else{
                    _self.ckeditor.setData('');
                }
            });
        },
        handleYearChange:function(){
            this.loadCateTree();
        },
        loadYearOptions:function(){
            var _self = this;
            axios.get(_contextPath+'/category/searchByType',{params:{type:'year'}}).then(function (resp) {
                var temp = resp.data;
                var tempRes = [];
                temp.forEach(function (item) {
                    tempRes.push({value:item.label.substring(0,item.label.length-1),label:item.label});
                });
                _self.yearOptions = tempRes;
            }).catch(function(e){
                console.log(e);
                _self.$message({
                    type:'error',
                    showClose: true,
                    message: '加载年份选项失败！'
                });
            });
        },
        handleClickCategory:function(node){
            var _self = this;
            var temp = null;
            if(node.type==='DAY'&&node.parent){
                temp = this.nodeArr.filter(function (item) {
                    return item.id === node.parent;
                });
                if(temp.length>0&&temp[0].parent){
                    _self.filter.category = _self.currentYear+'年'+temp[0].value+'月'+node.value+'日';
                }
            }else if(node.type==='MONTH'&&node.parent){
                _self.filter.category = _self.currentYear+'年'+node.value+'月';
            }else if(node.type==='YEAR'){
                _self.filter.category = _self.currentYear+'年';
            }
            this.search();
        },
        loadCateTree:function(){
            var _self = this;
            axios.get(_contextPath+'/category/cateTree',{params:{startTime:new Date(_self.currentYear,_self.currentMonth,_self.currentDay).getTime(),endTime:new Date(_self.currentYear+1,_self.currentMonth,_self.currentDay).getTime()}}).then(function (resp) {
                _self.categoryTree = resp.data;
                var exportTree = function(tree,arr){
                    if(tree instanceof Array){
                        tree.forEach(function (item) {
                            arr.push(item);
                            if(item.children&&item.children instanceof Array){
                                exportTree(item.children,arr);
                            }
                        });
                    }
                };
                _self.nodeArr = [];
                exportTree(resp.data,_self.nodeArr);
                _self.$nextTick(function () {
                    if(_self.nodeArr.length>0){
                        _self.$refs.tree.setCurrentNode(_self.nodeArr[0]);
                        _self.search();
                    }
                })
            }).catch(function(e){
                console.log(e);
                _self.$message({
                    type:'error',
                    showClose: true,
                    message: '加载日志架构树失败！'
                });
            });
        }
    },
    computed:{
        saveParams:function () {
            return {id:this.currRow?this.currRow.id:null,title:this.journalForm.title,content:this.ckeditor.getData()};
        },
        searchParams:function () {
            return {keyWord:this.filter.keyWord,pageNum:this.page.pageNum-1,pageSize:this.page.pageSize,startTime:this.filter.dateRange&&this.filter.dateRange.length>0?this.filter.dateRange[0].getTime():null,endTime:this.filter.dateRange&&this.filter.dateRange.length>0?this.filter.dateRange[1].getTime():null,category:this.filter.category};
        }
    },
    watch: {
    },
    created:function(){
        var _self = this;
        _self.loadCateTree();
        _self.loadYearOptions();
    },
    mounted:function () {

    }
})