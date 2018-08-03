package mq;


import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    /**
     * 将json字符串转换为map对象
     *
     * 需要加入依赖：
     <dependency>
         <groupId>net.sf.json-lib</groupId>
         <artifactId>json-lib</artifactId>
         <version>2.4</version>
         <classifier>jdk15</classifier>
     </dependency>
     *
     * @param jsonString json字符串
     * @return Map<String, Object>对象
     */
    public static Map<String, Object> jsonString2Map(String jsonString){
        if("".equals(jsonString.trim())){
            return null;
        }
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = JSONObject.fromObject(jsonString);
            return jsonMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String map2JsonString(Map<String, Object> map){
        if(map == null){
            return "";
        }
        String res = "";
        try {
            res = String.valueOf(JSONObject.fromObject(map));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static Map<String, Object> object2Map(Object bean){
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Class beanClazz = bean.getClass();
            while (beanClazz != Object.class){
                Field[] fields = beanClazz.getDeclaredFields();
                for(Field field: fields){
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(bean));
                }
                beanClazz = beanClazz.getSuperclass();
            }
            return map;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    /**
     * 将重量转换为重量等级
     * @param weight
     * @return
     */
    public static int getWeightClassByWeight(float weight){
        if(weight <= 0){
            return 0;
        }
        return (int)(Math.floor(weight / 100) + 1);
    }

    /**
     * 将对象转整数，除整形的最小数之外
     * @param obj
     * @return
     */
    public static int object2Int(Object obj){
        if(obj == null){
            return Integer.MIN_VALUE;
        }
        int res = Integer.MIN_VALUE;
        try {
            res = Integer.parseInt(String.valueOf(obj));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        /*Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Tom");
        map.put("age", 12);
        System.out.println("----:"+map2JsonString(map));*/



//        byte a = 0x0a;

//        System.out.println(byte2BinaryString(a));

        System.out.println("重量等级--》"+getWeightClassByWeight(499));



    }












}
