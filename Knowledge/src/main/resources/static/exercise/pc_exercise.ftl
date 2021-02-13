<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>知识管理练习</title>
    <script type="text/javascript">
        var answers = {};
        var updateAnswers = function(){
            if(${selectQuestions?size}>0){
                for(j=0;j<${selectQuestions?size};j++){
                    for(i=0;i<document.exerciseForm['select'+j].length;i++){
                        if (document.exerciseForm['select'+j][i].checked){
                            answers['select'+j] = i=='0'?'A':(i=='1'?'B':(i=='2'?'C':(i=='3'?'D':'')));
                        }
                    }
                }
            }
            if(${multiSelectQuestions?size}>0){
                for(j=0;j<${multiSelectQuestions?size};j++){
                    var temp = [];
                    for(i=0;i<document.exerciseForm['multiSelect'+j].length;i++){
                        if (document.exerciseForm['multiSelect'+j][i].checked){
                            if(i=='0'){
                                temp.push('A');
                            }else if(i=='1'){
                                temp.push('B');
                            }else if(i=='2'){
                                temp.push('C');
                            }else if(i=='3'){
                                temp.push('D');
                            }
                        }
                    }
                    answers['multiSelect'+j] = temp;
                }
            }
            if(${judgeQuestions?size}>0){
                for(j=0;j<${judgeQuestions?size};j++){
                    for(i=0;i<document.exerciseForm['judge'+j].length;i++){
                        if (document.exerciseForm['judge'+j][i].checked){
                            if(i=='0'){
                                answers['judge'+j] = true;
                            }else if(i=='1'){
                                answers['judge'+j] = false;
                            }
                        }
                    }
                }
            }
            document.getElementById('answers').innerHTML=JSON.stringify(answers)
        }
    </script>
