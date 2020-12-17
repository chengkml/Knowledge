package com.ck.common.controller;

import com.ck.common.aop.EnumName;
import com.ck.common.aop.Get;
import com.ck.common.enums.EnumInf;
import com.ck.common.vo.EnumVo;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("enum/")
public class EnumController {

    private static final String ENUM_DIR = "com/ck/*/enums";

    private static final Map<String, List<EnumVo>> enumMap = new HashMap<>();

    @Get("{name:.+}")
    public Object get(@PathVariable String name) throws IOException {
        return enumMap.get(name);
    }

    @PostConstruct
    private List<EnumVo> getEnum() throws IOException {
        List<EnumVo> enums = new ArrayList<>();
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] resources = loader.getResources(ENUM_DIR);
        for (Resource res : resources) {
            String path = res.getFile().getPath();
            Reflections reflections = new Reflections(path.substring(path.lastIndexOf("com")).replaceAll("\\\\", "."));
            Set<Class<? extends EnumInf>> monitorClasses = reflections.getSubTypesOf(EnumInf.class);
            Iterator<Class<? extends EnumInf>> it = monitorClasses.iterator();
            while (it.hasNext()) {
                Class<? extends EnumInf> m = it.next();
                EnumName anno = m.getAnnotation(EnumName.class);
                if (anno != null && StringUtils.isNotBlank(anno.value())) {
                    EnumInf[] enumConstants = m.getEnumConstants();
                    for (EnumInf anEnum : enumConstants) {
                        EnumVo vo = new EnumVo();
                        vo.setLabel(anEnum.getLabel());
                        vo.setValue(anEnum.getValue());
                        enums.add(vo);
                    }
                    enumMap.put(anno.value(), enums);
                }
            }
        }
        return enums;
    }
}
