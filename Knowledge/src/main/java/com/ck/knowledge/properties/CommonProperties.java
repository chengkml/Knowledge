package com.ck.knowledge.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title: CommonProperties
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2019/10/14 14:50
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix="common")
public class CommonProperties {

    private String tempDir;
}