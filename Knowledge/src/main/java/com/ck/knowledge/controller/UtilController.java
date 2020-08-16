package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Get;
import com.ck.knowledge.utils.ApiGenerator;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("util")
public class UtilController {

    @Autowired
    private ApiGenerator apiGenerator;
    @Get("context/api/all")
    public String generateApi(HttpServletRequest request) throws IOException, TemplateException {
        return apiGenerator.generateAllFromContext(request);
    }
}
