package com.mushiny.comm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Tank.li on 2017/6/25.
 */
public class CommonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将Map转换成json
     *
     * @param data
     * @param data
     * @return
     */
    public static String mapToJSon(Map data) {
        String result = "";

        if (!(data == null || data.isEmpty())) {
            try {
                result = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 按格式转换时间
     *
     * @param format
     * @return
     */
    public static String now2String(String format) {
        LocalDateTime localDateTime = LocalDateTime.now();
        //yyyyMMddHHmmss
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        //System.out.println(df.format(localDateTime));
        return df.format(localDateTime);
    }

    public static String genUUID() {
        return UUID.randomUUID().toString();
    }
/*
    public static void main(String[] args) {
       *//* System.out.println(now2String("yyyyMMddHHmmss"));
        System.out.println(genUUID());*//*
        Map addr = new HashMap();
        addr.put("ADDRESSID","111");


        System.out.println(mapToJSon(addr));
    }*/

    /**
     * 行数据转换成对象 TODO 用javaassist
     *
     * @param tClass
     * @param data
     * @return
     */
    public static Object map2Bean(Class tClass, Map data) {
        Object instance = null;
        try {
            instance = tClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Field[] fields = tClass.getDeclaredFields();
        Map<String, Method> methodNames = new HashMap();
        Method[] methods = tClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            methodNames.put(method.getName().toUpperCase(), method);
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            //包含get/set方法
            if (methodNames.get("GET" + fieldName.toUpperCase()) != null
                    && methodNames.get("SET" + fieldName.toUpperCase()) != null
                    && data.get(fieldName.toUpperCase()) != null) {
                Method methodSet = methodNames.get("SET" + fieldName.toUpperCase());//获取set方法
                try {
                    methodSet.invoke(instance, data.get(fieldName.toUpperCase()));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return instance;
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }


}
