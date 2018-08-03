package com.aricojf.util;



import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {


    /**
     * 字节（整型为正整数）以二进制字符串显示
     * @param b
     * @return
     */
    public static String byte2BinaryString(byte b){
        if(b < 0){
            return "";
        }
        String tmp = Integer.toBinaryString(b & 0xff);
        for(int i=0, len=tmp.length(); i<8-len; i++){
            tmp = "0"+tmp;
        }
        return tmp;
    }



    /*public static void main(String[] args) {
        *//*Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Tom");
        map.put("age", 12);
        System.out.println("----:"+map2JsonString(map));*//*



        byte a = 0x0a;

        System.out.println(byte2BinaryString(a));





    }*/









}
