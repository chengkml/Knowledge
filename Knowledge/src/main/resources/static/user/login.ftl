<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <#include "../component/__common.ftl"/>
    <title>登录</title>
</head>
<body>
<div id="main">
    <el-row :style="{marginTop:marginTop+'px'}">
        <el-col :offset="10" :span="4">
            <el-form :model="loginForm" :rules="rules" ref="loginForm">
                <el-form-item style="margin-bottom:10px;">
                    <el-input placeholder="请输入用户名" v-model="loginForm.userName"></el-input>
                </el-form-item>
                <el-form-item prop="pwd">
                    <el-input placeholder="请输入密码" v-model="loginForm.pwd" show-password></el-input>
                </el-form-item>
        </el-col>
    </el-row>
    <el-row>
        <el-col :offset="10" :span="4">
            <el-button type="primary" style="width:100%">登录</el-button>
        </el-col>
    </el-row>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#main',
        data() {
            return {
                loginForm:{
                    userName:'',
                    pwd:''
                },
                rules:{},
                marginTop:200
            }
        },
        methods: {},
        computed: {},
        watch: {},
        created() {
            addLayoutListen((width, height) => {
                marginTop = (height-100)/2;
            });
        }
    })
</script>
</html>
