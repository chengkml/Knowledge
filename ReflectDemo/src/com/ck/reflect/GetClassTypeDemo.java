package com.ck.reflect;

public class GetClassTypeDemo {
	public static void main(String[] args) {
		Foo foo = new Foo();
		Class<?> cl1 = foo.getClass();
		Class<?> cl2 = Foo.class;
		Class<?> cl3 = null;
		try {
			cl3 = Class.forName("com.ck.reflect.Foo");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(cl1==cl2);
		System.out.println(cl1==cl3);
	}
}	

class Foo{}