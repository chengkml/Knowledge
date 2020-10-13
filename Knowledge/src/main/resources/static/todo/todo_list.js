var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            stateMap:{},
            stateOptions:[],
            pickerOptions: {
                shortcuts: [{
                    text: '明天',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() + 3600 * 1000 * 24);
                        picker.$emit('pick', date);
                    }
                },  {
                    text: '一周后',
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', date);
                    }
                }]
            },
            groupMap: {},
            currTab: 'main',
            rules: {
                groupId: [
                    {required: true, message: '请选择类目', trigger: 'change'}
                ],
                name: [
                    {required: true, message: '请输入Todo标题', trigger: 'blur'}
                ],
                estimateStartTime: [
                    {required: true, message: '请选择预计开始时间', trigger: 'change'}
                ],
                estimateEndTime: [
                    {required: true, message: '请选择预计完成时间', trigger: 'change'}
                ],
                leadTime: [
                    {required: true, message: '请选择交付时间', trigger: 'change'}
                ]
            },
            showContentButton: true,
            currRow: null,
            showTabRightMenu: false,
            filterTreeHeight: 400,
            relaGraph: null,
            relaDialog: false,
            tabHeight: 400,
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
                descr: ''
            },
            detailDialog: false,
            currDetail: '',
            todoDialog: false,
            formLabelWidth: '110px',
            todoForm: {
                id: '',
                name: '',
                groupId: [],
                estimateStartTime:'',
                estimateEndTime:'',
                leadTime:'',
                createDate:'',
                finishTime:''
            },
            filterText: '',
            categoryTree: [],
            categoryCascaderTree: [],
            treeProps: {
                children: 'children',
                label: 'descr'
            },
            knowledgeData: [],
            filter: {
                keyWord: '',
                states:['waiting','running'],
                group: '',
                pageNum: 1,
                pageSize: 20
            },
            knowledgeTotal: 0
        }
    },
    methods: {

        finishItem(){
            var params = $.extend({},this.currRow);
            params.state='finish';
            params.finishTime = dateFormat('yyyy-MM-dd hh:mm:ss',new Date());
            axios.post(_contextPath + '/todo/save', params).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.list();
                    this.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '完成Todo项目成功！'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '完成Todo项目失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '完成Todo项目失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '完成Todo项目失败!'
                });
                console.error(err);
            });
        },
        groupTree: function () {
            axios.get(_contextPath + '/todo/group/tree').then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.categoryTree = resp.data.data;
                    if (resp.data.data.length > 0) {
                        this.categoryCascaderTree = resp.data.data;
                    }
                    var expand = function (tree, map) {
                        if (tree.length > 0) {
                            tree.forEach((item) => {
                                map[item.id] = item.descr;
                                if (item.children) {
                                    expand(item.children, map);
                                }
                            });
                        }
                    }
                    expand(resp.data.data, this.groupMap);
                } else if (resp && resp.data && resp.data.msg) {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Todo分组树失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Todo分组树失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    showClose: true,
                    message: '查询Todo分组树失败!'
                });
                console.error(err);
            });
        },

        getTabHeight: function () {
            this.tabHeight = window.innerHeight - 105;
            this.filterTreeHeight = window.innerHeight - 105;
        },

        addLayoutListen: function () {
            this.getTabHeight();
            if (window.addEventListener) {
                window.addEventListener('resize', this.getTabHeight)
            } else if (window.attachEvent) {
                window.attachEvent('onresize', this.getTabHeight)
            }
        },

        editKnowledge: function () {
            this.todoForm.id = this.currRow.id;
            this.todoForm.name = this.currRow.name;
            this.todoForm.groupId = this.filterFromTree(this.categoryCascaderTree, this.currRow.groupId);
            this.todoForm.estimateStartTime = new Date(this.currRow.estimateStartTime);
            this.todoForm.estimateEndTime = new Date(this.currRow.estimateEndTime);
            this.todoForm.leadTime = new Date(this.currRow.estimateEndTime);
            this.todoForm.createDate = new Date(this.currRow.createDate);
            this.todoForm.finishTime = new Date(this.currRow.finishTime);
            this.todoDialog = true;
        },
        tabRightClick: function (row, column, e) {
            e.preventDefault();
            this.tabRightMenu.left = e.clientX + 'px';
            this.tabRightMenu.top = e.clientY + 'px';
            this.tabRightMenu.display = 'block';
            this.showTabRightMenu = true;
            this.currRow = row;
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
        addGroup: function () {
            axios.post(_contextPath + '/todo/group/add', this.saveCategoryParams).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.categoryDialog = false;
                    this.categoryForm.id = '';
                    this.categoryForm.parentId = '';
                    this.categoryForm.name = '';
                    this.categoryForm.descr = '';
                    this.groupTree();
                    this.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '增加Todo分组成功!'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '增加Todo分组失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '增加Todo分组失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '增加Todo分组失败!'
                });
                console.error(err);
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
            this.categoryForm.descr = this.rightNode.data.descr;
        },
        confirmDeleteGroup() {
            this.$confirm('确定删除该分组及其子分组吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteGroup();
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '删除分组失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },
        deleteGroup: function () {
            var collect = function (tree, arr) {
                if (tree && tree.length > 0) {
                    tree.forEach((item) => {
                        arr.push(item.id);
                        if (item.children) {
                            collect(item.children, arr);
                        }
                    });
                }
            };
            var params = [this.rightNode.data.id];
            collect(this.rightNode.data.children, params);
            axios.post(_contextPath + '/todo/group/delete', params, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.groupTree();
                    this.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '删除Todo分组成功!'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '删除Todo分组失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '删除Todo分组失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '删除Todo分组失败!'
                });
                console.error(err);
            });
        },
        viewDetail: function () {
            this.currDetail = this.currRow.detail;
            this.detailDialog = true;
        },
        confirmDeleteItem() {
            this.$confirm('确定删除该项目吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.delete();
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '删除项目失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },
        delete: function () {
            axios.post(_contextPath + '/todo/delete', this.currRow.id, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.list();
                    this.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '删除Todo项目成功!'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '删除Todo项目失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '删除Todo项目失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '删除Todo项目失败!'
                });
                console.error(err);
            });
        },

        save: function () {
            if (this.$refs.todoForm) {
                this.$refs.todoForm.validate((res) => {
                    if (res) {
                        this.doSave();
                    }
                });
            }
        },
        doSave: function () {
            axios.post(_contextPath + '/todo/save', this.saveParams).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.todoDialog = false;
                    this.list();
                    this.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '保存Todo项目成功!'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '保存Todo项目失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '保存Todo项目失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '保存Todo项目失败!'
                });
                console.error(err);
            });
        },

        clickTreeNode: function (data) {
            this.showRightMenu = false;
            this.filter.group = data.id;
            this.list();
            this.todoForm.groupId = this.filterFromTree(this.categoryCascaderTree, data.id);
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
                    res2.push(temp2[0].id)
                    return res2.concat(_self.filterFromTree(temp2[0].children, id));
                }
            }
        },
        filterCascade: function (node, key) {
            if (!key) return true;
            return node.data.name.toLowerCase().indexOf(key.toLowerCase()) !== -1;
        },
        toAdd: function () {
            this.todoForm.id = '';
            this.todoForm.name = '';
            this.todoDialog = true;
        },
        filterNode: function (value, data) {
            if (!value) return true;
            return data.descr.toLowerCase().indexOf(value.toLowerCase()) !== -1||data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
        },
        changePageSize: function (val) {
            this.filter.pageSize = val;
            this.filter.pageNum = 1;
            this.list();
        },
        changeCurrPage: function (val) {
            this.filter.pageNum = val;
            this.list();
        },

        list: function () {
            axios.get(_contextPath + '/todo/list', {
                params: this.searchParams
            }).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.knowledgeData = resp.data.data.content;
                    this.knowledgeTotal = resp.data.data.totalElements;
                } else if (resp && resp.data && resp.data.msg) {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Todo列表失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Todo列表失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    showClose: true,
                    message: '查询Todo列表失败!'
                });
                console.error(err);
            });
        }

    },
    computed: {
        searchParams: function () {
            var res = $.extend({},this.filter);
            res.pageNum--;
            res.states = res.states.join(',');
            return res;
        },
        saveParams: function () {
            var res = {
                id: this.todoForm.id,
                name: this.todoForm.name,
                groupId: this.todoForm.groupId[this.todoForm.groupId.length - 1],
                estimateStartTime:dateFormat('yyyy-MM-dd hh:mm:ss',this.todoForm.estimateStartTime),
                estimateEndTime:dateFormat('yyyy-MM-dd hh:mm:ss',this.todoForm.estimateEndTime),
                leadTime:dateFormat('yyyy-MM-dd hh:mm:ss',this.todoForm.leadTime),
                createDate:dateFormat('yyyy-MM-dd hh:mm:ss',this.todoForm.createDate),
                finishTime:dateFormat('yyyy-MM-dd hh:mm:ss',this.todoForm.finishTime)
            };
            return res;
        },
        saveCategoryParams: function () {
            var res = {
                id: this.categoryForm.id,
                name: this.categoryForm.name,
                descr: this.categoryForm.descr,
                parentId: this.categoryForm.parentId
            };
            return res;
        }
    },
    watch: {
        filterText: function (val) {
            this.$refs.tree.filter(val);
        }
    },
    created: function () {
        this.groupTree();
        this.list();
        this.getTabHeight();
        this.addLayoutListen();
        loadEnum(this, 'todo:item:state','项目执行状态',this.stateOptions,this.stateMap);
    }
})