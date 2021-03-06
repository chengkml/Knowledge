<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bat表</title>
    <#include "../component/__common.ftl"/>
    <script src="../lib/echarts/dist/echarts.js"></script>
    <link rel="stylesheet" href="../bat/bat_list.css">
    <link rel="stylesheet" href="../lib/codemirror-5.56.0/lib/codemirror.css"/>
    <script src="../lib/codemirror-5.56.0/lib/codemirror.js"></script>
    <script src="../lib/codemirror-5.56.0/mode/javascript/javascript.js"></script>
    <link rel="stylesheet" href="../lib/codemirror-5.56.0/theme/eclipse.css"/>
    <#include "../component/__upload.ftl"/>
</head>
<body style="margin:0;">
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-container>
            <el-header>
                <el-row :gutter="10">
                    <el-col :span="5">
                        <el-input
                                placeholder="请输入内容"
                                @change="list"
                                v-model="filter.keyWord" size="small">
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                        </el-input>
                    </el-col>
                    <el-button type="primary" @click="list" size="small" style="margin-left:10px;">搜 索</el-button>
                    <el-button type="success" @click="toAdd" size="small">新 增</el-button>
                    <el-badge :value="runningProcessNum" class="item" style="float:right;margin-right:10px;"v-if="runningProcessNum>0">
                        <el-button icon="el-icon-s-unfold" type="primary" circle size="small" @click="listProcess"></el-button>
                    </el-badge>
                </el-row>
            </el-header>
            <el-main>
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
                            label="脚本英文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="label"
                            label="脚本中文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            width="120"
                            prop="params"
                            label="参数"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="batName"
                            label="脚本文件名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                        <template slot-scope="scope">
                            <span>{{scope.row.bat?scope.row.bat.name:''}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="createDate"
                            width="180"
                            label="创建时间"
                            header-align="center"
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
        <el-row :style="{height:dialogHeight+'px',overflow:'auto'}">
            <el-col :span="23">
                <el-form :model="batForm" :rules="rules" ref="batForm" :label-width="formLabelWidth">
                    <el-form-item label="英文名:" prop="name">
                        <el-input v-model="batForm.name" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="中文名:" prop="label">
                        <el-input v-model="batForm.label" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="参数">
                        <el-input v-model="batForm.params" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="bat文件:">
                        <ck-upload ref="upload" file-tip="文件大小请不要超过10M！" size="small"></ck-upload>
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
    <el-dialog title="执行日志" width="80%" :visible.sync="exeLogDialog" top="10vh" :close-on-click-modal="false">
        <textarea ref="codemirror" v-model="exeLog"></textarea>
    </el-dialog>
    <el-dialog title="运行中的脚本" width="80%" :visible.sync="processDialog" top="10vh" :close-on-click-modal="false">
        <el-table
                :data="runningProcess"
                stripe border
                :height="processTabHeight"
                style="width: 100%">
            <el-table-column type="index" label="序号" width="60" align="center" header-align="center">
            </el-table-column>
            <el-table-column
                    prop="id"
                    label="唯一标识"
                    header-align="center"
                    show-overflow-tooltip
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="脚本名"
                    header-align="center"
                    show-overflow-tooltip
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="label"
                    label="脚本中文名"
                    header-align="center"
                    show-overflow-tooltip
                    align="center">
            </el-table-column>
            <el-table-column prop="startTime"
                             label="启动时间"
                             header-align="center"
                             show-overflow-tooltip
                             align="center">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="120"
                    align="center"
                    header-align="center">
                <template slot-scope="scope">
                    <el-button
                            @click.native.prevent="viewLog(scope.row)"
                            type="text"
                            size="small">
                        执行日志
                    </el-button>
                    <el-button
                            @click.native.prevent="stopBat(scope.row)"
                            type="text"
                            size="small">
                        终止
                    </el-button>
                </template>
            </el-table-column>
    </el-dialog>
    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute',zIndex:999}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="startBat">执行</span>
            </div>
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="editBat">编辑</span>
            </div>
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="confirmDeleteItem">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="../bat/bat_list.js"></script>
</body>
</html>
