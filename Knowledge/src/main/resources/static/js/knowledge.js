var vm = new Vue({
    el:'#main',
    data:{
        filterTreeHeight:document.body.clientHeight-710,
        relaGraph:null,
        relaDialog:false,
        tabHeight:document.body.clientHeight-710,
        categoryOperTitle:'',
        rightNode:{
            node:null,
            data:null
        },
        rightMenu:{
          display:'',
          right:'',
          top:''
        },
        showRightMenu:false,
        showRelaButton:false,
        categoryDialog:false,
        categoryForm:{
            id:'',
            parentId:'',
            name:'',
            label:''
        },
        detailDialog:false,
        currDetail:'',
        ckeditor:null,
        categoryMap:new Map(),
        knowledgeDialog:false,
        formLabelWidth:'80px',
        knowledgeForm:{
            name:'',
            descr:'',
            category:''
        },
        filterText:'',
        categoryTree:[],
        categoryCascaderTree:[],
        treeProps:{
            children: 'children',
            label: 'label'
        },
        knowledgeData:[],
        categoryMap:new Map(),
        filter:{
            keyWord:'',
            category:'',
            pageNum:1,
            pageSize:20
        },
        knowledgeTotal:0
    },
    methods:{
        viewRela:function(){
            this.relaDialog = true;
            var _self = this;
            this.$nextTick(function () {
                _self.relaGraph = echarts.init(document.getElementById('relaGraph'));
                _self.relaGraph.showLoading();
                axios.get(_contextPath+'/knowledge/getKnowledgeTree',{
                    params:{nodeId:_self.rightNode.data.id}
                }).then(function(resp){
                    if(resp.data){
                        _self.relaGraph.hideLoading();
                        var initNodes = function (arr) {
                            var res = [];
                            if(arr&&arr.length>0){
                                arr.forEach(function(item){
                                    if(item.children==null||item.children.length==0){
                                        res.push({
                                            name:item.label,
                                            value:item.id
                                        });
                                    }else{
                                        res.push({
                                            name:item.label,
                                            children:initNodes(item.children),
                                            collapsed:true
                                        });
                                    }
                                });
                            }
                            return res;
                        };
                        var data = initNodes(resp.data);
                        data.forEach(function (item) {
                            if(item.collapsed!==undefined){
                                item.collapsed = false;
                            }
                            if(item.children&&item.children.length>0){
                                item.children.forEach(function (jtem) {
                                    if(jtem.collapsed!==undefined){
                                        jtem.collapsed = false;
                                    }
                                    if(jtem.children&&jtem.children.length>0){
                                        jtem.children.forEach(function (ktem) {
                                            if(ktem.collapsed!==undefined){
                                                ktem.collapsed = false;
                                            }
                                        })
                                    }
                                })
                            }
                        });
                        _self.relaGraph.setOption(option = {
                            tooltip: {
                                trigger: 'item',
                                triggerOn: 'mousemove'
                            },
                            series: [
                                {
                                    type: 'tree',

                                    data: data,

                                    top: '1%',
                                    left: '10%',
                                    bottom: '1%',
                                    right: '20%',

                                    symbolSize: 7,

                                    label: {
                                        normal: {
                                            position: 'left',
                                            verticalAlign: 'middle',
                                            align: 'right',
                                            fontSize: 13
                                        }
                                    },

                                    leaves: {
                                        label: {
                                            normal: {
                                                position: 'right',
                                                verticalAlign: 'middle',
                                                align: 'left'
                                            }
                                        }
                                    },

                                    expandAndCollapse: true,
                                    animationDuration: 550,
                                    animationDurationUpdate: 750
                                }
                            ]
                        });
                    }
                }).catch(function(e){
                    _self.relaGraph.hideLoading();
                    console.log(e);
                });
            });
        },
        clickPage:function(){
            this.showRightMenu = false;
        },
        treeRightClick:function(e,data,node){
            this.rightMenu.left= e.clientX+'px';
            this.rightMenu.top= e.clientY+'px';
            this.rightMenu.display = 'block';
            this.rightNode.node = node;
            this.rightNode.data = data;
            this.showRelaButton = !node.isLeaf;
            this.showRightMenu = true;
        },
        saveCategory:function(){
            var _self = this;
            axios.post(_contextPath+'/category/save',_self.saveCategoryParams,{
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then(function(resp){
                if(resp.data){
                    _self.categoryDialog = false;
                    _self.categoryForm.id = '';
                    _self.categoryForm.parentId = '';
                    _self.categoryForm.name = '';
                    _self.categoryForm.label = '';
                    _self.getCategoryTree();
                }
            }).catch(function(e){
                console.log(e);
            });
        },
        appendCategory:function(){
            this.categoryOperTitle = '新增类目';
            this.categoryDialog = true;
            this.categoryForm.parentId = this.rightNode.data.id;
        },
        editCategory:function(){
            this.categoryOperTitle = '编辑类目';
            this.categoryDialog = true;
            this.categoryForm.id = this.rightNode.data.id;
            this.categoryForm.parentId = this.rightNode.data.parentId;
            this.categoryForm.name = this.rightNode.data.name;
            this.categoryForm.label = this.rightNode.data.label;
        },
        removeCategory:function(){
            var _self = this;
            if(confirm("确认删除吗？")){
                axios.post(_contextPath+'/category/delete/'+_self.rightNode.data.id
                ).then(function(resp){
                    _self.getCategoryTree();
                }).catch(function(e){
                    console.log(e);
                });
            }
        },
        viewDetail:function(index,row){
            this.currDetail = row.detail;
            this.detailDialog = true;
        },
        deleteKnowledge:function(index,row){
            var _self = this;
            if(confirm("确认删除吗？")){
                axios.post(_contextPath+'/knowledge/delete/'+row.id
                ).then(function(resp){
                    _self.search();
                }).catch(function(e){
                    console.log(e);
                });
            }
        },
        save:function(){
            var _self = this;
            axios.post(_contextPath+'/knowledge/save',_self.saveParams,{
                    headers: {
                        "Content-Type": "application/json;charset=utf-8"
                    }
            }).then(function(resp){
                if(resp.data){
                    _self.knowledgeDialog = false;
                    _self.knowledgeForm.name='';
                    _self.knowledgeForm.descr='';
                    _self.ckeditor.setData('');
                    _self.search();
                }
            }).catch(function(e){
                console.log(e);
            });
        },
        clickTreeNode:function(data){
            this.showRightMenu = false;
            var filterFromTree = function(arr,id){
                var temp = arr.filter(function(item){
                    return item.id==id;
                });
                if(temp.length>0){
                    var res = [];
                    res.push(temp[0].id);
                    return res;
                }else{
                    var temp2 = arr.filter(function(item){
                        if(item.children&&item.children.length>0){
                            return filterFromTree(item.children,id);
                        }
                    })
                    if(temp2.length>0){
                        var res2 = [];
                        res2.push(temp2[0])
                        return res2.concat(filterFromTree(temp2[0].children,id));
                    }
                }
            };
            this.filter.category=data.id;
            this.search();
            this.knowledgeForm.category = filterFromTree(this.categoryCascaderTree,data.id)
        },
        filterCascade:function(node,key){
            if(!key) return true;
            return node.label.toLowerCase().indexOf(key.toLowerCase())!==-1;
        },
        toAdd:function(){
            this.knowledgeDialog=true;
            var _self = this;
            this.$nextTick(function () {
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
            });
        },
        filterNode:function(value,data){
            if (!value) return true;
            return data.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
        },
        getCategoryOptions:function(){
            var _self = this;
            axios.get(_contextPath+'/category/search').then(function(resp){
                if(resp.data){
                    resp.data.forEach(function(item){
                        _self.categoryMap.set(item.id,item.label);
                    });
                }
            }).catch(function(e){
                console.log(e);
            });
        },
        getCategoryTree:function(){
            var _self = this;
            axios.get(_contextPath+'/category/getTree').then(function(resp){
                if(resp.data){
                    _self.categoryTree = resp.data;
                    _self.categoryCascaderTree = resp.data[0].children;
                }
            }).catch(function(e){
                console.log(e);
            });
        },
        changePageSize:function(val){
            this.filter.pageSize=val;
            this.filter.pageNum = 1;
            this.search();
        },
        changeCurrPage:function(val){
            this.filter.pageNum = val;
            this.search();
        },
        formatCreateTime:function(row){
            return new Date(row.createDate).toLocaleDateString().replace('/','-').replace('/','-');
        },
        search:function(func){
            var _self = this;
            axios.get(_contextPath+'/knowledge/search',{
                params:_self.searchParams
            }).then(function(resp){
                if(resp.data){
                    _self.knowledgeData = resp.data.content;
                    _self.knowledgeTotal = resp.data.totalElements;
                }
            }).catch(function(e){
                console.log(e);
            });
        }
    },
    computed:{
      searchParams:function(){
          var res = {pageNum:this.filter.pageNum-1,pageSize:this.filter.pageSize,keyWord:this.filter.keyWord,category:this.filter.category};
          return res;
      },
        saveParams:function () {
            var res = {name:this.knowledgeForm.name,descr:this.knowledgeForm.descr,category:this.knowledgeForm.category[this.knowledgeForm.category.length-1],detail:this.ckeditor.getData()};
            return res;
        },
        saveCategoryParams:function () {
            var res = {id:this.categoryForm.id,name:this.categoryForm.name,label:this.categoryForm.label,parentId:this.categoryForm.parentId};
            return res;
        }
    },
    watch: {
        filterText:function(val) {
            this.$refs.tree.filter(val);
        }
    },
    created:function(){
        this.getCategoryOptions();
        this.getCategoryTree();
        this.search();
    },
    mounted:function () {

    }
})