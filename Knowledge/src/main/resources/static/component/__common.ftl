<#assign contextPath=request.contextPath />
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<base href="${contextPath}/"/>
<script type="text/javascript" src="lib/default-passive-events/dist/index.js"></script>
<script src="lib/vue/dist/vue.js"></script>
<script src="lib/element-ui/lib/index.js"></script>
<script src="lib/axios/dist/axios.js"></script>
<script src="lib/jquery/jquery.js"></script>
<link rel="stylesheet" href="lib/element-ui/lib/theme-chalk/index.css">
<script>
    var _contextPath = "${contextPath}";

    function addLayoutListen(func) {
        if (func && func instanceof Function) {
            func(window.innerWidth, window.innerHeight);
            if (window.addEventListener) {
                window.addEventListener('resize', function () {
                    func(window.innerWidth, window.innerHeight)
                })
            } else if (window.attachEvent) {
                window.attachEvent('onresize', function () {
                    func(window.innerWidth, window.innerHeight)
                })
            }
        }
    }

    String.prototype.replaceAll = function (s1, s2) {
        return this.replace(new RegExp(s1, "gm"), s2);
    }

    function dateFormat(fmt, date) {
        if (!(date instanceof Date)) {
            return date;
        }
        let ret;
        const opt = {
            "y+": date.getFullYear().toString(),        // 年
            "M+": (date.getMonth() + 1).toString(),     // 月
            "d+": date.getDate().toString(),            // 日
            "h+": date.getHours().toString(),           // 时
            "m+": date.getMinutes().toString(),         // 分
            "s+": date.getSeconds().toString()          // 秒
            // 有其他格式化字符需求可以继续添加，必须转化成字符串
        };
        for (let k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
            }
            ;
        }
        ;
        return fmt;
    };

    function loadEnum(vm, name, label, options, map, func) {
        axios.get(_contextPath + '/enum/' + name).then((resp) => {
            if (resp && resp.data && resp.data.success) {
                resp.data.data.forEach(function (item) {
                    if (map) {
                        Vue.set(map, item.value, item.label);
                    }
                    if (options) {
                        options.push(item);
                    }
                });
                if (func && func instanceof Function) {
                    func();
                }
            } else if (resp && resp.data && resp.data.msg) {
                vm.$message({
                    type: 'error',
                    showClose: true,
                    message: '载入' + label + '枚举信息失败，失败原因：' + resp.data.msg
                });
                console.error(resp);
            } else {
                vm.$message({
                    type: 'error',
                    showClose: true,
                    message: '载入' + label + '枚举信息失败！'
                });
                console.error(resp);
            }
        }).catch((err) => {
            vm.$message({
                type: 'error',
                showClose: true,
                message: '载入' + label + '枚举信息失败！'
            });
            console.error(err);
        });
    }
</script>
<style>

    [v-cloak] {
        display: none;
    }

    body {
        margin: 0px;
        width: 100%;
        height: 100%;
        overflow: auto;
        font-size: 12px;
        margin: 0;
        padding: 0;
        font-family: '微软雅黑';
        background: #fff;
    }

    .frame-class {
        border-width: 0px;
    }

    .el-dialog__body {
        padding-top: 0px;
        padding-bottom: 0px;
    }

    th.is-center.is-leaf {
        background-color: #eef1f6;
        color: #475669;
    }

    .el-table td, .el-table th {
        padding: 5px 0;
    }

    ::-webkit-scrollbar {
        width: 10px;
        height: 10px;
    }

    .elx-placeholder, div {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
    }

    #toolbar .el-form-item__label, *, :after, :before {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
    }

    html {
        -webkit-text-size-adjust: 100%;
        -ms-text-size-adjust: 100%;
    }

    #toolbar .el-form-item__label, *, :after, :before {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
    }

    #toolbar .el-form-item__label, *, :after, :before {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
    }

    ::-webkit-scrollbar {
        width: 10px;
        height: 10px;
    }

    ::-webkit-scrollbar-thumb {
        background-color: rgba(50, 50, 50, 0.25);
        border: 2px solid transparent;
        -webkit-border-radius: 10px;
        border-radius: 10px;
        background-clip: padding-box;
    }

    ::-webkit-scrollbar-track {
        background-color: rgba(50, 50, 50, 0.05);
    }

    .el-table {
        border-radius: 4px;
    }

    .el-main {
        padding: 10px 10px 5px 10px;
    }

    .el-header {
        text-align: left;
        height: unset !important;
        padding: 10px 10px 0 10px;
    }

    .el-footer {
        text-align: left;
        padding: 0;
        height: unset !important;
    }
</style>
