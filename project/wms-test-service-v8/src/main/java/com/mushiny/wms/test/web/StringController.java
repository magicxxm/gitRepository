package com.mushiny.wms.test.web;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Laptop-9 on 2018/11/19.
 */
public class StringController {

    public static void main(String[] args) {
        int  a =123;
        //1.格式化一个整数,位数不够向前补0
        System.out.print(String.format("%07d",a)+"\n"); //整数长度是7，不足7位前面补0
        //整数长度是7，不足7位填充空格
        System.out.println(String.format("% 7d",a));
        //2.格式化一个浮点数,小数位不够向后补0
        System.out.println(String.format("%.3f",new BigDecimal(123.1)));
        //3.格式化一个浮点数
        System.out.println(String.format("%.5f", 123.123451)); // 整数部分全部显示，小数部分后面保留5位小数 四舍五入
        System.out.println(String.format("%tF", new Date()));    //2018-03-04
        System.out.println(String.format("%tT", new Date()));     //15:59:31
        System.out.println(String.format("%tm", new Date())); //输出两位数字表示的月份，不够位自动补零11
        System.out.println(String.format("%tF %tT ", new Date(), new Date()));

    }
}
