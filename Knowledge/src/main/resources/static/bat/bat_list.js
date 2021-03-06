var vm = new Vue({
    el: '#main',
    data: function () {
        return {
            runningProcessId:null,
            runningProcessNum:0,
            runningProcess:[],
            processDialog:false,
            processTabHeight:300,
            dialogHeight:300,
            codemirror:null,
            exeLog:'',
            exeLogDialog:false,
            ckSocket:null,
            saveTitle:'新增bat',
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
                    {required: true, message: '请输入脚本英文名', trigger: 'blur'}
                ],
                label: [
                    {required: true, message: '请输入脚本中文名', trigger: 'blur'}
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
            batDialog: false,
            formLabelWidth: '80px',
            batForm: {
                id: '',
                name: '',
                label:'',
                createDate: '',
                params:''
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
        viewLog(row){
            this.processDialog = false;
            this.runningProcessId = row.id;
            this.exeLogDialog = true;
            this.$nextTick(()=>{
                if(!this.codemirror){
                    this.codemirror = CodeMirror.fromTextArea(this.$refs.codemirror, {
                        mode:"text/javascript",
                        theme: "eclipse",
                        lineNumbers: true
                    });
                }
                this.exeLog = "";
            })
        },
        stopBat(row){
            this.$confirm('确定终止该进程吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.stopProcess(row.id);
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '终止进程失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },
        stopProcess(processId){
            axios.post(_contextPath + '/bat/stop/process',processId, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.listProcess();
                    this.$notify({
                        type:'success',
                        title:'操作成功',
                        showClose:true,
                        message:'停止运行的进程成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'停止运行的进程失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'停止运行的进程失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'停止运行的进程失败!'
                });
                console.error(err);
            });
        },
        listProcess(){
            axios.get(_contextPath + '/bat/list/process').then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.processDialog = true;
                    this.runningProcess = resp.data.data;
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询运行进程失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询运行进程失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'查询运行进程失败!'
                });
                console.error(err);
            });
        },

        startBat(row){
            axios.post(_contextPath + '/bat/exe',this.currRow.id, {
                headers: {
                    "Content-Type": "application/json;charset=utf-8"
                }
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.exeLogDialog = true;
                    this.runningProcessId = resp.data.data;
                    this.$nextTick(()=>{
                        if(!this.codemirror){
                            this.codemirror = CodeMirror.fromTextArea(this.$refs.codemirror, {
                                mode:"text/javascript",
                                theme: "eclipse",
                                lineNumbers: true
                            });
                        }
                        this.exeLog = "";
                    })
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'启动bat失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp.data.stackTrace);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'启动bat失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'启动bat失败!'
                });
                console.error(err);
            });
        },

        initCkSocket(topic,func){
            var protocol = location.protocol === 'https'
                ? 'wss://' + window.location.host + _contextPath + '/ws/ck/'
                : 'ws://' + window.location.host + _contextPath + '/ws/ck/'
            this.ckSocket = new WebSocket(protocol);
            this.ckSocket.onmessage = (event)=>{
                if('wait'== event.data) {
                    console.warn(event);
                }else {
                    try {
                        var temp = JSON.parse(event.data);
                        if(temp.topic===topic&&func&&func instanceof Function){
                            func(temp);
                        }
                    }catch(e) {
                        this.$message({
                            type: 'error',
                            showClose: true,
                            message: '数据解析失败！'
                        });
                        console.error(event);
                    }
                }
            };
        },

        delete(){
            axios.post(_contextPath + '/bat/delete',this.currRow.id, {
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
                        message:'删除bat成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'删除bat失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'删除bat失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'删除bat失败!'
                });
                console.error(err);
            });
        },
        list(){
            axios.get(_contextPath + '/bat/list', {
                params: this.searchParams
            }).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.knowledgeData = resp.data.data.content;
                    this.knowledgeTotal = resp.data.data.totalElements;
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询bat失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'查询bat失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'查询bat失败!'
                });
                console.error(err);
            });
        },
        doSave(){
            axios.post(_contextPath + '/bat/save', this.saveParams).then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                    this.batDialog = false;
                    this.list();
                    this.$notify({
                        type:'success',
                        title:'操作成功',
                        showClose:true,
                        message:'保存bat成功!'
                    });
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'保存bat失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    this.$notify({
                        type:'error',
                        title:'操作失败',
                        showClose:true,
                        message:'保存bat失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$notify({
                    type:'error',
                    title:'操作失败',
                    showClose:true,
                    message:'保存bat失败!'
                });
                console.error(err);
            });
        },
        generateApi(){
            axios.get(_contextPath + '/bat/exe').then( (resp)=> {
                if(resp&&resp.data&&resp.data.success){
                }else if(resp&&resp.data&&resp.data.msg){
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'执行bat失败，失败原因：'+resp.data.msg
                    });
                    console.error(resp);
                }else{
                    this.$message({
                        type:'error',
                        showClose:true,
                        message:'执行bat失败!'
                    });
                    console.error(resp);
                }
            }).catch((err)=>{
                this.$message({
                    type:'error',
                    showClose:true,
                    message:'执行bat失败!'
                });
                console.error(err);
            });
        },

        getTabHeight: function () {
            this.tabHeight = window.innerHeight - 90;
            this.dialogHeight = window.innerHeight*0.7-120;
            this.processTabHeight = window.innerHeight*0.7-50;
        },

        addLayoutListen: function () {
            this.getTabHeight();
            if (window.addEventListener) {
                window.addEventListener('resize', this.getTabHeight)
            } else if (window.attachEvent) {
                window.attachEvent('onresize', this.getTabHeight)
            }
        },

        editBat: function () {
            this.batForm = $.extend({},this.currRow);
            this.batForm.createDate = new Date(this.currRow.createDate);
            this.saveTitle = '编辑bat';
            this.batDialog = true;
            this.$nextTick(()=>{
                this.$refs.upload.loadFiles([this.currRow.bat]);
            })
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
        treeRightClick: function (e, data, node) {
            var fitWidth = e.clientX + 72 < window.innerWidth ? e.clientX : window.innerWidth - 72;
            this.rightMenu.left = fitWidth + 'px';
            var fitHeight = e.clientY + 3 * 30 < window.innerHeight ? e.clientY : window.innerHeight - 3 * 30;
            this.rightMenu.top = fitHeight + 'px';
            this.rightMenu.display = 'block';
            this.rightNode.node = node;
            this.rightNode.data = data;
            this.showRelaButton = !node.isLeaf;
            this.showRightMenu = true;
        },

        appendCategory: function () {
            this.categoryOperTitle = '新增类目';
            this.categoryDialog = true;
            this.categoryForm.parentId = this.rightNode.data.id;
        },
        editCategory: function () {
            this.categoryOperTitle = '编辑类目';
            this.categoryDialog = true;
            this.categoryForm.id = this.rightNode.data.id;
            this.categoryForm.parentId = this.rightNode.data.parentId;
            this.categoryForm.name = this.rightNode.data.name;
            this.categoryForm.descr = this.rightNode.data.descr;
        },
        viewDetail: function () {
            this.currDetail = this.currRow.detail;
            this.detailDialog = true;
        },
        confirmDeleteItem() {
            this.$confirm('确定删除该bat吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.delete();
            }).catch((err) => {
                if (err !== 'cancel') {
                    this.$message({
                        type: 'error',
                        message: '删除bat失败！',
                        showClose: true
                    });
                    console.error(err);
                }
            });
        },

        save: function () {
            if (this.$refs.batForm) {
                this.$refs.batForm.validate((res) => {
                    if (res) {
                        this.doSave();
                    }
                });
            }
        },

        clickTreeNode: function (data) {
            this.showRightMenu = false;
            this.filter.group = data.id;
            this.list();
        },
        filterFromTree: function (arr, id) {
            var _self = this;
            var temp = arr.filter(function (item) {
                return item.id == id;
            });
            if (temp.length > 0) {
                var res = [];
                res.push(temp[0].id);
                return res;
            } else {
                var temp2 = arr.filter(function (item) {
                    if (item.children && item.children.length > 0) {
                        return _self.filterFromTree(item.children, id);
                    }
                })
                if (temp2.length > 0) {
                    var res2 = [];
                    res2.push(temp2[0].id)
                    return res2.concat(_self.filterFromTree(temp2[0].children, id));
                }
            }
        },
        filterCascade: function (node, key) {
            if (!key) return true;
            return node.data.name.toLowerCase().indexOf(key.toLowerCase()) !== -1;
        },
        toAdd: function () {
            this.batForm.id = '';
            this.batForm.name = '';
            this.batForm.label = '';
            this.batForm.params = '';
            this.saveTitle = '新增Bat';
            this.batDialog = true;
            this.$nextTick(()=>{
                this.$refs.upload.reset();
            })
        },
        filterNode: function (value, data) {
            if (!value) return true;
            return data.descr.toLowerCase().indexOf(value.toLowerCase()) !== -1 || data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
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
            var res = $.extend({},this.batForm);
            res.createDate = dateFormat('yyyy-MM-dd hh:mm:ss', res.createDate);
            var fileIds = this.$refs.upload.getFileIds();
            if(fileIds.length>0){
                res.fileId = fileIds[0];
            }
            return res;
        },
        saveCategoryParams: function () {
            var res = {
                id: this.categoryForm.id,
                name: this.categoryForm.name,
                descr: this.categoryForm.descr,
                parentId: this.categoryForm.parentId
            };
            return res;
        }
    },
    watch: {
        filterText: function (val) {
            this.$refs.tree.filter(val);
        }
    },
    created: function () {
        this.list();
        this.getTabHeight();
        this.addLayoutListen();
        loadEnum(this, 'todo:item:state', '项目执行状态', this.stateOptions, this.stateMap);
        this.initCkSocket('batLog',(data)=>{
            this.exeLog+=data.log;
            if(this.codemirror&&this.runningProcessId===data.processId){
                this.codemirror.setValue(this.exeLog);
                this.codemirror.setCursor(this.codemirror.lastLine());
            }
            this.runningProcessNum = data.runningProcessNum;
        });
    }
})
