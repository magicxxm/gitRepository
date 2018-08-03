package com.jsk.test;

public class Person {
   private String name;
   private int age;
public Person() {
	super();
	// TODO 自动生成的构造函数存根
}
public Person(String name, int age) {
	super();
	this.name = name;
	this.age = age;
}
public void enjoy(Course c){
	System.out.println(name+"喜欢课程是："+c.getName()+"，课程编号是："+c.getId());
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
   
}
