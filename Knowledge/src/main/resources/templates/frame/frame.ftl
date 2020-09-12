<!DOCTYPE html>
<html>
<head>
    <title>知识管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="${contextPath}/frame/frame.css">
</head>
<body style="margin:0px;">
<div id="main" v-cloak>
    <el-menu :default-active="defaultActive" mode="horizontal" :collapse="isCollapse" @select="handleSelect">
        <template v-for="item in menuData">
            <el-submenu v-if="item.subMenus&&item.subMenus.length>0" :index="item.id.toString()">
                <template slot="title">
                    <span slot="title">{{item.label}}</span>
                </template>
                <el-menu-item-group>
                    <template v-for="jtem in item.subMenus">
                        <el-submenu v-if="jtem.subMenus&&jtem.subMenus.length>0" :index="jtem.id.toString()">
                            <template slot="title">
                                <span slot="title">{{jtem.label}}</span>
                            </template>
                            <el-menu-item-group>
                                <el-menu-item v-for="mtem in jtem.subMenus" :index="mtem.id.toString()">{{mtem.label}}</el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                        <el-menu-item v-else :index="jtem.id.toString()">{{jtem.label}}</el-menu-item>
                    </template>
                </el-menu-item-group>
            </el-submenu>
            <el-menu-item v-else :index="item.id.toString()">
                {{item.label}}
            </el-menu-item>
        </template>

    </el-menu>
    <template v-for="item in menus">
        <template v-if="openedMenu.indexOf(item.id)!==-1">
            <iframe v-show="item.id===currMenu" :src="item.url" :style="{height:frameHeight+'px',overflowX:'hidden',width:'calc(100% - 5px)'}" class="frame-class"></iframe>
        </template>
    </template>


</div>
<script src="${contextPath}/frame/frame.js"></script>
</body>
</html>
