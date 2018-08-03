package com.qf.collection;

//JVM会自动维护八种基本类型的常量池，int常量池中初始化-128~127的范围，
//所以当为Integer i=127时，在自动装箱过程中是取自常量池中的数值，
//而当Integer i=128时，128不在常量池范围内，
//所以在自动装箱过程中需new 128，所以地址不一样。
public class IntegerDemo {
	public static void main(String[] args) {
		Integer i = 127;
		Integer j = 127;
		if (i == j) {
			System.out.println("相等");
		} else {
			System.out.println("不等");
		}
		Integer a = 128;
		Integer b = 128;
		if (a == b) {
			System.out.println("相等");
		} else {
			System.out.println("不等");
		}
	}
}
