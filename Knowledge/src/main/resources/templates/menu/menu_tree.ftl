<!DOCTYPE html>
<html>
<head>
    <title>知识管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="${contextPath}/menu/menu_tree.css">
    <script src="${contextPath}/lib/gojs/go.js"></script>
</head>
<body>
<div id="main">
    <div id="menuTree" :style="{width:'100%',height:graphHeight+'px'}"></div>
    <ul id="contextMenu" class="ctxmenu">
        <li id="addMenu" class="menu-item" @click="addMenu">新增页面</li>
        <li id="deleteMenu" class="menu-item" @click="deleteMenu">删除页面</li>
    </ul>
</div>
<script src="${contextPath}/menu/menu_tree.js"></script>
</body>
</html>
