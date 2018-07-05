package com.ck.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @title 复合作用域注解
 * @author Chengkai
 * @date 2018年7月4日
 * @time 上午9:17:14
 * @describe
 */
@Target(value= {ElementType.TYPE,ElementType.METHOD})
public @interface ComplexAnnotationDemo {

}	
