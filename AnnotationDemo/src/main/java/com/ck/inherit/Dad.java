package com.ck.inherit;

import com.ck.anno.ComplexAnnotationDemo;
import com.ck.anno.InheritAnnotationDemo;

/**
 * @title 父类
 * @author Chengkai
 * @date 2018年7月3日
 * @time 下午8:08:45
 * @describe
 */
@InheritAnnotationDemo
@ComplexAnnotationDemo
public class Dad {
	/**
	 * 私有的姓名
	 */
	private String name;
	/**
	 * 年龄
	 */
	private int age;
	
	@ComplexAnnotationDemo
	@Override
	public String toString() {
		return "Dad [name=" + name + ", age=" + age + "]";
	}
	
}
