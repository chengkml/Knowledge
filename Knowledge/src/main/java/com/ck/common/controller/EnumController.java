package com.ck.common.controller;

import com.ck.common.aop.EnumName;
import com.ck.common.aop.Get;
import com.ck.common.enums.EnumInf;
import com.ck.common.vo.EnumVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("enum/")
public class EnumController {

    private static final String ENUM_DIR = "classpath*:com/ck/*/enums/*.class";

    private static final Map<String, List<EnumVo>> enumMap = new HashMap<>();

    @Get("{name:.+}")
    public Object get(@PathVariable String name) {
        return enumMap.get(name);
    }

    @PostConstruct
    private void getEnum() throws IOException, ClassNotFoundException {
        ResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(loader);
        Resource[] resources = loader.getResources(ENUM_DIR);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                Class cls = Class.forName(className);
                Class<? extends EnumInf> m = cls;
                EnumName anno = m.getAnnotation(EnumName.class);
                if (anno != null && StringUtils.isNotBlank(anno.value())) {
                    EnumInf[] enumConstants = m.getEnumConstants();
                    List<EnumVo> enums = new ArrayList<>();
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
    }
}
