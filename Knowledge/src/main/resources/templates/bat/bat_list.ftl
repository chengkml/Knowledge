<!DOCTYPE html>
<html>
<head>
    <title>TODO列表</title>
    <#include "../component/__common.ftl"/>
    <script src="${contextPath}/lib/echarts/dist/echarts.js"></script>
    <link rel="stylesheet" href="${contextPath}/bat/bat_list.css">
</head>
<body style="margin:0px;">
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-container>
            <el-header style="height:40px;">
                <el-row :gutter="10" style="margin-top:10px;">
                    <el-col :span="5">
                        <el-input
                                placeholder="请输入内容"
                                @change="list"
                                v-model="filter.keyWord">
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                        </el-input>
                    </el-col>
                    <el-button type="primary" plain @click="list">搜 索</el-button>
                    <el-button type="success" plain @click="toAdd">新 增</el-button>
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
                            label="英文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="label"
                            label="中文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="createDate"
                            width="180"
                            label="创建时间"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column type="expand" label="参数">

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
    <el-dialog title="新增Bat" width="40%" :visible.sync="todoDialog" top="10vh" :close-on-click-modal="false">
        <el-row>
            <el-col :span="23">
                <el-form :model="batForm" :rules="rules" ref="batForm" :label-width="formLabelWidth">
                    <el-form-item label="英文名:" prop="name">
                        <el-input v-model="batForm.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="中文名:">
                        <el-input v-model="batForm.label" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-row>
                <el-col :span="23">
                    <el-button @click="todoDialog = false">取 消</el-button>
                    <el-button type="primary" @click="save">保 存</el-button>
                </el-col>
            </el-row>
        </div>
    </el-dialog>
    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute',zIndex:999}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="editKnowledge">编辑</span>
            </div>
            <div style="padding:5px 0px 3px 0px;">
                <span class="menu-button" @click="confirmDeleteItem">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="${contextPath}/bat/bat_list.js"></script>
</body>
</html>
