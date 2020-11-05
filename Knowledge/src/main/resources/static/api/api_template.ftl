${funcName}(){
    axios.<#if method??&&method=='get'>get<#elseif method??&&method=='post'>post<#else>get</#if>(_contextPath + '${mapUrl}').then( (resp)=> {
        if(resp&&resp.data&&resp.data.success){
            <#if method??&&method=='post'>
                this.$notify({
                type:'success',
                title:'操作成功',
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>成功!'
                });
            </#if>
        }else if(resp&&resp.data&&resp.data.msg){
            this.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
                type:'error',<#if method??&&method=='post'>
                title:'操作失败',</#if>
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败，失败原因：'+resp.data.msg
            });
            console.error(resp.data.stackTrace);
        }else{
            this.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
                type:'error',<#if method??&&method=='post'>
                title:'操作失败',</#if>
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败!'
            });
            console.error(resp);
        }
    }).catch((err)=>{
            this.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
                type:'error',<#if method??&&method=='post'>
                title:'操作失败',</#if>
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败!'
            });
            console.error(err);
    });
},
