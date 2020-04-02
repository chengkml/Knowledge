<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>知识管理练习</title>

</head>
<body style="width: 100%;height: 100%;overflow:auto;font-size:12px;margin:0;padding:0; font-family:'微软雅黑';background:#fff; text-align:center;">
<div class="main">
    <!--nr start-->
    <div style="width:calc(100% - 30px);  margin:15px;">
        <div style="float:left;width:100%;height:100%;min-width:400px;">
            <div style="width:100%;border:1px solid #e4e4e4; text-align:left; float:left;">
                <form action="" method="post">
                    <#if selectQuestions??&&(selectQuestions?size>0)>
                        <div style="width:100%; height:50px; line-height:50px; background:#f7f7f7; text-align:center;">
                        <h2 style="margin:0px;padding:0 30px; font-size:16px; font-weight:normal; display:block; float:left">
                            单选题</h2>
                        <p style="padding:0 10px; height:40px; line-height:40px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                        <span>共</span><i class="content_lit">${selectQuestions?size}</i><span>题</span>
                        </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                        <ul>
                        <#list selectQuestions as item>
                            <li style="margin:0px;list-style:none;width:100%;  border-bottom:5px solid #efefef; padding-top:10px;">
                            <div style="width:85%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-50px; color:#fff; margin-top:8px; font-size:16px;">${item_index+1}</i>
                            <font
                                    style="font-size:14px">${item.stem}</font>
                            </div>
                            <div style="width:85%; margin:0 auto; padding:10px 0;height:auto;">
                            <ul style="padding:0px;">
                            <#list item.optionItems as oItem>
                                <li class="option" style="line-height:32px; display:block; background:#fff;color:#666;">
                                <input type="radio" class="radioOrCheck" name="answer1"
                                       style="width:20px; height:20px; display:block; float:left; margin:10px 10px 0 0;"
                                />
                                <label style="height:auto;display:block;">
                                <#switch oItem_index>
                                    <#case 0>A<#break>
                                    <#case 1>B<#break>
                                    <#case 2>C<#break>
                                    <#case 3>D<#break>
                                </#switch>
                            .<p class="ue" style="display: inline;">${oItem}</p>
                                </label>
                                </li>
                            </#list>

                            </ul>
                            </div>
                            </li>
                        </#list>
                        </ul>
                        <div>
                    答案：
                        <#list selectQuestions as item>
                            <span>${item_index+1}.${item.result}</span>
                        </#list>
                        </div>
                        </div>
                    </#if>
                    <#if multiSelectQuestions??&&(multiSelectQuestions?size>0)>
                        <div style="width:100%; height:50px; line-height:50px; background:#f7f7f7; text-align:center;">
                        <h2 style="margin:0px;padding:0 30px; font-size:16px; font-weight:normal; display:block; float:left">
                            多选题</h2>
                        <p style="padding:0 10px; height:40px; line-height:40px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                        <span>共</span><i class="content_lit">${multiSelectQuestions?size}</i><span>题</span>
                        </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                        <ul>
                        <#list multiSelectQuestions as item>
                            <li style="margin:0px;list-style:none;width:100%;  border-bottom:5px solid #efefef; padding-top:10px;">
                            <div style="width:85%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-50px; color:#fff; margin-top:8px; font-size:16px;">${item_index+1}</i>
                            <font
                                    style="font-size:14px">${item.stem}</font>
                            </div>

                            <div style="width:85%; margin:0 auto; padding:10px 0;height:auto;">
                            <ul style="padding:0px;">
                            <#list item.optionItems as oItem>
                                <li style="line-height:32px; display:block; background:#fff;color:#666;">


                                <input type="checkbox" class="radioOrCheck" name="answer1"
                                       style="width:20px; height:20px; display:block; float:left; margin:10px 10px 0 0;"
                                />

                                <label style="height:auto;display:block;">
                                <#switch oItem_index>
                                <#case 0>A<#break>
                                <#case 1>B<#break>
                                <#case 2>C<#break>
                                <#case 3>D<#break>
                                </#switch>.
                                <p class="ue" style="display: inline;">${oItem}</p>
                                </label>
                                </li>
                            </#list>
                            </ul>
                            </div>
                            </li>
                        </#list>
                        </ul>
                        <div>答案：
                        <#list multiSelectQuestions as item>
                            <span>${item_index+1}.${item.result}</span>
                        </#list>
                        </div>
                        </div>
                    </#if>
                    <#if judgeQuestions??&&(judgeQuestions?size>0)>
                        <div style="width:100%; height:50px; line-height:50px; background:#f7f7f7; text-align:center;">
                        <h2 style="margin:0px;padding:0 30px; font-size:16px; font-weight:normal; display:block; float:left">
                            判断题</h2>
                        <p style="padding:0 10px; height:40px; line-height:40px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                        <span>共</span><i class="content_lit">${judgeQuestions?size}</i><span>题</span>
                        </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                        <ul>
                        <#list judgeQuestions as item>
                            <li style="margin:0px;list-style:none;width:100%;  border-bottom:5px solid #efefef; padding-top:10px;">
                            <div style="width:85%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-50px; color:#fff; margin-top:8px; font-size:16px;">${item_index+1}</i>
                            <font
                                    style="font-size:14px">${item.stem}</font>
                            </div>
                            </li>
                        </#list>
                        </ul>
                        <div>答案：
                        <#list judgeQuestions as item>
                            <span>${item_index+1}.
                            <#switch item.result>
                            <#case "RIGHT">正确<#break>
                            <#case "FALSE">错误<#break>
                        </#switch></span>
                        </#list>
                        </div>
                        </div>
                    </#if>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
