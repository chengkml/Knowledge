<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
    <title>日志管理</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <base href="${pageContext.request.contextPath}/"/>
    <script src="lib/vue/dist/vue.js"></script>
    <link rel="stylesheet" href="lib/element-ui/lib/theme-chalk/index.css">
    <link rel="stylesheet" href="css/journal.css?time=<%= new Date().getTime() %>">
    <script src="lib/element-ui/lib/index.js"></script>
    <script src="lib/axios/dist/axios.js"></script>
    <script type="text/javascript" src="lib/ckeditor/ckeditor.js"></script>
</head>
<body style="margin:0px;">
<div id="main">
    <el-container>
        <el-aside width="150px" style="padding:10px;">
            <el-select v-model="currentYear" placeholder="请选择年份" @change="handleYearChange">
                <el-option
                        v-for="item in yearOptions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-tree ref="tree" :data="categoryTree" :props="defaultProps" @node-click="handleClickCategory" node-key="id" :expand-on-click-node="false" default-expand-all></el-tree>
        </el-aside>
        <el-container>
            <el-header style="margin-top:10px;">
                <el-row :gutter="10">
                    <el-col :span="4">
                        <el-input v-model="filter.keyWord" placeholder="请输入关键字" clearable @change="search"></el-input>
                    </el-col>
                    <el-col :span="20">
                        <el-date-picker
                                v-model="filter.dateRange"
                                type="daterange"
                                @change="search"
                                placeholder="选择日期范围">
                        </el-date-picker>
                        <el-button type="primary" plain @click="search">搜索</el-button>
                        <el-button type="success" plain @click="newJournal">新增</el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-main style="padding-top:0px;">
                <el-table
                        :data="journals"
                        stripe border
                        style="width: 100%">
                    <el-table-column
                            prop="createDate"
                            label="创建时间"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            prop="title"
                            label="标题"
                            width="180">
                    </el-table-column>
                    <el-table-column
                            prop="summary"
                            label="摘要">
                    </el-table-column>
                    <el-table-column label="操作" width="200">
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    type="success"
                                    @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                            <el-button
                                    size="mini"
                                    type="primary"
                                    @click="handleView(scope.$index, scope.row)">查看</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer v-if="page.total>page.pageSize">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page.sync="page.pageNum"
                        :page-size="page.pageSize"
                        layout="total, prev, pager, next"
                        :total="page.total">
                </el-pagination>
            </el-footer>
        </el-container>
    </el-container>
    <el-dialog title="新增日志" width="80%" :visible.sync="journalDialog" top="5vh">
        <div>
            <el-row>
                <el-col :span="20" :offset="2">
                    <el-form :model="journalForm">
                        <el-form-item label="标题:" :label-width="formLabelWidth">
                            <el-input v-model="journalForm.title" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="内容:" :label-width="formLabelWidth">
                            <textarea class="ckeditor" id="ckeditor" name="ckeditor"></textarea>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="journalDialog = false">取 消</el-button>
            <el-button type="primary" @click="save" style="margin-right:90px;">保存</el-button>
        </div>
    </el-dialog>
    <el-dialog title="日志详情" width="80%" :visible.sync="contentDialog" top="5vh">
        <div v-html="currDetail" style="height:400px;overflow:auto;">
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="contentDialog = false">确 定</el-button>
        </div>
    </el-dialog>
</div>
<script>
    var _contextPath = "${pageContext.request.contextPath}";
</script>
<script src="js/journal.js?time=<%= new Date().getTime() %>"></script>
</body>
</html>