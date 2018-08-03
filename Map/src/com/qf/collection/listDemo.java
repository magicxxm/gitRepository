package com.qf.collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class listDemo {
  public static void main(String[] args) {
	List<String> li=new ArrayList<>();
	li.add("a");
	li.add("b");
	for (String str : li) {
		System.out.println(str);
	}
	for (int i = 0; i < li.size(); i++) {
		System.out.println(li.get(i));		
	}
	System.out.println("****************");
	Iterator<String> it=li.iterator();
	while(it.hasNext()){
		System.out.println(it.next());
	}
   }
  List<String> list=Arrays.asList("apple","water");
  int [] d = {2,8,9,1};
	//从小到大排序 
}
