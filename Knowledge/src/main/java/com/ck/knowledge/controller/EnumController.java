package com.ck.knowledge.controller;

import com.ck.knowledge.aop.EnumName;
import com.ck.knowledge.aop.Get;
import com.ck.knowledge.enums.EnumInf;
import com.ck.knowledge.vo.EnumVo;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("enum/")
public class EnumController {

    private static final String ENUM_DIR = "com.ck.knowledge.enums";

    @Get("{name:.+}")
    public Object get(@PathVariable String name) {
        return getEnum(name);
    }

    private List<EnumVo> getEnum(String name) {
        List<EnumVo> enums = new ArrayList<>();
        Reflections reflections = new Reflections(ENUM_DIR);
        Set<Class<? extends EnumInf>> monitorClasses = reflections.getSubTypesOf(EnumInf.class);
        Iterator<Class<? extends EnumInf>> it = monitorClasses.iterator();
        while (it.hasNext()) {
            Class<? extends EnumInf> m = it.next();
            EnumName anno = m.getAnnotation(EnumName.class);
            if (anno != null && name.equals(anno.value())) {
                EnumInf[] enumConstants = m.getEnumConstants();
                for (EnumInf anEnum : enumConstants) {
                    EnumVo vo = new EnumVo();
                    vo.setLabel(anEnum.getLabel());
                    vo.setValue(anEnum.getValue());
                    enums.add(vo);
                }
            }
        }
        return enums;
    }
}
