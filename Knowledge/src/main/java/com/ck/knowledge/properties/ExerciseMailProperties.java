package com.ck.knowledge.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title: ExerciseMailProperties
 * @Author: Chengkai
 * @Date: 2019/9/19 13:41
 * @Version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix="exercise.mail")
public class ExerciseMailProperties {

    private String reciever;

    private String sender;
}
