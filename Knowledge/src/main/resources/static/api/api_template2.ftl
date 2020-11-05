${funcName}:function(func,errFunc){
    var _self = this;
    axios.<#if method??&&method=='get'>get<#elseif method??&&method=='post'>post<#else>get</#if>(_contextPath + '${mapUrl}').then( function(resp) {
        if(resp&&resp.data&&resp.data.success){
            <#if method??&&method=='post'>
            _self.$notify({
                type:'success',
                title:'操作成功',
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>成功!'
            });
            </#if>
            if(func&&func instanceOf Function){
                func(resp.data.data);
            }
        }else if(resp&&resp.data&&resp.data.msg){
            _self.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
                type:'error',<#if method??&&method=='post'>
                title:'操作失败',</#if>
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败，失败原因：'+resp.data.msg
            });
            if(errFunc&&errFunc instanceOf Function){
                errFunc();
            }
            console.error(resp.data.stackTrace);
        }else{
            _self.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
                type:'error',<#if method??&&method=='post'>
                title:'操作失败',</#if>
                showClose:true,
                message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败!'
            });
            if(errFunc&&errFunc instanceOf Function){
                errFunc();
            }
            console.error(resp);
        }
    }).catch(function(err){
        _self.$<#if method??&&method=='get'>message<#elseif method??&&method=='post'>notify<#else>message</#if>({
            type:'error',<#if method??&&method=='post'>
            title:'操作失败',</#if>
            showClose:true,
            message:'<#if methodNote??>${methodNote}<#else>操作</#if>失败!'
        });
        if(errFunc&&errFunc instanceOf Function){
            errFunc();
        }
        console.error(err);
    });
},
