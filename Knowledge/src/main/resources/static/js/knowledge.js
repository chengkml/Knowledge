var vm = new Vue({
    el: '#main',
    data: {
        questionRules: {
            type: [
                {required: true, message: '请选择题型', trigger: 'change'}
            ],
            stem: [
                {required: true, message: '请输入题干', trigger: 'blur'}
            ],
            result: [
                {required: true, message: '请设置答案', trigger: 'change'}
            ],
            resultArr: [
                {
                    validator: function (rule, value, callback) {
                        if(value.length===0){
                            callback(new Error('请设置答案'));
                            return false;
                        }else{
                            callback();
                            return true;
                        }
                    }, trigger: 'change'
                }
            ],
            optionA: [
                {required: true, message: '请设置选项', trigger: 'blur'}
            ],
            optionB: [
                {required: true, message: '请设置选项', trigger: 'blur'}
            ],
            optionC: [
                {required: true, message: '请设置选项', trigger: 'blur'}
            ],
            optionD: [
                {required: true, message: '请设置选项', trigger: 'blur'}
            ]
        },
        questionForms: [],
        knowledgeTabs: [],
        currTab: 'main',
        rules: {
            category: [
                {required: true, message: '请选择知识点类目', trigger: 'change'}
            ],
            name: [
                {required: true, message: '请输入知识点名称', trigger: 'blur'}
            ],
            descr: [
                {required: true, message: '请输入知识点描述', trigger: 'blur'}
            ]
        },
        showContentButton: true,
        currRow: null,
        showTabRightMenu: false,
        filterTreeHeight: document.body.clientHeight - 1215,
        relaGraph: null,
        relaDialog: false,
        tabHeight: document.body.clientHeight - 1215,
        categoryOperTitle: '',
        rightNode: {
            node: null,
            data: null
        },
        rightMenu: {
            display: '',
            right: '',
            top: ''
        },
        tabRightMenu: {
            display: '',
            right: '',
            top: ''
        },
        showRightMenu: false,
        showRelaButton: false,
        categoryDialog: false,
        categoryForm: {
            id: '',
            parentId: '',
            name: '',
            label: ''
        },
        detailDialog: false,
        currDetail: '',
        ckeditor: null,
        categoryMap: new Map(),
        knowledgeDialog: false,
        formLabelWidth: '70px',
        knowledgeForm: {
            id: '',
            name: '',
            descr: '',
            category: ''
        },
        filterText: '',
        categoryTree: [],
        categoryCascaderTree: [],
        treeProps: {
            children: 'children',
            label: 'label'
        },
        knowledgeData: [],
        categoryMap: new Map(),
        filter: {
            keyWord: '',
            category: '',
            pageNum: 1,
            pageSize: 20
        },
        knowledgeTotal: 0
    },
    methods: {
        toExercise:function(){
            var _self = this;
            axios.get(_contextPath + '/exercise/generateExercise?size=100').then(function (resp) {
                _self.$notify({
                    type: 'success',
                    title: '操作成功',
                    message: '生成练习成功，练习已发至邮箱',
                    duration: 3000
                });
            }).catch(function(e){
                console.log(e);
                _self.$notify({
                    type: 'error',
                    title: '操作失败',
                    message: '生成练习失败，失败原因：' + e,
                    duration: 3000
                });
            })
        },
        addQuestionTab: function () {
            var _self = this;
            var index = this.knowledgeTabs.length;
            this.questionForms.push({
                id:'',
                type: 'judge',
                stem: _self.knowledgeForm.name,
                result: '',
                optionA: '',
                optionB: '',
                optionC: '',
                optionD: '',
                options: '',
                resultArr:[]
            });
            var newTabName = index + 1;
            while (this.tabNameIndexs.indexOf(newTabName) !== -1) {
                newTabName++;
            }
            this.knowledgeTabs.push({
                title: '题目' + newTabName,
                name: newTabName + ''
            });
            this.currTab = newTabName + '';
        },
        removeTab(targetName) {
            var tabs = this.knowledgeTabs;
            var targetTab = tabs.filter(function (tab) {
                return tab.name === targetName
            })[0];
            var index = tabs.indexOf(targetTab);
            this.questionForms.splice(index, 1);
            var activeName = this.currTab;
            if (activeName === targetName) {
                tabs.forEach(function (tab, index) {
                    if (tab.name === targetName) {
                        var nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            activeName = nextTab.name;
                        }
                    }
                });
            }

            this.currTab = activeName;
            this.knowledgeTabs = tabs.filter(function (tab) {
                return tab.name !== targetName
            });
        },
        editKnowledge: function () {
            this.knowledgeForm.id = this.currRow.id;
            this.knowledgeForm.name = this.currRow.name;
            this.knowledgeForm.descr = this.currRow.descr;
            this.knowledgeForm.category = this.filterFromTree(this.categoryCascaderTree, this.currRow.category)
            this.knowledgeDialog = true;
            var _self = this;
            this.loadCkEditor(function () {
                _self.ckeditor.setData(_self.currRow.detail);
            });
            _self.knowledgeTabs = [];
            this.currRow.questions.forEach(function (item) {
                var index = _self.knowledgeTabs.length;
                var newTabName = index + 1;
                while (_self.tabNameIndexs.indexOf(newTabName) !== -1) {
                    newTabName++;
                }
                _self.knowledgeTabs.push({
                    title: item.stem,
                    name: newTabName+''
                });
                var tempArr = [];
                if(item.type==='select'){
                    tempArr = item.options.split(',');
                    _self.questionForms.push({
                        id:item.id,
                        type: item.type,
                        stem: item.stem,
                        result: item.result,
                        optionA: tempArr[0],
                        optionB: tempArr[1],
                        optionC: tempArr[2],
                        optionD: tempArr[3],
                        options: item.options,
                        resultArr:[]
                    });
                }else if(item.type==='multiSelect'){
                    tempArr = item.options.split(',');
                    _self.questionForms.push({
                        id:item.id,
                        type: item.type,
                        stem: item.stem,
                        result: item.result,
                        optionA: tempArr[0],
                        optionB: tempArr[1],
                        optionC: tempArr[2],
                        optionD: tempArr[3],
                        options: item.options,
                        resultArr:item.result.split(',')
                    });
                }else{
                    _self.questionForms.push({
                        id:item.id,
                        type: item.type,
                        stem: item.stem,
                        result: item.result,
                        optionA: '',
                        optionB: '',
                        optionC: '',
                        optionD: '',
                        options: [],
                        resultArr:[]
                    });
                }
            });
        },
        tabRightClick: function (row, column, e) {
            e.preventDefault();
            this.tabRightMenu.left = e.clientX + 'px';
            this.tabRightMenu.top = e.clientY + 'px';
            this.tabRightMenu.display = 'block';
            this.showTabRightMenu = true;
            this.showDetailButton = !!(row.detail);
            this.currRow = row;
        },
        viewRela: function () {
            this.relaDialog = true;
            var _self = this;
            this.$nextTick(function () {
                _self.relaGraph = echarts.init(document.getElementById('relaGraph'));
                _self.relaGraph.showLoading();
                axios.get(_contextPath + '/knowledge/getKnowledgeTree', {
                    params: {nodeId: _self.rightNode.data.id}
                }).then(function (resp) {
                    if (resp.data) {
                        _self.relaGraph.hideLoading();
                        var initNodes = function (arr) {
                            var res = [];
                            if (arr && arr.length > 0) {
                                arr.forEach(function (item) {
                                    if (item.children == null || item.children.length == 0) {
                                        res.push({
                                            name: item.label,
                                            value: item.id
                                        });
                                    } else {
                                        res.push({
                                            name: item.label,
                                            children: initNodes(item.children),
                                            collapsed: true
                                        });
                                    }
                                });
                            }
                            return res;
                        };
                        var data = initNodes(resp.data);
                        data.forEach(function (item) {
                            if (item.collapsed !== undefined) {
                                item.collapsed = false;
                            }
                            if (item.children && item.children.length > 0) {
                                item.children.forEach(function (jtem) {
                                    if (jtem.collapsed !== undefined) {
                                        jtem.collapsed = false;
                                    }
                                    if (jtem.children && jtem.children.length > 0) {
                                        jtem.children.forEach(function (ktem) {
                                            if (ktem.collapsed !== undefined) {
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
                }).catch(function (e) {
                    _self.relaGraph.hideLoading();
                    console.log(e);
                });
            });
        },
        clickPage: function () {
            this.showRightMenu = false;
            this.showTabRightMenu = false;
        },
        treeRightClick: function (e, data, node) {
            this.rightMenu.left = e.clientX + 'px';
            this.rightMenu.top = e.clientY + 'px';
            this.rightMenu.display = 'block';
            this.rightNode.node = node;
            this.rightNode.data = data;
            this.showRelaButton = !node.isLeaf;
            this.showRightMenu = true;
        },
        saveCategory: function () {
            var _self = this;
            axios.post(_contextPath + '/category/save', _self.saveCategoryParams, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then(function (resp) {
                if (resp.data) {
                    _self.categoryDialog = false;
                    _self.categoryForm.id = '';
                    _self.categoryForm.parentId = '';
                    _self.categoryForm.name = '';
                    _self.categoryForm.label = '';
                    _self.getCategoryTree();
                }
            }).catch(function (e) {
                console.log(e);
            });
        },
        appendCategory: function () {
            this.categoryOperTitle = '新增类目';
            this.categoryDialog = true;
            this.categoryForm.parentId = this.rightNode.data.id;
        },
        editCategory: function () {
            this.categoryOperTitle = '编辑类目';
            this.categoryDialog = true;
            this.categoryForm.id = this.rightNode.data.id;
            this.categoryForm.parentId = this.rightNode.data.parentId;
            this.categoryForm.name = this.rightNode.data.name;
            this.categoryForm.label = this.rightNode.data.label;
        },
        removeCategory: function () {
            var _self = this;
            if (confirm("确认删除吗？")) {
                axios.post(_contextPath + '/category/delete/' + _self.rightNode.data.id
                ).then(function (resp) {
                    _self.getCategoryTree();
                }).catch(function (e) {
                    console.log(e);
                });
            }
        },
        viewDetail: function () {
            this.currDetail = this.currRow.detail;
            this.detailDialog = true;
        },
        deleteKnowledge: function () {
            var _self = this;
            if (confirm("确认删除吗？")) {
                axios.post(_contextPath + '/knowledge/delete/' + _self.currRow.id
                ).then(function (resp) {
                    _self.search();
                }).catch(function (e) {
                    console.log(e);
                });
            }
        },
        save: function () {
            var _self = this;
            if (this.$refs.knowledgeForm) {
                this.$refs.knowledgeForm.validate(function (res) {
                    if (res) {
                        if (_self.questionForms.length > 0) {
                            var index = 0;
                            var validated = true;
                            var inValidIndex = -1;
                            _self.questionForms.forEach(function (item) {
                                _self.$refs['question' + index][0].validate(function (subRes) {
                                    if (!subRes) {
                                        validated = false;
                                        if (inValidIndex === -1) {
                                            inValidIndex = index;
                                        }
                                    }
                                });
                                index++;
                            });
                            if (validated) {
                                _self.doSave();
                            } else {
                                _self.currTab = inValidIndex + 1 + '';
                            }
                        } else {
                            _self.doSave();
                        }
                    } else {
                        _self.currTab = 'main';
                    }
                });
            }
        },
        doSave: function () {
            var _self = this;
            axios.post(_contextPath + '/knowledge/save', _self.saveParams, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then(function (resp) {
                if (resp.data) {
                    _self.knowledgeDialog = false;
                    _self.knowledgeForm.id = '';
                    _self.knowledgeForm.name = '';
                    _self.knowledgeForm.descr = '';
                    _self.ckeditor.setData('');
                    _self.knowledgeTabs = [];
                    _self.questionForms = [];
                    _self.search();
                    _self.$notify({
                        type: 'success',
                        title: '操作成功',
                        message: '保存成功',
                        duration: 3000
                    });
                }
            }).catch(function (e) {
                console.log(e);
                _self.$notify({
                    type: 'error',
                    title: '操作失败',
                    message: '保存失败，失败原因：' + e,
                    duration: 3000
                });
            });
        },
        clickTreeNode: function (data) {
            this.showRightMenu = false;
            this.filter.category = data.id;
            this.search();
            this.knowledgeForm.category = this.filterFromTree(this.categoryCascaderTree, data.id)
        },
        filterFromTree: function (arr, id) {
            var _self = this;
            var temp = arr.filter(function (item) {
                return item.id == id;
            });
            if (temp.length > 0) {
                var res = [];
                res.push(temp[0].id);
                return res;
            } else {
                var temp2 = arr.filter(function (item) {
                    if (item.children && item.children.length > 0) {
                        return _self.filterFromTree(item.children, id);
                    }
                })
                if (temp2.length > 0) {
                    var res2 = [];
                    res2.push(temp2[0])
                    return res2.concat(_self.filterFromTree(temp2[0].children, id));
                }
            }
        },
        filterCascade: function (node, key) {
            if (!key) return true;
            return node.label.toLowerCase().indexOf(key.toLowerCase()) !== -1;
        },
        toAdd: function () {
            var _self = this;
            _self.knowledgeForm.id = '';
            _self.knowledgeForm.name = '';
            _self.knowledgeForm.descr = '';
            _self.knowledgeTabs = [];
            _self.questionForms = [];
            this.knowledgeDialog = true;
            this.loadCkEditor(function () {
                _self.knowledgeForm.id = '';
                _self.knowledgeForm.name = '';
                _self.knowledgeForm.descr = '';
                _self.ckeditor.setData('');
            });
        },
        loadCkEditor: function (func) {
            var _self = this;
            this.$nextTick(function () {
                if (!_self.ckeditor) {
                    _self.ckeditor = CKEDITOR.replace('ckeditor',
                        {
                            language: 'zh-cn',
                            toolbar: [
                                {name: 'basicstyles', items: ['Bold', 'Italic', 'Underline', 'Strike']},
                                {
                                    name: 'paragraph',
                                    items: ['Outdent', 'Indent', 'JustifyLeft', 'JustifyCenter', 'JustifyRight']
                                },
                                {name: 'insert', items: ['Image']},
                                {name: 'styles', items: ['Format', 'FontSize']},
                                {name: 'colors', items: ['TextColor', 'BGColor']},
                                {name: 'document', items: ['Preview']},
                                {name: 'tools', items: ['Maximize']},
                                {name: 'clipboard', items: ['Undo', 'Redo']}
                            ],
                            filebrowserUploadUrl: "${mvcPath}/res/uploadRichText/file?path=richTextFile",
                            filebrowserImageUploadUrl: "${mvcPath}/res/uploadRichText/file?path=richTextImage",
                            filebrowserFlashUploadUrl: '${mvcPath}/res/uploadRichText/file?path=richTextFlash',
                            beforeUpload: function () {
                                _self.uploadingNums++;
                            },
                            afterUpload: function () {
                                _self.uploadingNums--;
                            },
                            height: 150
                        });
                }
                if (func) {
                    func.apply();
                }
            });
        },
        filterNode: function (value, data) {
            if (!value) return true;
            return data.label.toLowerCase().indexOf(value.toLowerCase()) !== -1;
        },
        getCategoryOptions: function () {
            var _self = this;
            axios.get(_contextPath + '/category/search').then(function (resp) {
                if (resp.data) {
                    resp.data.forEach(function (item) {
                        _self.categoryMap.set(item.id, item.label);
                    });
                }
            }).catch(function (e) {
                console.log(e);
            });
        },
        getCategoryTree: function () {
            var _self = this;
            axios.get(_contextPath + '/category/getTree').then(function (resp) {
                if (resp.data) {
                    _self.categoryTree = resp.data;
                    _self.categoryCascaderTree = resp.data[0].children;
                }
            }).catch(function (e) {
                console.log(e);
            });
        },
        changePageSize: function (val) {
            this.filter.pageSize = val;
            this.filter.pageNum = 1;
            this.search();
        },
        changeCurrPage: function (val) {
            this.filter.pageNum = val;
            this.search();
        },
        formatCreateTime: function (row) {
            return new Date(row.createDate).toLocaleDateString().replace('/', '-').replace('/', '-');
        },
        search: function (func) {
            var _self = this;
            axios.get(_contextPath + '/knowledge/search', {
                params: _self.searchParams
            }).then(function (resp) {
                if (resp.data) {
                    _self.knowledgeData = resp.data.content;
                    _self.knowledgeTotal = resp.data.totalElements;
                }
            }).catch(function (e) {
                console.log(e);
            });
        }
    },
    computed: {
        tabNameIndexs: function () {
            var res = [];
            this.knowledgeTabs.forEach(function (item) {
                res.push(Number(item.name));
            });
            return res;
        },
        searchParams: function () {
            var res = {
                pageNum: this.filter.pageNum - 1,
                pageSize: this.filter.pageSize,
                keyWord: this.filter.keyWord,
                category: this.filter.category
            };
            return res;
        },
        saveParams: function () {
            this.questionForms.forEach(function (item) {
                if (item.type === 'select'||item.type === 'multiSelect') {
                    item.options = item.optionA + ',' + item.optionB + ',' + item.optionC + ',' + item.optionD;
                }
                if(item.type === 'multiSelect'){
                    item.result = item.resultArr.join(',');
                }
            })
            var res = {
                id: this.knowledgeForm.id,
                name: this.knowledgeForm.name,
                descr: this.knowledgeForm.descr,
                category: this.knowledgeForm.category[this.knowledgeForm.category.length - 1],
                detail: this.ckeditor.getData(),
                questions: this.questionForms
            };
            return res;
        },
        saveCategoryParams: function () {
            var res = {
                id: this.categoryForm.id,
                name: this.categoryForm.name,
                label: this.categoryForm.label,
                parentId: this.categoryForm.parentId
            };
            return res;
        }
    },
    watch: {
        filterText: function (val) {
            this.$refs.tree.filter(val);
        },
        knowledgeTabs: function (val) {
            if (val.length === 0) {
                this.currTab = 'main';
            }
        }
    },
    created: function () {
        this.getCategoryOptions();
        this.getCategoryTree();
        this.search();
    },
    mounted: function () {

    }
})