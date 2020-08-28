var vm = new Vue({
    el: '#main',
    data: function(){
        return {
            defaultActive:'',
            isCollapse:false,
            frameHeight:300,
            frameWidth:300
        }
    },
    methods: {},
    computed: {},
    watch: {},
    created: function () {
        var _self = this;
        addLayoutListen(function (width,height) {
            _self.frameHeight = height - 70;
        })
    }
})
