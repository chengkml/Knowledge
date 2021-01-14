<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <#include "../component/__common.ftl"/>
    <title>用户列表</title>
</head>
<body>
<div id="main">
    <el-container>
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
                        label="知识点"
                        header-align="center"
                        show-overflow-tooltip
                        align="center">
                </el-table-column>
                <el-table-column
                        prop="descr"
                        label="描述"
                        header-align="center"
                        show-overflow-tooltip
                        min-width="300"
                        align="center">
                </el-table-column>
                <el-table-column
                        prop="category"
                        label="分类"
                        header-align="center"
                        show-overflow-tooltip
                        align="center">
                    <template slot-scope="scope">
                        <el-tag type="warning">{{categoryMap.get(scope.row.category)}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        :formatter="formatCreateTime"
                        label="创建时间"
                        header-align="center"
                        show-overflow-tooltip
                        align="center">
                </el-table-column>
            </el-table>
        </el-main>
    </el-container>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#main',
        data() {
            return {
                userTableHeight: 400;
        }
        },
        methods: {},
        computed: {},
        watch: {},
        created() {
            addLayoutListen((width, height) => {
                this.userTableHeight = height - 30;
            });
        }
    })
</script>
</html>
