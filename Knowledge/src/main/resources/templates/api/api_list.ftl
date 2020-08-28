<!DOCTYPE html>
<html>
<head>
    <title>api管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="${contextPath}/api/api_list.css">
    <link rel="stylesheet" href="${contextPath}/lib/codemirror-5.56.0/lib/codemirror.css"/>
    <script src="${contextPath}/lib/codemirror-5.56.0/lib/codemirror.js"></script>
    <script src="${contextPath}/lib/codemirror-5.56.0/mode/javascript/javascript.js"></script>
    <link rel="stylesheet" href="${contextPath}/lib/codemirror-5.56.0/theme/eclipse.css"/>
</head>
<body style="margin:0px;">
<div id="main">
    <el-row>
        <el-col :span="6">
            <div style="padding:0 0 10px 10px;background-color:#f7f7f7;">
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
            </div>
        </el-col>
        <el-col :span="18" :style="{height:codemirrorHeight+'px'}">
            <textarea ref="codemirror" v-model="code"></textarea>
        </el-col>
    </el-row>
</div>
<script src="${contextPath}/api/api_list.js"></script>
</body>
</html>
