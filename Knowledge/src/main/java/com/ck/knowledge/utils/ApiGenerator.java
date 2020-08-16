package com.ck.knowledge.utils;

import com.ck.knowledge.properties.CommonProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * api生成器（前台）
 */
@Service
public class ApiGenerator {

    @Autowired
    private CommonProperties commonProperties;

    public String generateAllFromContext(HttpServletRequest request) throws IOException, TemplateException {
        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        configuration.setDirectoryForTemplateLoading(new File(commonProperties.getTempDir()));
        Template template = configuration.getTemplate("api//api.ftl");
        Map<String, Object> dataMap = new HashMap<>();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            dataMap.clear();
            StringWriter sw = new StringWriter();
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            dataMap.put("mapUrl", info.getPatternsCondition().getPatterns().toArray()[0]);
            dataMap.put("funcName", method.getMethod().getName());
            if (!info.getMethodsCondition().getMethods().isEmpty()) {
                dataMap.put("method", info.getMethodsCondition().getMethods().toArray()[0]);
            }
            dataMap.put("methodNote", "保存");
            template.process(dataMap, sw);
            sb.append(sw.toString());
        }
       return sb.toString();
    }
}
