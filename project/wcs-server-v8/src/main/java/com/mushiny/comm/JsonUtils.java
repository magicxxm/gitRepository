package com.mushiny.comm;

/**
 * Created by Tank.li on 2017/7/27.
 */

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String bean2Json(T bean) {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String map2Json(Map map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String list2Json(List list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T json2Bean(String json, Class<T> beanClass) {
        try {
            return objectMapper.readValue(json, beanClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> json2List(String json, Class<T> beanClass) {
        if(CommonUtils.isEmpty(json)){
            return new ArrayList<>();
        }
        try {
            return (List<T>) objectMapper.readValue(json, getCollectionType(List.class, beanClass));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map json2Map(String json) {
        if(CommonUtils.isEmpty(json)){
            return  new HashMap();
        }
        try {
            return (Map) objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static void main(String[] args) {
        Map data = new HashMap();
        data.put("sectionID","1");
        data.put("reCode", "1");
        data.put("reMsg", "succes");
        /*List<String> canwork = new ArrayList<>();
        List<String> cannotwork = new ArrayList<>();
        canwork.add("1");
        canwork.add("2");
        canwork.add("3");
        cannotwork.add("4");
        cannotwork.add("5");
        cannotwork.add("6");

        data.put("blockedAddressList",cannotwork);
        data.put("availableAddressList",canwork);*/

        String json = map2Json(data);
        System.out.println(json);

        System.out.println("===>"+Long.parseLong("P0000003".substring(1)));
    }
}
