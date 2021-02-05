var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            importDialog:false,
            filter:{
                keyWord:''
            },
            ruleData:[],
            page:{
                pageSize:20,
                pageNum:1,
                total:0
            }
        }
    },
    methods: {
        toImport(){
            this.importDialog = true;
        },
        search(){
            axios.get(_contextPath + '/cosmic/check/rule/search',{
                params:this.searchParams
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.ruleData = resp.data.data.content;
                    this.page.total = resp.data.data.totalElements;
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询cosmic检查规则列表信息失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询cosmic检查规则列表信息失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'查询cosmic检查规则列表信息失败!'
                });
                console.error(err);
            });
        },
        handleSizeChange(val){
            this.page.pageNum = 1;
            this.search();
        },
        handleCurrentChange(val){
            this.search();
        }
    },
    computed: {
        searchParams(){
            var res = $.extend({},this.filter,this.page);
            res.pageNum = res.pageNum - 1;
            return res;
        }
    },
    watch: {
    },
    created: function () {
        this.search();
    }
})
