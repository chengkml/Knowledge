    <#assign contextPath=request.contextPath />
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <script src="${contextPath}/lib/vue/dist/vue.js"></script>
    <script src="${contextPath}/lib/element-ui/lib/index.js"></script>
    <script src="${contextPath}/lib/axios/dist/axios.js"></script>
    <link rel="stylesheet" href="${contextPath}/lib/element-ui/lib/theme-chalk/index.css">
    <script>
        var _contextPath = "${contextPath}";
        var addLayoutListen = function (func) {
            if(func&&func instanceof Function){
                func(window.innerWidth,window.innerHeight);
                if (window.addEventListener) {
                    window.addEventListener('resize', function(){func(window.innerWidth,window.innerHeight)})
                } else if (window.attachEvent) {
                    window.attachEvent('onresize', function(){func(window.innerWidth,window.innerHeight)})
                }
            }
        }
        String.prototype.replaceAll = function(s1,s2){
            return this.replace(new RegExp(s1,"gm"),s2);
        }
    </script>
    <style>

        [v-cloak] {
            display: none;
        }

        body {
            margin:0px;
        }
        .frame-class {
            border-width: 0px;
        }

        div::-webkit-scrollbar {
            /*滚动条整体样式*/
            width : 10px;  /*高宽分别对应横竖滚动条的尺寸*/
            height: 1px;
        }
        div::-webkit-scrollbar-thumb {
            /*滚动条里面小方块*/
            border-radius   : 10px;
            background-color: skyblue;
            background-image: -webkit-linear-gradient(
                    45deg,
                    rgba(255, 255, 255, 0.2) 25%,
                    transparent 25%,
                    transparent 50%,
                    rgba(255, 255, 255, 0.2) 50%,
                    rgba(255, 255, 255, 0.2) 75%,
                    transparent 75%,
                    transparent
            );
        }
        div::-webkit-scrollbar-track {
            /*滚动条里面轨道*/
            box-shadow   : inset 0 0 5px rgba(0, 0, 0, 0.2);
            background   : #ededed;
            border-radius: 10px;
        }
    </style>
