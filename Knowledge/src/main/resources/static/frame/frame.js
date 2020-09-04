var vm = new Vue({
    el: '#main',
    data: function(){
        return {
            currMenu:'',
            menuUrlMap:{},
            menuData:[],
            defaultActive:'',
            isCollapse:false,
            frameHeight:300,
            frameWidth:300
        }
    },
    methods: {
        handleSelect(index){
            this.currMenu = this.menuUrlMap[index];
        },
        getMenuTree(){
            axios.get(_contextPath + '/menu/tree').then((resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.menuData = resp.data.data[0].subMenus;
                    let getTreeMap = function (tree,map) {
                        if(tree&&tree.length>0){
                            tree.forEach(item=>{
                                map[item.id] = item.url;
                                if(item.subMenus){
                                    getTreeMap(item.subMenus,map);
                                }
                            })
                        }
                    }
                    getTreeMap(this.menuData,this.menuUrlMap);
                    this.currMenu = this.menuUrlMap[this.menuData[0].id];
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询菜单树失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询菜单树失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'查询菜单树失败!'
                });
                console.error(err);
            });
        }
    },
    computed: {},
    watch: {},
    created: function () {
        addLayoutListen( (width,height) =>{
            this.frameHeight = height - 70;
        })
        this.getMenuTree();
    }
})
