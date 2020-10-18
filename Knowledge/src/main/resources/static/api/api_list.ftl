<!DOCTYPE html>
<html>
<head>
    <title>api管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="../api/api_list.css">
    <link rel="stylesheet" href="../lib/codemirror-5.56.0/lib/codemirror.css"/>
    <script src="../lib/codemirror-5.56.0/lib/codemirror.js"></script>
    <script src="../lib/codemirror-5.56.0/mode/javascript/javascript.js"></script>
    <link rel="stylesheet" href="../lib/codemirror-5.56.0/theme/eclipse.css"/>
</head>
<body style="margin:0px;">
<div id="main" v-cloak>
    <el-row>
        <el-col :span="6">
            <div style="padding:0 0 10px 10px;background-color:#f7f7f7;">
                <el-tabs type="card">
                    <el-tab-pane label="输入Api">
                        <el-row style="height:100%;">
                            <el-col :span="23">
                                <el-form ref="inputForm" :model="inputForm" :label-width="formLabelWidth" :rules="inputFormRules" :style="{overflow:'hidden',height:formHeight+'px'}">
                                    <el-form-item label="请求url" prop="url">
                                        <el-input v-model="inputForm.url" placeholder="请输入请求url" autocomplete="off" @change="generateInputApi"></el-input>
                                    </el-form-item>
                                    <el-form-item label="方法名" prop="name">
                                        <el-input v-model="inputForm.name" placeholder="请输入方法名" autocomplete="off" @change="generateInputApi"></el-input>
                                    </el-form-item>
                                    <el-form-item label="请求类型" prop="method">
                                        <el-select v-model="inputForm.method" placeholder="请选择请求类型" @change="generateInputApi" style="width:100%;">
                                            <el-option label="Post" value="post">
                                            </el-option>
                                            <el-option label="Get" value="get">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="方法提示" prop="note">
                                        <el-input v-model="inputForm.note" placeholder="请输入方法提示" autocomplete="off" @change="generateInputApi"></el-input>
                                    </el-form-item>
                                </el-form>
                            </el-col>
                        </el-row>
                    </el-tab-pane>
                    <el-tab-pane label="本地Api">
                        <el-input style="margin:10px 0;"
                                  placeholder="输入关键字进行过滤"
                                  v-model="filterText">
                        </el-input>
                        <el-tree :style="{overflow:'auto',height:treeHeight+'px'}"
                                 ref="tree"
                                 :data="apiTree"
                                 show-checkbox
                                 node-key="id"
                                 default-expand-all
                                 @check="handleCheck"
                                 :props="defaultProps"
                                 check-on-click-node
                                 :filter-node-method="filterNode">
                        </el-tree>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </el-col>
        <el-col :span="18" :style="{height:codemirrorHeight+'px'}">
            <textarea ref="codemirror" v-model="code"></textarea>
        </el-col>
    </el-row>
</div>
<script src="../api/api_list.js"></script>
</body>
</html>
