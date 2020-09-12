var vm = new Vue({
    el: '#main',
    data: function(){
        return {
            openedMenu:[],
            menus:[],
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
            this.currMenu = Number(index);
            if(this.openedMenu.indexOf(Number(index))===-1){
                this.openedMenu.push(Number(index));
            }
        },
        getMenuTree(){
            axios.get(_contextPath + '/menu/tree').then((resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.menuData = resp.data.data[0].subMenus;
                    let getTreeMap = function (tree,map,menus) {
                        if(tree&&tree.length>0){
                            tree.forEach(item=>{
                                map[item.id] = item.url;
                                menus.push({
                                    id:item.id,
                                    url:item.url
                                });
                                if(item.subMenus){
                                    getTreeMap(item.subMenus,map,menus);
                                }
                            })
                        }
                    }
                    getTreeMap(this.menuData,this.menuUrlMap,this.menus);
                    this.currMenu = this.menuData[0].id;
                    this.openedMenu.push(this.menuData[0].id);
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
