console.log("开始解释执行脚本。");
sleep(5000);
console.log("解释执行脚本结束开始加载页面。");

function sleep(numberMillis) {  
    var now = new Date();  
    var exitTime = now.getTime() + numberMillis;  
    while (true) {  
        now = new Date();  
        if (now.getTime() > exitTime)  
        return;  
        }  
}