package com.qf.collection;

/**
 * 递归算法100以内的和
 * 
 * @author Administrator
 *
 */
public class DiGuiDemo {
	public static void main(String[] args) {
//		System.out.println(DiGuiDemo.add(100));
		System.out.println("总高度是："+DiGuiDemo.add2(3));
	}

	public static int add(int num) {
		if (num == 1) {
			return 1;
		}
		return num + add(num - 1);
	}	
	
	//从100米高空中落下，每次弹起为上一次的一半，num为弹跳次数
	public static double add2(int num){
		double h=100;
		double sum=100;
		for(int i=1;i<=num;i++){
			h=h/2;
			sum+=h;			
		}
		System.out.println("第3次高度是:"+h);
		return sum;	
	}
}
