"use strict";
//创建对象的基本方式（创建对象，然后进行属性赋值）
var person = new Object();
person.name='ck';
person.age=27;
person.job='coder';
person.sayhello=function(){
	alert('hello');
};

//字面量创建对象方式
var person2 = {
		name:'ck',
		age:27,
		job:'coder',
		sayhello:function(){
			alert('hello');
		}
};

/*
 * 1.对象的属性包括数据属性和访问器属性,相当于java的字段声明和get/set方法；
 * 2.属性有自己的特性；
 */

var person3 = {
		age:27,
		job:'coder',
		sayhello:function(){
			alert('hello');
		},
		foreach:function(){
			for(var i in this){
				alert(this[i]);
			}
		}
};

Object.defineProperty(person3,'name',{
	Configurable:true,	//是否可以对该属性进行配置（删除、设置特性、定义为访问器属性）
	Enumerable:false,	//是否可以通过for-in遍历该属性
	Writable:true,		//是否可以修改该属性的值（类似于final修饰）
	Value:'ck'			//相当于定义时的赋值
});

Object.defineProperty(person3,'name',{
	Configurable:false,
	Enumerable:true,
	Writable:true,
	Value:'ck'
});

delete person3.name;

Object.defineProperty(person3,'name',{
	Configurable:true,
	Enumerable:true,
	Writable:true,
	Value:'ck'
});

