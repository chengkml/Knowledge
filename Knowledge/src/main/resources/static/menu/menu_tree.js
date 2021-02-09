const vm = new Vue({
    el: '#main',
    data: function () {
        return {
            currNodeData:null,
            formLabelWidth: '90px',
            saveMenuDialog: false,
            form: {
                key: '',
                name: '',
                label: '',
                descr: '',
                sort:0,
                url: '',
                parent: '',
                valid: ''
            },
            rules: {
                name: [
                    {required: true, message: '请输入页面编码', trigger: 'blur'},
                    {min: 2, max: 64, message: '长度在 2 到 64 个字符', trigger: 'blur'}
                ],
                label: [
                    {required: true, message: '请输入页面标题', trigger: 'blur'},
                    {min: 2, max: 64, message: '长度在 2 到 64 个字符', trigger: 'blur'}
                ],
                sort: [
                    {required: true, message: '请输入页面序号', trigger: 'blur'}
                ]
            },
            currObj: null,
            graphHeight: 400,
            myDiagram: null
        }
    },
    methods: {
        getTabHeight: function () {
            this.dialogHeight = window.innerHeight*0.7-120;
        },
        batchDelete: function (ids) {
            var _self = this;
            axios.post(_contextPath + '/menu/batch/delete', ids).then(function (resp) {
                if (resp && resp.data && resp.data.success) {
                    _self.$notify({
                        type: 'success',
                        title: '操作成功',
                        showClose: true,
                        message: '批量删除菜单成功！'
                    });
                } else if (resp && resp.data && resp.data.msg) {
                    _self.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '批量删除菜单失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    _self.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '批量删除菜单失败!'
                    });
                    console.error(resp);
                }
            }).catch(function (err) {
                _self.$notify({
                    type: 'error',
                    title: '操作失败',
                    showClose: true,
                    message: '批量删除菜单失败!'
                });
                console.error(err);
            });
        },

        toAddMenu() {
            this.form = {
                key: '',
                name: '',
                label: '',
                descr: '',
                url: '',
                parent: this.currObj.key,
                valid: 'valid'
            };
            this.saveMenuDialog = true;
            this.$nextTick(() => {
                if (this.$refs.menuForm) {
                    this.$refs.menuForm.clearValidate();
                }
            });
        },

        saveMenu(func, errFunc) {
            this.$refs.menuForm.validate((valid) => {
                if (valid) {
                    var params = {
                        id:this.form.key,
                        name: this.form.name,
                        label: this.form.label,
                        descr: this.form.descr,
                        url: this.form.url,
                        parentId: this.form.parent,
                        sort:this.form.sort,
                        valid: this.form.valid
                    };
                    axios.post(_contextPath + '/menu/add', params).then((resp) => {
                        if (resp && resp.data && resp.data.success) {
                            if (func && func instanceof Function) {
                                func();
                            }
                            if(!this.form.key){
                                this.myDiagram.model.addNodeData({
                                    key: resp.data.data,
                                    name: this.form.name,
                                    label: this.form.label,
                                    descr: this.form.descr,
                                    sort:this.form.sort,
                                    url: this.form.url,
                                    parent: this.form.parent,
                                    valid: this.form.valid
                                });
                            }else{
                                this.currNodeData.key = resp.data.data;
                                this.currNodeData.name = this.form.name;
                                this.currNodeData.label = this.form.label;
                                this.currNodeData.descr = this.form.descr;
                                this.currNodeData.sort = this.form.sort;
                                this.currNodeData.url = this.form.url;
                                this.currNodeData.parent = this.form.parent;
                                this.currNodeData.valid = this.form.valid;
                            }
                            this.form.key = '';
                            this.form.name = '';
                            this.form.label = '';
                            this.form.descr = '';
                            this.form.sort = '';
                            this.form.url = '';
                            this.form.parent = '';
                            this.form.valid = '';
                            this.saveMenuDialog = false;
                            this.$notify({
                                type: 'success',
                                title: '操作成功',
                                showClose: true,
                                message: '添加菜单成功！'
                            });
                        } else if (resp && resp.data && resp.data.msg) {
                            if (errFunc && errFunc instanceof Function) {
                                errFunc();
                            }
                            this.$notify({
                                type: 'error',
                                title: '操作失败',
                                showClose: true,
                                message: '添加菜单失败，失败原因：' + resp.data.msg
                            });
                            console.error(resp);
                        } else {
                            if (errFunc && errFunc instanceof Function) {
                                errFunc();
                            }
                            this.$notify({
                                type: 'error',
                                title: '操作失败',
                                showClose: true,
                                message: '添加菜单失败!'
                            });
                            console.error(resp);
                        }
                    }).catch((err) => {
                        if (errFunc && errFunc instanceof Function) {
                            errFunc();
                        }
                        this.$notify({
                            type: 'error',
                            title: '操作失败',
                            showClose: true,
                            message: '添加菜单失败!'
                        });
                        console.error(err);
                    });
                }
            });
        },

        deleteMenu() {
            if (!this.currObj.findTreeParentNode()) {
                this.$message({
                    type: 'warning',
                    showClose: true,
                    message: '不允许删除根节点！'
                });
                return;
            }
            this.$confirm('确定删除结点及其下属结点?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var ids = [];
                this.doDeleteNode(this.currObj, ids);
                this.batchDelete(ids);
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '删除结点失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },

        doDeleteNode(obj, ids) {
            if (!obj) {
                return;
            }
            ids.push(obj.key);
            let temp = this.myDiagram.model.nodeDataArray.filter(function (node) {
                return node.key === obj.key;
            });
            let tempMap = obj.findTreeChildrenNodes();
            for (let key in tempMap) {
                if (tempMap[key] && tempMap[key].key) {
                    this.doDeleteNode(this.myDiagram.findNodeForKey(tempMap[key].key), ids);
                }
            }
            this.myDiagram.model.removeNodeData(temp[0]);
        },

        initGraph() {
            let $ = go.GraphObject.make;  // for conciseness in defining templates
            let cxElement = document.getElementById("contextMenu");
            this.myDiagram =
                $(go.Diagram, "menuTree",  // must be the ID or reference to div
                    {
                        "toolManager.hoverDelay": 100,  // 100 milliseconds instead of the default 850
                        allowCopy: true,
                        layout:  // create a TreeLayout for the family tree
                            $(go.TreeLayout,
                                {angle: 90, nodeSpacing: 10, layerSpacing: 40, layerStyle: go.TreeLayout.LayerUniform})
                    });

            // 结点提示信息内容转换器
            function tooltipTextConverter(menu) {
                let str = "";
                str += "url: " + (menu.url || '');
                if (menu.descr) {
                    str += "\n描述: " + menu.descr;
                }
                return str;
            }

            // 定义结点提示信息内容及样式
            let tooltiptemplate =
                $("ToolTip",
                    {"Border.fill": "whitesmoke", "Border.stroke": "black"},
                    $(go.TextBlock,
                        {
                            font: "bold 8pt Helvetica, bold Arial, sans-serif",
                            wrap: go.TextBlock.WrapFit,
                            margin: 5
                        },
                        new go.Binding("text", "", tooltipTextConverter))
                );

            // define Converters to be used for Bindings
            function validBrushConverter(valid) {
                return valid === 'valid' ? '#90CAF9' : '#a8b3bf';
            }

            let myContextMenu = $(go.HTMLInfo, {
                show: showContextMenu,
                hide: hideContextMenu
            });

            this.myDiagram.contextMenu = myContextMenu;
            // We don't want the div acting as a context menu to have a (browser) context menu!
            cxElement.addEventListener("contextmenu", function (e) {
                e.preventDefault();
                return false;
            }, false);

            function hideCX() {
                if (vm.myDiagram.currentTool instanceof go.ContextMenuTool) {
                    vm.myDiagram.currentTool.doCancel();
                }
            }

            function showContextMenu(obj, diagram) {
                vm.currObj = obj;
                // Show only the relevant buttons given the current state.
                let cmd = diagram.commandHandler;
                let hasMenuItem = false;

                function maybeShowItem(elt, pred) {
                    if (pred) {
                        elt.style.display = "block";
                        hasMenuItem = true;
                    } else {
                        elt.style.display = "none";
                    }
                }

                maybeShowItem(document.getElementById("addMenu"), cmd.canCutSelection());
                maybeShowItem(document.getElementById("deleteMenu"), cmd.canCutSelection());
                // Now show the whole context menu element
                if (hasMenuItem) {
                    cxElement.classList.add("show-menu");
                    // we don't bother overriding positionContextMenu, we just do it here:
                    let mousePt = diagram.lastInput.viewPoint;
                    cxElement.style.left = mousePt.x + 5 + "px";
                    cxElement.style.top = mousePt.y + "px";
                }
                // Optional: Use a `window` click listener with event capture to
                //           remove the context menu if the user clicks elsewhere on the page
                window.addEventListener("click", hideCX, true);
            }

            function hideContextMenu() {
                cxElement.classList.remove("show-menu");
                // Optional: Use a `window` click listener with event capture to
                //           remove the context menu if the user clicks elsewhere on the page
                window.removeEventListener("click", hideCX, true);
            }

            // replace the default Node template in the nodeTemplateMap
            this.myDiagram.nodeTemplate =
                $(go.Node, "Auto",
                    new go.Binding("location", "loc"),
                    {contextMenu: myContextMenu},
                    {deletable: false, toolTip: tooltiptemplate},
                    new go.Binding("text", "name"),
                    $(go.Shape, "Rectangle",
                        {
                            fill: "lightgray",
                            stroke: null, strokeWidth: 0,
                            stretch: go.GraphObject.Fill,
                            alignment: go.Spot.Center
                        },
                        new go.Binding("fill", "valid", validBrushConverter)),
                    $(go.TextBlock,
                        {
                            font: "700 12px Droid Serif, sans-serif",
                            textAlign: "center",
                            margin: 10, maxSize: new go.Size(80, NaN)
                        },
                        new go.Binding("text", "label")) // 结点中的文本
                );

            // define the Link template
            this.myDiagram.linkTemplate =
                $(go.Link,  // the whole link panel
                    {routing: go.Link.Orthogonal, corner: 5, selectable: false},
                    $(go.Shape, {strokeWidth: 3, stroke: '#424242'}));  // the gray link shape

            this.myDiagram.addDiagramListener("ObjectDoubleClicked", (e)=> {
                this.currNodeData = e.subject.findTemplateBinder().data;
                this.form.key = this.currNodeData.key;
                this.form.name = this.currNodeData.name;
                this.form.label = this.currNodeData.label;
                this.form.descr = this.currNodeData.descr;
                this.form.sort = this.currNodeData.sort;
                this.form.url = this.currNodeData.url;
                this.form.parent = this.currNodeData.parent;
                this.form.valid = this.currNodeData.valid;
                this.saveMenuDialog = true;
            });
        },
        getMenuTree(params, func, errFunc) {
            axios.get(_contextPath + '/menu/list').then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    if (func && func instanceof Function) {
                        func();
                    }
                    let temp = [];
                    resp.data.data.forEach(function (item) {
                        if (item.parentId) {
                            temp.push({
                                key: item.id,
                                name: item.name,
                                parent: item.parentId,
                                label: item.label,
                                sort:item.sort,
                                url:item.url,
                                valid: item.valid
                            });
                        } else {
                            temp.push({
                                key: item.id,
                                name: item.name,
                                label: item.label,
                                sort:item.sort,
                                url:item.url,
                                valid: item.valid
                            });
                        }
                    });
                    this.myDiagram.model = new go.TreeModel(temp);
                } else if (resp && resp.data && resp.data.msg) {
                    if (errFunc && errFunc instanceof Function) {
                        errFunc();
                    }
                    this.$({
                        type: 'error',
                        showClose: true,
                        message: '查询菜单树失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    if (errFunc && errFunc instanceof Function) {
                        errFunc();
                    }
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询菜单树失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                if (errFunc && errFunc instanceof Function) {
                    errFunc();
                }
                this.$message({
                    type: 'error',
                    showClose: true,
                    message: '查询菜单树失败!'
                });
                console.error(err);
            });
        },
        zoomToFit() {
            this.myDiagram.commandHandler.zoomToFit();
        },
        centerOnRoot() {
            this.myDiagram.scale = 1;
            this.myDiagram.scrollToRect(this.myDiagram.findNodeForKey(0).actualBounds);
        }
    },
    mounted() {
        this.initGraph();
        this.getMenuTree();
    },
    created() {
        addLayoutListen((width, height) => {
            this.graphHeight = height;
            this.dialogHeight = window.innerHeight*0.7-120;
        });
    }
});
