package com.ck.knowledge.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title: NamingProperties
 * @Description: 命名相关属性
 * @Author: Chengkai
 * @Date: 2019/9/21 9:01
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "knowledge.naming")
public class NamingProperties {

    private String exercisePrefix;
}
