package com.qf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class BufferedReaderDemo2 {

	public static void main(String[] args) {
		File f=new File("E:"+File.separator+"a.txt");
		Reader r=null;
		BufferedReader br=null;			
		try {
			r=new FileReader(f);
			br=new BufferedReader(r);
			String line="";
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
