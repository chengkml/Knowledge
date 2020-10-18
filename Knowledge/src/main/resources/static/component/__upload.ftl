<script type="text/x-template" id="ck-upload">
    <el-upload multiple ref="upload" :data="uploadParams" action="${contextPath}/res/upload" :on-remove="doDelete"
               :before-upload="beforeUpload" :on-success="uploadSuccess" :on-error="uploadError" :file-list="fileList">
        <span style="color:#409EFF;" slot="trigger"><u>选取文件</u></span>
        <div slot="tip"><span v-html="fileTip"></span></div>
    </el-upload>
</script>
<script>
    Vue.component('ck-upload', {
        template: "#ck-upload",
        props: {
            fileMaxSize: {
                type: Number,
                default: 0
            },
            fileTip: {
                type: String,
                default: ''
            }
        },
        data: function () {
            return {
                fileList: [],
                uploadingNums: 0,
                fileIdMap: {},
                uploadParams: {path: ''}
            }
        },
        methods: {
            loadFiles(files){
                this.$refs.upload.uploadFiles = [];
                files.forEach((file)=>{
                    if(file){
                        var tempFile = {
                            id:file.id,
                            name:file.name,
                            percentage:100,
                            response:{success:true,msg:'',data:[{success:true,msg:'',data:file.id}]},
                            status:'success',
                            uid:file.id
                        }
                        this.fileIdMap[tempFile] = tempFile.id;
                        this.$refs.upload.uploadFiles.push(tempFile);
                    }
                });

            },
            reset(){
                this.$refs.upload.uploadFiles = [];
                this.uploadingNums = 0;
                this.fileIdMap = {};
            },
            getFileIds() {
                var fileIds = [];
                this.$refs.upload.uploadFiles.forEach((file) => {
                    fileIds.push(file.id);
                });
                return fileIds;
            },
            uploadError(response, file, fileList) {
                this.uploadingNums--;
                this.$message({
                    type: 'error',
                    showClose: true,
                    message: '文件上传异常！'
                });
            },
            uploadSuccess(response, file, fileList) {
                this.uploadingNums--;
                if (response.success) {
                    response.data.forEach((fileId) => {
                        this.fileIdMap[file] = fileId;
                        file.id = fileId;
                    });
                } else {
                    this.$message({
                        type: 'error',
                        showClose: true,
                        message: '文件上传失败，失败原因：' + response.msg
                    });
                    var temp = fileList.indexOf(file);
                    if (temp !== -1) {
                        fileList.splice(temp, 1);
                    }
                }
            },
            beforeUpload: function (file) {
                var maxSize = this.fileMaxSize * 1024 * 1024;
                if (maxSize > 0 && file.size > maxSize) {
                    this.$message({
                        type: 'warning',
                        showClose: true,
                        message: '文件大小不能超过' + this.fileMaxSize + 'M'
                    });
                    return false;
                }
                this.uploadingNums++;
                return true;
            },
            toDeleteFile(file) {
                this.$confirm('确定删除文件吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.delete(file);
                }).catch((err) => {
                    if (err !== 'cancel') {
                        this.$message({
                            type: 'error',
                            message: '删除文件失败！',
                            showClose: true
                        });
                        console.error(err);
                    }
                });
            },
            doDelete(file) {
                if(!this.fileIdMap[file]){
                    return;
                }
                axios.post(_contextPath + '/res/delete', this.fileIdMap[file], {
                    headers: {
                        "Content-Type": "application/json;charset=utf-8"
                    }
                }).then((resp) => {
                    if (resp && resp.data && resp.data.success) {
                        delete this.fileIdMap[file];
                    } else if (resp && resp.data && resp.data.msg) {
                        this.$notify({
                            type: 'error',
                            title: '操作失败',
                            showClose: true,
                            message: '删除资源失败，失败原因：' + resp.data.msg
                        });
                        console.error(resp);
                    } else {
                        this.$notify({
                            type: 'error',
                            title: '操作失败',
                            showClose: true,
                            message: '删除资源失败!'
                        });
                        console.error(resp);
                    }
                }).catch((err) => {
                    this.$notify({
                        type: 'error',
                        title: '操作失败',
                        showClose: true,
                        message: '删除资源失败!'
                    });
                    console.error(err);
                });
            }
        }
    });
</script>
