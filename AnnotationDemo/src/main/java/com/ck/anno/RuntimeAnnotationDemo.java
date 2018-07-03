package com.ck.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title 运行期注解
 * @author Chengkai
 * @date 2018年7月3日
 * @time 下午7:30:54
 * @describe @Documented让被标记的注解能够记录在javadoc中
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RuntimeAnnotationDemo {
	
}
