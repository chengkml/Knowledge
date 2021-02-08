var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            jobClassOptions:[],
            dialogHeight:300,
            jobTypeOptions: [],
            jobTypeMap: {},
            codemirror: null,
            exeLog: '',
            exeLogDialog: false,
            ckSocket: null,
            saveTitle: '新增Job',
            stateMap: {},
            stateOptions: [],
            pickerOptions: {
                shortcuts: [
                    {
                        text: '今天中午',
                        onClick(picker) {
                            const date = new Date();
                            let targetDate = new Date();
                            targetDate.setHours(14);
                            targetDate.setMinutes(0);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '下班前一个小时',
                        onClick(picker) {
                            const date = new Date();
                            let targetDate = new Date();
                            targetDate.setHours(17);
                            targetDate.setMinutes(0);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '下班前',
                        onClick(picker) {
                            const date = new Date();
                            let targetDate = new Date();
                            targetDate.setHours(18);
                            targetDate.setMinutes(0);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '明早',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24);
                            let targetDate = new Date();
                            targetDate.setDate(date.getDate());
                            targetDate.setHours(9);
                            targetDate.setMinutes(30);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '明天下班前1个小时',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24);
                            let targetDate = new Date();
                            targetDate.setDate(date.getDate());
                            targetDate.setHours(17);
                            targetDate.setMinutes(0);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '明天下班前',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24);
                            let targetDate = new Date();
                            targetDate.setDate(date.getDate());
                            targetDate.setHours(18);
                            targetDate.setMinutes(0);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    },
                    {
                        text: '一周后',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
                            let targetDate = new Date();
                            targetDate.setDate(date.getDate());
                            targetDate.setHours(9);
                            targetDate.setMinutes(30);
                            targetDate.setSeconds(0);
                            picker.$emit('pick', targetDate);
                        }
                    }
                ]
            },
            groupMap: {},
            currTab: 'main',
            rules: {
                name: [
                    {required: true, message: '请输入任务英文名', trigger: 'blur'}
                ],
                label: [
                    {required: true, message: '请输入任务中文名', trigger: 'blur'}
                ],
                type: [
                    {required: true, message: '请选择任务类型', trigger: 'blur'}
                ],
                cron: [
                    {required: true, message: '请输入任务定时配置', trigger: 'blur'}
                ],
                jobClass: [
                    {required: true, message: '请选择任务执行类', trigger: 'blur'}
                ]
            },
            showContentButton: true,
            currRow: null,
            showTabRightMenu: false,
            filterTreeHeight: 400,
            relaGraph: null,
            relaDialog: false,
            tabHeight: 400,
            categoryOperTitle: '',
            rightNode: {
                node: null,
                data: null
            },
            rightMenu: {
                display: '',
                right: '',
                top: ''
            },
            tabRightMenu: {
                display: '',
                right: '',
                top: ''
            },
            showRightMenu: false,
            showRelaButton: false,
            categoryDialog: false,
            categoryForm: {
                id: '',
                parentId: '',
                name: '',
                descr: ''
            },
            detailDialog: false,
            currDetail: '',
            jobDialog: false,
            formLabelWidth: '100px',
            form: {
                id: '',
                name: '',
                label: '',
                type: 'cron',
                cron: '',
                jobClass:''
            },
            filterText: '',
            categoryTree: [],
            categoryCascaderTree: [],
            treeProps: {
                children: 'children',
                label: 'descr'
            },
            knowledgeData: [],
            filter: {
                keyWord: '',
                states: ['waiting', 'running'],
                group: '',
                pageNum: 1,
                pageSize: 20
            },
            knowledgeTotal: 0
        }
    },
    methods: {

        fireJob(row){
            axios.post(_contextPath + '/job/fire', this.currRow.id, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.$notify({
                        type:'success',
                        title:'操作成功',
                        showClose:true,
                        message:'触发任务成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'触发任务失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'触发任务失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'触发任务失败!'
                });
                console.error(err);
            });
        },

        scanJobClass(){
            axios.get(_contextPath + '/job/scanJobClass').then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.jobClassOptions = resp.data.data;
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'扫描Job类失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'扫描Job类失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'扫描Job类失败!'
                });
                console.error(err);
            });
        },

        formatJobType(row) {
            return this.jobTypeMap[row.type] || row.type;
        },

        delete(){
            axios.post(_contextPath + '/job/delete', this.currRow.id, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.list();
                    this.$notify({
                        type:'success',
                        title:'操作成功',
                        showClose:true,
                        message:'删除任务成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'删除任务失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'删除任务失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'删除任务失败!'
                });
                console.error(err);
            });
        },
        list() {
            axios.get(_contextPath + '/job/search', {
                params: this.searchParams
            }).then((resp) => {
                if (resp && resp.data && resp.data.success) {
                    this.knowledgeData = resp.data.data.content;
                    this.knowledgeTotal = resp.data.data.totalElements;
                } else if (resp && resp.data && resp.data.msg) {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Job失败，失败原因：' + resp.data.msg
                    });
                    console.error(resp);
                } else {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '查询Job失败!'
                    });
                    console.error(resp);
                }
            }).catch((err) => {
                this.$message({
                    type: 'error',
                    showClose: true,
                    message: '查询Job失败!'
                });
                console.error(err);
            });
        },
        doSave(){
            axios.post(_contextPath + '/job/save',this.saveParams).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.jobDialog = false;
                    this.list();
                    this.$notify({
                        type:'success',
                        title:'操作成功',
                        showClose:true,
                        message:'保存任务成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'保存任务失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'保存任务失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'保存任务失败!'
                });
                console.error(err);
            });
        },

        getTabHeight: function () {
            this.tabHeight = window.innerHeight - 105;
            this.dialogHeight = window.innerHeight*0.7-120;
        },

        editRow: function () {
            this.form = $.extend({}, this.currRow);
            this.form.createDate = new Date(this.currRow.createDate);
            this.saveTitle = '编辑Job';
            this.jobDialog = true;
        },
        tabRightClick: function (row, column, e) {
            e.preventDefault();
            var fitWidth = e.clientX + 72 < window.innerWidth ? e.clientX : window.innerWidth - 72;
            this.tabRightMenu.left = fitWidth + 'px';
            var fitHeight = e.clientY + 3 * 30 < window.innerHeight ? e.clientY : window.innerHeight - 3 * 30;
            this.tabRightMenu.top = fitHeight + 'px';
            this.tabRightMenu.display = 'block';
            this.showTabRightMenu = true;
            this.currRow = row;
        },
        clickPage: function () {
            this.showRightMenu = false;
            this.showTabRightMenu = false;
        },
        viewDetail: function () {
            this.currDetail = this.currRow.detail;
            this.detailDialog = true;
        },
        confirmDeleteItem() {
            this.$confirm('确定删除该Job吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.delete();
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '删除Job失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },

        save: function () {
            if (this.$refs.form) {
                this.$refs.form.validate((res) => {
                    if (res) {
                        this.doSave();
                    }
                });
            }
        },

        toAdd: function () {
            this.form.id = '';
            this.form.name = '';
            this.form.label = '';
            this.form.type = 'cron';
            this.form.cron = '';
            this.form.jobClass = '';
            this.saveTitle = '新增Job';
            this.jobDialog = true;
        },
        changePageSize: function (val) {
            this.filter.pageSize = val;
            this.filter.pageNum = 1;
            this.list();
        },
        changeCurrPage: function (val) {
            this.filter.pageNum = val;
            this.list();
        }

    },
    computed: {
        searchParams: function () {
            var res = $.extend({}, this.filter);
            res.pageNum--;
            res.states = res.states.join(',');
            return res;
        },
        saveParams: function () {
            var res = $.extend({}, this.form);
            res.createDate = dateFormat('yyyy-MM-dd hh:mm:ss', res.createDate);
            return res;
        }
    },
    created: function () {
        this.list();
        this.getTabHeight();
        loadEnum(this, 'job:type', '任务类型', this.jobTypeOptions, this.jobTypeMap);
    },
    mounted(){
        this.scanJobClass();
    }
})
