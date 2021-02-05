<!DOCTYPE html>
<html>
<head>
    <title>Cosmic检查规则</title>
    <#include "../../component/__common.ftl"/>
    <link rel="stylesheet" href="../cosmic/rule/cosmic_check_rule.css">
</head>
<body style="margin:0px;">
<div id="main" v-cloak>
    <el-container>
        <el-header>
            <el-input v-model="filter.keyWord" placeholder="请输入关键字" style="width:200px" size="medium"></el-input>
            <el-button type="primary" plain @click="search" size="medium">查 询</el-button>
            <el-button type="primary" plain @click="toImport" size="medium">导 入</el-button>
        </el-header>
        <el-main>
            <el-table
                    :data="ruleData"
                    style="width: 100%">
                <el-table-column
                        prop="sortNum"
                        label="序号"
                        width="60">
                </el-table-column>
                <el-table-column
                        prop="element"
                        label="检查部分">
                </el-table-column>
                <el-table-column
                        prop="element"
                        label="检查规则">
                </el-table-column>
            </el-table>
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page.pageNum"
                    :page-sizes="[20,50,100, 200, 300, 400]"
                    :page-size="page.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="page.total">
            </el-pagination>
        </el-main>
    </el-container>
</div>
<script src="../cosmic/rule/cosmic_check_rule.js"></script>
<link rel="stylesheet"
      href="../../lib/handsontable/handsontable.full.css">
<script src="../../lib/handsontable/handsontable.full.js"></script>
<script src="../../lib/handsontable/vue-handsontable.js"></script>
</body>
</html>
