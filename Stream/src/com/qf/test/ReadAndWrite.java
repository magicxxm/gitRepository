package com.qf.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadAndWrite {

	public static void main(String[] args) {
	   InputStream input=null;
	   OutputStream out=null;
	   try {
		input=new FileInputStream("E:"+File.separator+"a.txt");
		out=new FileOutputStream("E:"+File.separator+"b.txt");
		byte [] b=new byte[1024];
		int len=0;
		while((len=input.read(b))!=-1){
			out.write(b,0,len);			
		}
		System.out.println("–¥»Î≥…π¶");
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		try {
			out.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	}
}
