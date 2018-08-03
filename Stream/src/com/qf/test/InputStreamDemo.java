package com.qf.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamDemo {
public static void main(String[] args) {
	InputStream input=null;
	try {
		input=new FileInputStream("E:"+File.separator+"a.txt");
		byte[] b=new byte[1024];
		int len=input.read(b);
		System.out.println(new String(b,4,len));
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  }
}
