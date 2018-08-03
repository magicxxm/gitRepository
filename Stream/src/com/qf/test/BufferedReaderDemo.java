package com.qf.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 获取键盘输入
 */
public class BufferedReaderDemo {

	public static void main(String[] args) {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入：");
        try {
			String str=br.readLine();
			System.out.println("您输入的内容是："+str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
