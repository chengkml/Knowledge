<!DOCTYPE html>
<html lang="en">
<head>
    <title>文件管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="../res/res_list.css">
    <#include "../component/__upload.ftl"/>
</head>
<body>
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-container>
            <el-header>
                <el-row :gutter="10">
                    <el-col :span="2">
                        <el-select v-model="filter.valid" size="small" placeholder="文件状态" @change="list" clearable>
                            <el-option v-for="item in resValidOptions" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-col>
                    <el-col :span="5">
                        <el-input
                                placeholder="请输入内容"
                                @change="list"
                                v-model="filter.keyWord" size="small">
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                        </el-input>
                    </el-col>
                    <el-button type="primary" plain @click="list" size="small" style="margin-left:10px;">搜 索</el-button>
                    <el-button type="success" plain @click="toAdd" size="small">新 增</el-button>
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
                            width="200"
                            prop="name"
                            label="文件名"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            width="100"
                            label="文件大小"
                            header-align="center"
                            show-overflow-tooltip
                            :formatter="formatFileSize"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="mdCode"
                            width="300"
                            label="MD编码"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="path"
                            label="文件路径"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="resUrl"
                            label="文件URL"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            width="80"
                            prop="valid"
                            label="文件状态"
                            header-align="center"
                            :formatter="formatFileValid"
                            show-overflow-tooltip
                            align="center">
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
    <el-dialog :title="saveTitle" width="40%" :visible.sync="batDialog" top="10vh" :close-on-click-modal="false">
        <el-row>
            <el-col :span="23">
                <el-form :model="batForm" :rules="rules" ref="batForm" :label-width="formLabelWidth">
                    <el-form-item label="文件名:" prop="name">
                        <el-input v-model="batForm.name" size="small" placeholder="请输入文件名" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="文件状态:" prop="valid">
                        <el-select v-model="batForm.valid" size="small" clearable style="width:100%;" placeholder="请选择文件状态">
                            <el-option v-for="item in resValidOptions" :label="item.label" :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="资源文件:">
                        <ck-upload ref="upload" file-tip="文件大小请不要超过10M！"></ck-upload>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-row>
                <el-col :span="23">
                    <el-button @click="batDialog = false" size="small">取 消</el-button>
                    <el-button type="primary" @click="save" size="small">保 存</el-button>
                </el-col>
            </el-row>
        </div>
    </el-dialog>

    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute',zIndex:999}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="editBat">编辑</span>
            </div>
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="confirmDeleteItem">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="../res/res_list.js"></script>
</body>
</html>
