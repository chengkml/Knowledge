package com.ck.knowledge.aop;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class GetAspect {

    private static Logger LOG = LoggerFactory.getLogger(GetAspect.class);

    @Around("@annotation(get)")
    public Object around(ProceedingJoinPoint point, Get get) {
        Map<String, Object> result = new HashMap<>();
        catchAndRecord(point, result, get.path());
        return result;
    }

    @Around("@annotation(post)")
    public Object around(ProceedingJoinPoint point, Post post) {
        Map<String, Object> result = new HashMap<>();
        catchAndRecord(point, result, post.path());
        return result;
    }

    private void catchAndRecord(ProceedingJoinPoint point, Map<String, Object> result, String[] path) {
        try {
            Long start = System.currentTimeMillis();
            Object res = point.proceed();
            if(path.length>0){
                LOG.info("请求“" + path[0] + "”耗时：{}ms", System.currentTimeMillis() - start);
            }else{
                LOG.info("请求耗时：{}ms", System.currentTimeMillis() - start);
            }
            result.put("success", true);
            result.put("data", res);
        } catch (Throwable e) {
            String rootMsg = ExceptionUtils.getRootCauseMessage(e);
            LOG.error(rootMsg, e);
            result.put("success", false);
            result.put("msg", rootMsg);
            result.put("stackTrace", ExceptionUtils.getStackTrace(e));
        }
    }
}
