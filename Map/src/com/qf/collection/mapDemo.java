package com.qf.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class mapDemo {

	public static void main(String[] args) {
		Map<String,String> map=new HashMap<>();
		map.put("马云","10086");
		map.put("马化腾","12306");
		map.put("李彦宏","45678");
		//方法一：使用map的EntrySet()方法遍历
		Set<Map.Entry<String,String>> set=map.entrySet();
		for (Map.Entry<String, String> me : set) {
			System.out.println(me.getKey()+"="+me.getValue());
		}
		System.out.println("*****");
		//方法二：通过keySet()返回集合的iterator遍历
		Iterator<String>iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			System.out.println(key+"="+map.get(key));
		}
		System.out.println("############");
		//方法三：使用keySet()方法遍历
		for ( String key : map.keySet()) {
			System.out.println(key+"="+map.get(key));
		}
		//遍历value值
		for (String value:map.values()) {
			System.out.println(value);
		}
	}
}
