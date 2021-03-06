<!DOCTYPE html>
<html>
<head>
    <title>菜单管理</title>
    <#include "../component/__common.ftl"/>
    <link rel="stylesheet" href="../menu/menu_tree.css">
    <script src="../lib/gojs/go.js"></script>
</head>
<body>
<div id="main" v-cloak>
    <div id="menuTree" :style="{width:'100%',height:graphHeight+'px'}"></div>
    <ul id="contextMenu" class="ctxmenu">
        <li id="addMenu" class="menu-item" @click="toAddMenu">新增页面</li>
        <li id="deleteMenu" class="menu-item" @click="deleteMenu">删除页面</li>
    </ul>
    <el-dialog title="新增页面" :visible.sync="saveMenuDialog" :close-on-click-modal="false">
        <el-row :style="{height:dialogHeight+'px',overflow:'auto'}">
            <el-col :span="23">
                <el-form ref="menuForm" :model="form" :label-width="formLabelWidth" label-suffix=":" :rules="rules">
                    <el-form-item label="页面编码" prop="name">
                        <el-input v-model="form.name" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="页面标题" prop="label">
                        <el-input v-model="form.label" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="页面路由">
                        <el-input v-model="form.url" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="页面描述">
                        <el-input v-model="form.descr" type="textarea" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="页面排序" prop="sort">
                        <el-input v-model="form.sort" size="small"></el-input>
                    </el-form-item>
                    <el-form-item label="页面状态">
                        <el-radio v-model="form.valid" label="valid">生效</el-radio>
                        <el-radio v-model="form.valid" label="invalid">失效</el-radio>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer">
            <el-row>
                <el-col :span="23">
                    <el-button @click="saveMenuDialog = false" size="small">取 消</el-button>
                    <el-button type="primary" @click="saveMenu" size="small">确 定</el-button>
                </el-col>
            </el-row>
        </span>
    </el-dialog>
</div>
<script src="../menu/menu_tree.js"></script>
</body>
</html>
