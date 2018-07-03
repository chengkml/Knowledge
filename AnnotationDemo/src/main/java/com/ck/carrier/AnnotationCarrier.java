package com.ck.carrier;

import com.ck.anno.ClassAnnotationDemo;
import com.ck.anno.MethodAnnotationDemo;
import com.ck.anno.RuntimeAnnotationDemo;
import com.ck.anno.TypeAnnotationDemo;
/**
 * @title 注解载体
 * @author Chengkai
 * @date 2018年7月3日
 * @time 下午7:32:00
 * @describe
 */
@RuntimeAnnotationDemo
@TypeAnnotationDemo
public class AnnotationCarrier {
	@ClassAnnotationDemo
	@MethodAnnotationDemo
	public void testMethod() {
		
	}
}
