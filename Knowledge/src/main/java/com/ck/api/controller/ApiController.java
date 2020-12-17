package com.ck.api.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.api.service.ApiService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Api("api接口")
@RestController
@RequestMapping("api")
public class ApiController {

    @Autowired
    private ApiService apiGenerator;

    @ApiOperation("将Context中所有服务生成js方法")
    @Get("context/all")
    public Object generateApi(HttpServletRequest request) throws IOException, TemplateException {
        return apiGenerator.generateAllFromContext(request);
    }

    @ApiOperation("指定Context中某个服务生成js方法")
    @Get("context/class")
    public Object generateBeanApi(@RequestParam("className") String className, HttpServletRequest request) throws IOException, TemplateException {
        return apiGenerator.generateBeanFromContext(className, request);
    }

    @ApiOperation("指定Context中某个服务的某个方法生成js方法")
    @Get("context/method")
    public Object generateBeanMethodApi(@RequestParam("className") String className, @RequestParam("methodName") String methodName, HttpServletRequest request) throws IOException, TemplateException {
        return apiGenerator.generateBeanMethodFromContext(className, methodName, request);
    }

    @ApiOperation("获取Context中服务树")
    @Get("context/apiTree")
    public Object getApiTree(HttpServletRequest request) {
        return apiGenerator.getApiTree(request);
    }

    @ApiOperation("批量指定Context中服务的方法生成js方法")
    @Post("context/selected")
    public Object generateApiSelected(@RequestBody List<String> nodeKeys, HttpServletRequest request) throws IOException, TemplateException {
        return apiGenerator.generateApiSelected(nodeKeys, request);
    }

    @ApiOperation("通过输入信息生成js方法")
    @Get("input")
    public Object generateInputApi(@RequestParam("url") String url, @RequestParam("name") String name, @RequestParam("method") String method, @RequestParam("note") String note) throws IOException, TemplateException {
        return apiGenerator.generateApiByInput(url, name, method, note);
    }
}
