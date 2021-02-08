<!DOCTYPE html>
<html>
<head>
    <title>TODO列表</title>
    <#include "../component/__common.ftl"/>
    <script type="text/javascript" src="../lib/ckeditor/ckeditor.js"></script>
    <link rel="stylesheet" href="../todo/todo_list.css">
</head>
<body style="margin:0px;">
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-aside width="200px" id="aside" style="overflow: auto;">
            <el-input style="margin:10px 0 10px 10px;width:calc(100% - 10px)" size="small" clearable
                      placeholder="输入关键字进行过滤"
                      v-model="filterText">
            </el-input>
            <el-tree id="tree" clearable
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
            <el-header>
                <el-select v-model="filter.states" multiple placeholder="请选择项目状态" @change="list" size="small" clearable style="width:240px;">
                    <el-option
                            v-for="item in stateOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <el-input size="small" style="width:180px;"
                          placeholder="请输入内容"
                          @change="list"
                          v-model="filter.keyWord" @keyup.enter.native="list" clearable>
                </el-input>
                <el-button type="primary" @click="list" size="small" style="margin-left:10px;">搜 索</el-button>
                <el-button type="success" @click="toAdd" size="small">新 增</el-button>
            </el-header>
            <el-main>
                <el-table
                        @row-contextmenu="tabRightClick"
                        :data="knowledgeData"
                        stripe border
                        :height="tabHeight"
                        @row-dblclick="editAnalysis"
                        style="width: 100%">
                    <el-table-column type="index" label="序号" width="60" align="center" header-align="center">
                    </el-table-column>
                    <el-table-column
                            label="分类"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                        <template slot-scope="scope">
                            <el-tag type="warning" size="small">{{groupMap[scope.row.groupId]}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="内容"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="estimateStartTime"
                            width="180"
                            label="预计开始时间"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="estimateEndTime"
                            width="180"
                            label="预计完成时间"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="leadTime"
                            width="180"
                            label="交付时间"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            width="110"
                            label="剩余时间"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                        <template slot-scope="scope">
                            <template v-if="scope.row.state==='finish'">
                            </template>
                            <template v-else-if="scope.row.leftTime<-(1000*60*60*24)">
                                <el-tag size="small" type="danger">{{'超期'+parseInt(-scope.row.leftTime/(1000*60*60*24))+'天'}}</el-tag>
                            </template>
                            <template v-else-if="scope.row.leftTime<0">
                                <el-tag size="small" type="danger">{{'超期'+parseInt(-scope.row.leftTime/(1000*60*60))+'小时'}}</el-tag>
                            </template>
                            <template v-else-if="scope.row.leftTime>(1000*60*60*24*3)">
                                <el-tag size="small" type="success">{{parseInt(scope.row.leftTime/(1000*60*60*24))+'天'}}</el-tag>
                            </template>
                            <template v-else-if="scope.row.leftTime>(1000*60*60*24)">
                                <el-tag size="small">{{parseInt(scope.row.leftTime/(1000*60*60*24))+'天'}}</el-tag>
                            </template>
                            <template v-else>
                                <el-tag size="small" type="warning">{{parseInt(scope.row.leftTime/(1000*60*60))+'小时'}}</el-tag>
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column
                            width="110"
                            label="状态"
                            header-align="center"
                            show-overflow-tooltip
                            align="center" :formatter="function(row){return stateMap[row.state]||row.state}">
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer>
                <el-pagination
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
    <el-dialog title="新增ToDo" width="40%" :visible.sync="todoDialog" top="10vh" :close-on-click-modal="false">
        <el-row>
            <el-col :span="23">
                <el-form :model="todoForm" :rules="rules" ref="todoForm" :label-width="formLabelWidth">
                    <el-form-item label="类目:" prop="groupId">
                        <el-cascader style="width:100%;"
                                placeholder="请选择所属类目"
                                :options="categoryCascaderTree"
                                :props="{ value:'id',label:'descr',expandTrigger: 'hover'}"
                                filterable
                                v-model="todoForm.groupId"
                                :filter-method="filterCascade"
                                clearable></el-cascader>
                    </el-form-item>
                    <el-form-item label="名称:" prop="name">
                        <el-input v-model="todoForm.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="计划开始时间:" prop="estimateStartTime">
                        <el-date-picker style="width:100%;"
                                        v-model="todoForm.estimateStartTime"
                                        type="datetime"
                                        :picker-options="pickerOptions"
                                        placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="计划结束时间:" prop="estimateEndTime">
                        <el-date-picker style="width:100%;"
                                        v-model="todoForm.estimateEndTime"
                                        type="datetime"
                                        :picker-options="pickerOptions"
                                        placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="交付时间:" prop="leadTime">
                        <el-date-picker style="width:100%;"
                                        v-model="todoForm.leadTime"
                                        type="datetime"
                                        :picker-options="pickerOptions"
                                        placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-row>
                <el-col :span="23">
                    <el-button @click="todoDialog = false" size="small">取 消</el-button>
                    <el-button type="primary" @click="save" size="small">保 存</el-button>
                </el-col>
            </el-row>
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
                    <el-form-item label="分支:" :label-width="formLabelWidth">
                        <el-input v-model="categoryForm.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="名称:" :label-width="formLabelWidth">
                        <el-input v-model="categoryForm.descr" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-button @click="categoryDialog = false">取 消</el-button>
            <el-button type="primary" @click="addGroup" style="margin-right:50px;">确 定</el-button>
        </div>
    </el-dialog>
        <el-dialog :title="currRow.name" :visible.sync="itemAnalysisDialog" width="80%" :close-on-press-escape="false" :close-on-click-modal="false" @close="closeAnalysisDialog">
        <textarea class="ckeditor" id="ckeditor" name="ckeditor"></textarea>
        <div slot="footer" class="dialog-footer">
            <el-button @click="itemAnalysisDialog = false" size="small">取 消</el-button>
            <el-button type="primary" @click="saveAnalysis" size="small">保 存</el-button>
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
            <div style="padding:3px 0px 5px 0px;">
                <span class="menu-button" @click="confirmDeleteGroup">删除</span>
            </div>
        </div>
    </div>
    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute',zIndex:999}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="finishItem">完成</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="editKnowledge">编辑</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="pushItemMail">推送</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="confirmDeleteItem">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="../todo/todo_list.js"></script>
</body>
</html>
