<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>我的待办</title>

</head>
<body style="width: 100%;height: 100%;overflow:auto;font-size:12px;margin:0;padding:0; font-family:'微软雅黑';background:#fff; text-align:center;">
<div class="main">
    <!--nr start-->
    <div style="width:calc(100% - 30px);  margin:15px;">
        <div style="float:left;width:100%;height:100%;min-width:400px;">
            <div style="width:100%;border:1px solid #e4e4e4; text-align:left; float:left;">
                <form action="" method="post">
                    <#if todoItems??&&(todoItems?size>0)>
                        <table border="1" style="width:100%;border-spacing: 0;">
                            <tr>
                                <th>序号</th>
                                <th>分类</th>
                                <th>内容</th>
                                <th>预计完成时间</th>
                                <th>交付时间</th>
                            </tr>
                            <#list todoItems as item>
                                <tr>
                                    <td>${item_index+1}</td>
                                    <td>${item.group}</td>
                                    <td>${item.name}</td>
                                    <td>${item.estimateEndTime}</td>
                                    <td>${item.leadTime}</td>
                                </tr>
                            </#list>
                        </table>
                    </#if>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