</head>
<body style="width: 100%;height: 100%;overflow:auto;font-size:12px;margin:0;padding:0; font-family:'微软雅黑';background:#fff; text-align:center;">
<div class="main">
    <!--nr start-->
    <div style="width:calc(100% - 30px);  margin:15px;">
        <div style="float:left;width:100%;height:100%;min-width:400px;">
            <div style="width:100%;border:1px solid #e4e4e4; text-align:left; float:left;">
                <form action="" method="post" name="exerciseForm">
                    <#if selectQuestions??&&(selectQuestions?size>0)>
                        <div style="width:100%; height:40px; line-height:40px; background:#f7f7f7; text-align:center;">
                            <h2 style="margin:0px;padding:0 15px; font-size:16px; font-weight:normal; display:block; float:left">
                                单选题</h2>
                            <p style="padding:0 10px; height:30px; line-height:30px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                                <span>共</span><i class="content_lit">${selectQuestions?size}&nbsp;</i><span>题</span>
                            </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                            <ul>
                                <#list selectQuestions as item>
                                    <li style="margin:0px;list-style:none;width:95%;">
                                        <div style="width:95%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-40px; color:#fff; margin-top:4px; font-size:16px;">${item_index+1}</i>
                                            <font
                                                    style="font-size:14px">${item.stem}</font>
                                        </div>
                                        <div style="width:95%; margin:0 auto; padding:10px 0;height:auto;">
                                            <ul style="padding:0px;">
                                                <#list item.optionItems as oItem>
                                                    <li class="option"
                                                        style="line-height:32px; display:block; background:#fff;color:#666;">
                                                        <input type="radio" class="radioOrCheck" name="${'select'+item_index}" id="${'select'+item_index+'op'+oItem_index}"
                                                               style="width:20px; height:20px; display:block; float:left; margin:6px 10px 0 0;" onchange="updateAnswers()">
                                                        <label style="height:auto;display:block;" for="${'select'+item_index+'op'+oItem_index}">
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
                        </div>
                    </#if>
                    <#if multiSelectQuestions??&&(multiSelectQuestions?size>0)>
                        <div style="width:100%; height:40px; line-height:40px; background:#f7f7f7; text-align:center;">
                            <h2 style="margin:0px;padding:0 15px; font-size:16px; font-weight:normal; display:block; float:left">
                                多选题</h2>
                            <p style="padding:0 10px; height:30px; line-height:30px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                                <span>共</span><i class="content_lit">${multiSelectQuestions?size}&nbsp;</i><span>题</span>
                            </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                            <ul>
                                <#list multiSelectQuestions as item>
                                    <li style="margin:0px;list-style:none;width:95%;">
                                        <div style="width:95%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-40px; color:#fff; margin-top:4px; font-size:16px;">${item_index+1}</i>
                                            <font
                                                    style="font-size:14px">${item.stem}</font>
                                        </div>

                                        <div style="width:95%; margin:0 auto; padding:10px 0;height:auto;">
                                            <ul style="padding:0px;">
                                                <#list item.optionItems as oItem>
                                                    <li style="line-height:32px; display:block; background:#fff;color:#666;">


                                                        <input type="checkbox" class="radioOrCheck" name="${'multiSelect'+item_index}" id="${'multiSelect'+item_index+'op'+oItem_index}"
                                                               style="width:20px; height:20px; display:block; float:left; margin:6px 10px 0 0;" onchange="updateAnswers()">

                                                        <label style="height:auto;display:block;" for="${'multiSelect'+item_index+'op'+oItem_index}">
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
                        </div>
                    </#if>
                    <#if judgeQuestions??&&(judgeQuestions?size>0)>
                        <div style="width:100%; height:40px; line-height:40px; background:#f7f7f7; text-align:center;">
                            <h2 style="margin:0px;padding:0 15px; font-size:16px; font-weight:normal; display:block; float:left">
                                判断题</h2>
                            <p style="padding:0 10px; height:30px; line-height:30px; background:#389fc3; color:#fff; border-radius:30px; display:block; float:left; margin-top:5px;">
                                <span>共</span><i class="content_lit">${judgeQuestions?size}&nbsp;</i><span>题</span>
                            </p>
                        </div>
                        <div style="width:100%; border-top:3px solid #efefef;">
                            <ul>
                                <#list judgeQuestions as item>
                                    <li style="margin:0px;list-style:none;width:95%;  ">
                                        <div style="width:95%; height:auto; line-height:32px; margin:0 auto; border-bottom:1px solid #e4e4e4;">
                                            <i style="width:25px; height:25px; line-height:25px; text-align:center; display:block; float:left; background:#5d9cec; border-radius:50%; margin-left:-40px; color:#fff; margin-top:4px; font-size:16px;">${item_index+1}</i>
                                            <font
                                                    style="font-size:14px">${item.stem}</font>
                                        </div>
                                        <ul style="padding:0px;">
                                            <li class="option"
                                                style="line-height:32px; display:block; background:#fff;color:#666;">
                                                <input type="radio" class="radioOrCheck" name="${'judge'+item_index}" id="${'judge'+item_index+'op0'}"
                                                       style="width:20px; height:20px; display:block; float:left; margin:6px 10px 0 0;" onchange="updateAnswers()">
                                                <label style="height:auto;display:block;" for="${'judge'+item_index+'op0'}">
                                                    A.<p class="ue" style="display: inline;">正确</p>
                                                </label>
                                            </li>
                                            <li class="option"
                                                style="line-height:32px; display:block; background:#fff;color:#666;">
                                                <input type="radio" class="radioOrCheck" name="${'judge'+item_index}" id="${'judge'+item_index+'op1'}"
                                                       style="width:20px; height:20px; display:block; float:left; margin:6px 10px 0 0;" onchange="updateAnswers()">
                                                <label style="height:auto;display:block;" for="${'judge'+item_index+'op1'}">
                                                    B.<p class="ue" style="display: inline;">错误</p>
                                                </label>
                                            </li>
                                        </ul>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </#if>
                </form>
                <h3 style="margin-left:20px;">答案:<span id="answers"></span></h3>
            </div>
        </div>
    </div>
</div>
</body>
</html>
