package com.ck.knowledge.service;

import com.ck.knowledge.util.TemplateHelper;
import com.ck.knowledge.vo.TreeNodeVo;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * api生成器（前台）
 */
@Service
public class ApiService {

    private static final List<String> EXCEPT_CONTROLLER = Arrays.asList(new String[]{"BasicErrorController", "Swagger2Controller", "ApiResourceController"});

    public String generateAllFromContext(HttpServletRequest request) throws IOException, TemplateException {
        Map<RequestMappingInfo, HandlerMethod> map = getHandlerMap(request);
        Template template = TemplateHelper.getTemplate(TemplateHelper.MY_TPL);
        Map<String, Object> dataMap = new HashMap<>();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            dataMap.clear();
            StringWriter sw = new StringWriter();
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            loadTplData(dataMap, info, method);
            template.process(dataMap, sw);
            sb.append(sw.toString());
        }
        return sb.toString();
    }

    public String generateBeanFromContext(String className, HttpServletRequest request) throws IOException, TemplateException {
        Map<RequestMappingInfo, HandlerMethod> map = getHandlerMap(request);
        Template template = TemplateHelper.getTemplate(TemplateHelper.MY_TPL);
        Map<String, Object> dataMap = new HashMap<>();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            dataMap.clear();
            StringWriter sw = new StringWriter();
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            if (method.getBeanType() != null && method.getBeanType().getName().endsWith(className)) {
                loadTplData(dataMap, info, method);
                template.process(dataMap, sw);
                sb.append(sw.toString());
            }
        }
        return sb.toString();
    }

    private void loadTplData(Map<String, Object> dataMap, RequestMappingInfo info, HandlerMethod method) {
        dataMap.put("mapUrl", info.getPatternsCondition().getPatterns().toArray()[0]);
        dataMap.put("funcName", method.getMethod().getName());
        if (!info.getMethodsCondition().getMethods().isEmpty()) {
            dataMap.put("method", info.getMethodsCondition().getMethods().toArray()[0].toString().toLowerCase());
        }
        ApiOperation apiOperation = method.getMethod().getAnnotation(ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.value())) {
            dataMap.put("methodNote", apiOperation.value());
        }
    }

    public Object generateBeanMethodFromContext(String className, String methodName, HttpServletRequest request) throws IOException, TemplateException {
        Map<RequestMappingInfo, HandlerMethod> map = getHandlerMap(request);
        Template template = TemplateHelper.getTemplate(TemplateHelper.MY_TPL);
        Map<String, Object> dataMap = new HashMap<>();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            dataMap.clear();
            StringWriter sw = new StringWriter();
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            if (method.getBeanType() != null && method.getBeanType().getName().endsWith(className) && methodName.equals(method.getMethod().getName())) {
                loadTplData(dataMap, info, method);
                template.process(dataMap, sw);
                sb.append(sw.toString());
            }
        }
        return sb.toString();
    }

    private Map<RequestMappingInfo, HandlerMethod> getHandlerMap(HttpServletRequest request) {
        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        return rmhp.getHandlerMethods();
    }

    public List<TreeNodeVo> getApiTree(HttpServletRequest request) {
        Map<String, TreeNodeVo> resMap = new HashMap<>();
        Map<RequestMappingInfo, HandlerMethod> map = getHandlerMap(request);
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            String className = method.getMethod().getDeclaringClass().getName();
            TreeNodeVo methodVo = new TreeNodeVo();
            methodVo.setId(className + "." + method.getMethod().getName());
            methodVo.setValue(method.getMethod().getName());
            methodVo.setLabel(info.getPatternsCondition().getPatterns().toArray()[0].toString());
            if (EXCEPT_CONTROLLER.contains(className.substring(className.lastIndexOf(".") + 1))) {
                continue;
            }
            resMap.computeIfAbsent(className, key -> {
                TreeNodeVo classVo = new TreeNodeVo();
                classVo.setId(className);
                classVo.setValue(className);
                classVo.setLabel(className.substring(className.lastIndexOf(".") + 1));
                classVo.setChildren(new ArrayList<>());
                return classVo;
            }).getChildren().add(methodVo);
        }
        return new ArrayList<>(resMap.values());
    }

    public String generateApiSelected(List<String> nodeKeys, HttpServletRequest request) throws IOException, TemplateException {
        Map<String, List<String>> methodMap = new HashMap<>();
        nodeKeys.forEach(key -> {
            String className = key.substring(0, key.lastIndexOf("."));
            String methodName = key.substring(key.lastIndexOf(".") + 1);
            methodMap.computeIfAbsent(className, k -> new ArrayList<>()).add(methodName);
        });
        Map<RequestMappingInfo, HandlerMethod> map = getHandlerMap(request);
        Template template = TemplateHelper.getTemplate(TemplateHelper.MY_TPL);
        Map<String, Object> dataMap = new HashMap<>();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> it = map.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            dataMap.clear();
            StringWriter sw = new StringWriter();
            Map.Entry<RequestMappingInfo, HandlerMethod> e = it.next();
            RequestMappingInfo info = e.getKey();
            HandlerMethod method = e.getValue();
            String className = method.getMethod().getDeclaringClass().getName();
            if (!methodMap.containsKey(className) || !methodMap.get(className).contains(method.getMethod().getName())) {
                continue;
            }
            loadTplData(dataMap, info, method);
            template.process(dataMap, sw);
            sb.append(sw.toString());
        }
        return sb.toString();
    }

    public String generateApiByInput(String url, String name, String method, String note) throws IOException, TemplateException {
        Template template = TemplateHelper.getTemplate(TemplateHelper.MY_TPL);
        StringWriter sw = new StringWriter();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("mapUrl", url);
        dataMap.put("funcName", name);
        dataMap.put("method", method);
        dataMap.put("methodNote", note);
        template.process(dataMap, sw);
        return sw.toString();
    }

}
