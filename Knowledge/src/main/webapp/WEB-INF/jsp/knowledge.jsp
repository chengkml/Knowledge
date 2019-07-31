<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
    <title>知识管理</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <base href="${pageContext.request.contextPath}/"/>
    <script src="lib/vue/dist/vue.js"></script>
    <link rel="stylesheet" href="lib/element-ui/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="css/knowledge.css?time=<%= new Date().getTime() %>">
    <script src="lib/element-ui/lib/index.js"></script>
    <script src="lib/axios/dist/axios.js"></script>
    <script type="text/javascript" src="lib/ckeditor/ckeditor.js"></script>
    <script src="lib/echarts/dist/echarts.js?v=${date}"></script>
</head>
<body style="margin:0px;">
    <div id="main" @click="clickPage">
        <el-container>
            <el-aside width="200px" id="aside" style="overflow: auto;">
                <el-input style="margin:10px 0 10px 10px;width:calc(100% - 10px)"
                        placeholder="输入关键字进行过滤"
                        v-model="filterText">
                </el-input>
                <el-tree id="tree"
                        class="filter-tree"
                        :data="categoryTree"
                        :props="treeProps"
                        default-expand-all
                        :filter-node-method="filterNode"
                         :expand-on-click-node="false"
                        ref="tree"
                        highlight-current
                        @node-contextmenu="treeRightClick"
                        @node-click="clickTreeNode">
                </el-tree>
            </el-aside>
            <el-container>
                <el-header style="height:40px;">
                    <el-row :gutter="10" style="margin-top:10px;">
                        <el-col :span="5">
                            <el-input
                                    placeholder="请输入内容"
                                    @change="search"
                                    v-model="filter.keyWord">
                                <i slot="prefix" class="el-input__icon el-icon-search"></i>
                            </el-input>
                        </el-col>
                        <el-button type="primary" plain @click="search">搜索</el-button>
                        <el-button type="success" plain @click="toAdd">新增</el-button>
                    </el-row>
                </el-header>
                <el-main style="padding-bottom:5px;">
                    <el-table
                            :data="knowledgeData"
                            stripe
                            :height="tabHeight"
                            style="width: 100%">
                        <el-table-column type="index" label="序号">
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="知识点"
                                header-align="center"
                                show-overflow-tooltip
                                align="center">
                        </el-table-column>
                        <el-table-column
                                prop="descr"
                                label="描述"
                                header-align="center"
                                show-overflow-tooltip
                                min-width="300"
                                align="center">
                        </el-table-column>
                        <el-table-column
                                prop="category"
                                label="分类"
                                header-align="center"
                                show-overflow-tooltip
                                align="center">
                            <template slot-scope="scope">
                                <el-tag type="warning">{{categoryMap.get(scope.row.category)}}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="createTime"
                                :formatter="formatCreateTime"
                                label="创建时间"
                                header-align="center"
                                show-overflow-tooltip
                                align="center">
                        </el-table-column>
                        <el-table-column label="操作" width="150">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        type="danger"
                                        @click="deleteKnowledge(scope.$index, scope.row)">删除</el-button>
                                <el-button
                                        size="mini"
                                        type="success"
                                        :disabled="scope.row.detail===null||!scope.row.detail"
                                        @click="viewDetail(scope.$index, scope.row)">详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-main>
                <el-footer style="text-align:center;height:40px;">
                    <el-pagination v-if="knowledgeTotal>filter.pageSize"
                            background
                            :current-page.sync="filter.pageNum"
                            :page-size="filter.pageSize"
                            @current-change="changeCurrPage"
                            @size-change="changePageSize"
                            layout="prev, pager, next,jumper,sizes"
                            :total="knowledgeTotal">
                    </el-pagination>
                </el-footer>
            </el-container>
        </el-container>
        <el-dialog title="知识点新增" width="80%" :visible.sync="knowledgeDialog" top="5vh">
            <div>
                <el-row>
                    <el-col :span="20" :offset="2">
                        <el-form :model="knowledgeForm">
                            <el-form-item label="类目:" :label-width="formLabelWidth">
                                <el-cascader
                                        placeholder="请选择所属类目"
                                        :options="categoryCascaderTree"
                                        :props="{ value:'id'}"
                                        filterable
                                        v-model="knowledgeForm.category"
                                        :filter-method="filterCascade"
                                        clearable></el-cascader>
                            </el-form-item>
                            <el-form-item label="名称:" :label-width="formLabelWidth">
                                <el-input v-model="knowledgeForm.name" autocomplete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="描述:" :label-width="formLabelWidth">
                                <el-input v-model="knowledgeForm.descr" type="textarea" autocomplete="off"></el-input>
                            </el-form-item>
                            <el-form-item label="详情:" :label-width="formLabelWidth">
                                <textarea class="ckeditor" id="ckeditor" name="ckeditor"></textarea>
                            </el-form-item>
                        </el-form>
                    </el-col>
                </el-row>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="knowledgeDialog = false">取 消</el-button>
                <el-button type="primary" @click="save" style="margin-right:90px;">保存</el-button>
            </div>
        </el-dialog>
        <el-dialog title="知识点详情" width="80%" :visible.sync="detailDialog" top="5vh">
            <div v-html="currDetail" style="height:400px;overflow:auto;">
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="detailDialog = false">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :title="categoryOperTitle" :visible.sync="categoryDialog">
            <el-row>
                <el-col :span="20" :offset="2">
                    <el-form :model="categoryForm">
                        <el-form-item label="名称:" :label-width="formLabelWidth">
                            <el-input v-model="categoryForm.name" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="描述:" :label-width="formLabelWidth">
                            <el-input v-model="categoryForm.label" type="textarea" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button @click="categoryDialog = false">取 消</el-button>
                <el-button type="primary" @click="saveCategory" style="margin-right:50px;">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="知识图" :visible.sync="relaDialog" fullscreen>
            <div id="relaGraph" style="height:600px;">
            </div>
        </el-dialog>
        <div v-if="showRightMenu" :style="{display:rightMenu.display,left:rightMenu.left,top:rightMenu.top,position:'absolute'}">
            <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
                <div style="padding:5px 0px 3px 0px;">
                    <span class="menu-button" @click="appendCategory">新增</span>
                </div>
                <div style="padding:5px 0px 3px 0px;">
                    <span class="menu-button" @click="editCategory">编辑</span>
                </div>
                <div style="padding:5px 0px 3px 0px;">
                    <span v-if="showRelaButton" class="menu-button" @click="viewRela">图谱</span>
                </div>
                <div style="padding:3px 0px 5px 0px;">
                    <span class="menu-button" @click="removeCategory">删除</span>
                </div>
            </div>
        </div>
    </div>
    <script>
        var _contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="js/knowledge.js?time=<%= new Date().getTime() %>"></script>
</body>
</html>