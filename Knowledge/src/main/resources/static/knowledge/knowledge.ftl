<!DOCTYPE html>
<html>
<head>
    <title>知识管理</title>
    <#include "../component/__common.ftl"/>
    <script type="text/javascript" src="../lib/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="lib/echarts/dist/echarts.js"></script>
    <link type="text/css" rel="stylesheet" href="../knowledge/knowledge.css"/>
</head>
<body style="margin:0px;">
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-aside width="200px" id="aside" style="overflow: auto;">
            <el-input style="margin:10px 0 10px 10px;width:calc(100% - 10px)"
                      placeholder="输入关键字进行过滤"
                      v-model="filterText">
            </el-input>
            <el-tree id="tree"
                     :style="'border-bottom-width: 0px!;important;overflow: auto;height:'+filterTreeHeight+'px'"
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
                    <el-button type="primary" plain @click="search">搜 索</el-button>
                    <el-button type="success" plain @click="toAdd">新 增</el-button>
                    <el-button type="success" plain @click="toExercise">练 习</el-button>
                </el-row>
            </el-header>
            <el-main style="padding-bottom:5px;">
                <el-table
                        @row-contextmenu="tabRightClick"
                        :data="knowledgeData"
                        stripe border
                        :height="tabHeight"
                        style="width: 100%">
                    <el-table-column type="index" label="序号" width="60" align="center" header-align="center">
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
                </el-table>
            </el-main>
            <el-footer style="text-align:center;height:40px;">
                <el-pagination v-if="knowledgeTotal>filter.pageSize"
                               background
                               :current-page.sync="filter.pageNum"
                               :page-size="filter.pageSize"
                               @current-change="changeCurrPage"
                               @size-change="changePageSize"
                               layout="prev, pager, next,jumper,sizes,total"
                               :total="knowledgeTotal">
                </el-pagination>
            </el-footer>
        </el-container>
    </el-container>
    <el-dialog title="知识点新增" width="60%" :visible.sync="knowledgeDialog" top="5vh" :close-on-click-modal="false">
        <el-tabs v-model="currTab" type="card" closable @tab-remove="removeTab">
            <el-tab-pane name="main" label="知识点详情" :closable="false">
                <el-form :model="knowledgeForm" :rules="rules" ref="knowledgeForm">
                    <el-form-item label="类目:" :label-width="formLabelWidth" prop="category">
                        <el-cascader
                                placeholder="请选择所属类目"
                                :options="categoryCascaderTree"
                                :props="{ value:'id'}"
                                filterable
                                v-model="knowledgeForm.category"
                                :filter-method="filterCascade"
                                clearable></el-cascader>
                    </el-form-item>
                    <el-form-item label="名称:" :label-width="formLabelWidth" prop="name">
                        <el-input v-model="knowledgeForm.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="描述:" :label-width="formLabelWidth" prop="descr">
                        <el-input v-model="knowledgeForm.descr" type="textarea" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="详情:" :label-width="formLabelWidth">
                        <textarea class="ckeditor" id="ckeditor" name="ckeditor"></textarea>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
            <el-tab-pane
                    v-for="(item, index) in knowledgeTabs"
                    :key="item.name"
                    :label="item.title"
                    :name="item.name">
                <div style="height:447px;">
                    <el-form :model="questionForms[index]" :rules="questionRules" :ref="'question'+index"
                             style="max-height:400px;overflow:auto;">
                        <el-form-item label="题型:" :label-width="formLabelWidth" prop="type">
                            <el-select v-model="questionForms[index].type" placeholder="请选择题型" prop="type">
                                <el-option label="判断题" value="judge"></el-option>
                                <el-option label="单选题" value="select"></el-option>
                                <el-option label="多选题" value="multiSelect"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="题干:" :label-width="formLabelWidth" prop="stem">
                            <el-input type="textarea" v-model="questionForms[index].stem"></el-input>
                        </el-form-item>
                        <el-form-item v-if="questionForms[index].type==='judge'" label="答案:"
                                      :label-width="formLabelWidth" prop="result">
                            <el-radio v-model="questionForms[index].result" label="RIGHT">正确</el-radio>
                            <el-radio v-model="questionForms[index].result" label="FALSE">错误</el-radio>
                        </el-form-item>
                        <template v-else-if="questionForms[index].type==='select'">
                            <el-form-item label="选项A:" :label-width="formLabelWidth" prop="optionA">
                                <el-input v-model="questionForms[index].optionA" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项B:" :label-width="formLabelWidth" prop="optionB">
                                <el-input v-model="questionForms[index].optionB" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项C:" :label-width="formLabelWidth" prop="optionC">
                                <el-input v-model="questionForms[index].optionC" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项D:" :label-width="formLabelWidth" prop="optionD">
                                <el-input v-model="questionForms[index].optionD" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="答案:" :label-width="formLabelWidth" prop="result">
                                <el-radio v-model="questionForms[index].result" label="A">A.
                                    {{questionForms[index].optionA}}
                                </el-radio>
                                <el-radio v-model="questionForms[index].result" label="B">B.
                                    {{questionForms[index].optionB}}
                                </el-radio>
                                <el-radio v-model="questionForms[index].result" label="C">C.
                                    {{questionForms[index].optionC}}
                                </el-radio>
                                <el-radio v-model="questionForms[index].result" label="D">D.
                                    {{questionForms[index].optionD}}
                                </el-radio>
                            </el-form-item>
                        </template>
                        <template v-else-if="questionForms[index].type==='multiSelect'">
                            <el-form-item label="选项A:" :label-width="formLabelWidth" prop="optionA">
                                <el-input v-model="questionForms[index].optionA" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项B:" :label-width="formLabelWidth" prop="optionB">
                                <el-input v-model="questionForms[index].optionB" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项C:" :label-width="formLabelWidth" prop="optionC">
                                <el-input v-model="questionForms[index].optionC" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="选项D:" :label-width="formLabelWidth" prop="optionD">
                                <el-input v-model="questionForms[index].optionD" placeholder="请输入内容"></el-input>
                            </el-form-item>
                            <el-form-item label="答案:" :label-width="formLabelWidth" prop="resultArr">
                                <el-checkbox-group v-model="questionForms[index].resultArr">
                                    <el-checkbox label="A">A.
                                        {{questionForms[index].optionA}}
                                    </el-checkbox>
                                    <el-checkbox label="B">B.
                                        {{questionForms[index].optionB}}
                                    </el-checkbox>
                                    <el-checkbox label="C">C.
                                        {{questionForms[index].optionC}}
                                    </el-checkbox>
                                    <el-checkbox label="D">D.
                                        {{questionForms[index].optionD}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </template>
                    </el-form>
                </div>
            </el-tab-pane>
        </el-tabs>
        <div slot="footer" class="dialog-footer">
            <el-button @click="knowledgeDialog = false">取 消</el-button>
            <el-button type="primary" @click="addQuestionTab">出 题</el-button>
            <el-button type="primary" @click="save" style="margin-right:90px;">保 存</el-button>
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
    <div v-if="showRightMenu"
         :style="{display:rightMenu.display,left:rightMenu.left,top:rightMenu.top,position:'absolute'}">
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
    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute'}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div v-if="showDetailButton" style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="viewDetail">查看</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="editKnowledge">编辑</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="deleteKnowledge">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="../knowledge/knowledge.js"></script>
</body>
</html>
