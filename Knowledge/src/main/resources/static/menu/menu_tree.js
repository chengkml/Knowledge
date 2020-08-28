var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            currObj: null,
            graphHeight: 400,
            myDiagram: null
        }
    },
    methods: {

        addMenu: function () {
            var _self = this;
            this.myDiagram.model.addNodeData({
                key: 1,
                name: '',
                parent: _self.currObj.key,
                label: '',
                valid: 'invalid'
            });
        },

        deleteMenu: function () {
            var _self = this;
            if (!_self.currObj.findTreeParentNode()) {
                _self.$message({
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
            }).then(function () {
                _self.doDeleteNode(_self.currObj);
            }).catch(function (err) {
                if (err !== 'cancel') {
                    _self.$message({
                        type: 'error',
                        message: '删除结点失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },

        doDeleteNode: function (obj) {
            var _self = this;
            if (!obj) {
                return;
            }
            var temp = _self.myDiagram.model.nodeDataArray.filter(function (node) {
                return node.key === obj.key;
            });
            var tempMap = obj.findTreeChildrenNodes();
            for (var key in tempMap) {
                if (tempMap[key] && tempMap[key].key) {
                    _self.doDeleteNode(_self.myDiagram.findNodeForKey(tempMap[key].key));
                }
            }
            _self.myDiagram.model.removeNodeData(temp[0]);
        },

        initGraph: function () {
            var _self = this;
            var $ = go.GraphObject.make;  // for conciseness in defining templates
            var cxElement = document.getElementById("contextMenu");
            _self.myDiagram =
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
                var str = "";
                str += "url: " + (menu.url || '');
                if (menu.descr) {
                    str += "\n描述: " + menu.descr;
                }
                return str;
            }

            // 定义结点提示信息内容及样式
            var tooltiptemplate =
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

            var myContextMenu = $(go.HTMLInfo, {
                show: showContextMenu,
                hide: hideContextMenu
            });

            _self.myDiagram.contextMenu = myContextMenu;
            // We don't want the div acting as a context menu to have a (browser) context menu!
            cxElement.addEventListener("contextmenu", function (e) {
                e.preventDefault();
                return false;
            }, false);

            function hideCX() {
                if (_self.myDiagram.currentTool instanceof go.ContextMenuTool) {
                    _self.myDiagram.currentTool.doCancel();
                }
            }

            function showContextMenu(obj, diagram, tool) {
                _self.currObj = obj;
                // Show only the relevant buttons given the current state.
                var cmd = diagram.commandHandler;
                var hasMenuItem = false;

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
                    var mousePt = diagram.lastInput.viewPoint;
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
            _self.myDiagram.nodeTemplate =
                $(go.Node, "Auto",
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
            _self.myDiagram.linkTemplate =
                $(go.Link,  // the whole link panel
                    {routing: go.Link.Orthogonal, corner: 5, selectable: false},
                    $(go.Shape, {strokeWidth: 3, stroke: '#424242'}));  // the gray link shape
        },
        getMenuTree: function (params, func, errFunc) {
            var _self = this;
            axios.get(_contextPath + '/menu/list').then(function (resp) {
                if (resp && resp.data && resp.data.success) {
                    if (func && func instanceof Function) {
                        func();
                    }
                    var temp = [];
                    resp.data.data.forEach(function (item) {
                        if (item.parentId) {
                            temp.push({
                                key: item.id,
                                name: item.name,
                                parent: item.parentId,
                                label: item.label,
                                valid: item.valid
                            });
                        } else {
                            temp.push({
                                key: item.id,
                                name: item.name,
                                label: item.label,
                                valid: item.valid
                            });
                        }
                    });
                    _self.myDiagram.model = new go.TreeModel(temp);
                } else if (resp && resp.data && resp.data.msg) {
                    if (errFunc && errFunc instanceof Function) {
                        errFunc();
                    }
                    _self.$({
                        type: 'error',
                        showClose: true,
                        message: '查询菜单树失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    if (errFunc && errFunc instanceof Function) {
                        errFunc();
                    }
                    _self.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询菜单树失败!'
                    });
                    console.error(resp);
                }
            }).catch(function (err) {
                if (errFunc && errFunc instanceof Function) {
                    errFunc();
                }
                _self.$message({
                    type: 'error',
                    showClose: true,
                    message: '查询菜单树失败!'
                });
                console.error(err);
            });
        },
        zoomToFit: function () {
            this.myDiagram.commandHandler.zoomToFit();
        },
        centerOnRoot: function () {
            this.myDiagram.scale = 1;
            this.myDiagram.scrollToRect(this.myDiagram.findNodeForKey(0).actualBounds);
        }
    },
    mounted: function () {
        var _self = this;
        _self.initGraph();
        _self.getMenuTree();
    },
    created: function () {
        var _self = this;
        addLayoutListen(function (width, height) {
            _self.graphHeight = height;
        });
    }
})
