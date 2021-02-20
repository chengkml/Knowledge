<!DOCTYPE html>
<html lang="en">
<head>
    <title>定时任务监控</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="../job/job_list.css">
</head>
<body>
<div id="main" @click="clickPage" v-cloak>
    <el-container>
        <el-container>
            <el-header style="height:40px;">
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
                            label="任务英文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            prop="label"
                            label="任务中文名"
                            header-align="center"
                            show-overflow-tooltip
                            align="left">
                    </el-table-column>
                    <el-table-column
                            width="100"
                            label="任务类型"
                            header-align="center"
                            show-overflow-tooltip
                            :formatter="formatJobType"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="cron"
                            width="120"
                            label="定时配置"
                            header-align="center"
                            show-overflow-tooltip
                            align="center">
                    </el-table-column>
                    <el-table-column
                            width="360"
                            prop="jobClass"
                            label="执行类"
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
    <el-dialog :title="saveTitle" width="40%" :visible.sync="jobDialog" :close-on-click-modal="false">
        <el-row :style="{height:dialogHeight+'px',overflow:'auto'}">
            <el-col :span="23">
                <el-form :model="form" :rules="rules" ref="form" :label-width="formLabelWidth">
                    <el-form-item label="任务英文名:" prop="name">
                        <el-input v-model="form.name" :disabled="!!form.id" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="任务中文名:" prop="label">
                        <el-input v-model="form.label" :disabled="!!form.id" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="任务类型" prop="type">
                        <el-select v-model="form.type" style="width:100%;" size="small">
                            <el-option v-for="item in jobTypeOptions" :label="item.label" :value="item.value" :key="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="定时配置" prop="cron">
                        <el-input v-model="form.cron" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="执行类" prop="jobClass">
                        <el-select v-model="form.jobClass" style="width:100%;" size="small">
                            <el-option v-for="item in jobClassOptions" :label="item.label+'('+item.value+')'" :value="item.value" :key="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
            <el-row>
                <el-col :span="23">
                    <el-button @click="jobDialog = false" size="small">取 消</el-button>
                    <el-button type="primary" @click="save" size="small">保 存</el-button>
                </el-col>
            </el-row>
        </div>
    </el-dialog>

    <div v-if="showTabRightMenu"
         :style="{display:tabRightMenu.display,left:tabRightMenu.left,top:tabRightMenu.top,position:'absolute',zIndex:999}">
        <div style="border:solid 1px #c7c4c4;background-color:#ffffff;">
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="fireJob">执行</span>
            </div>
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="editRow">编辑</span>
            </div>
            <div style="padding:5px 0 3px 0;">
                <span class="menu-button" @click="confirmDeleteItem">删除</span>
            </div>
        </div>
    </div>
</div>
<script src="../job/job_list.js"></script>
</body>
</html>
