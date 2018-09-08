package com.mushiny.wms.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/20.
 */
public class JSONUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static JsonParser parser = JsonParserFactory.getJsonParser();

    public static Object toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
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

    /**
     * 将Map转换成json
     *
     * @param data
     * @param data
     * @return
     */
    public static String mapToJSon(Map data) {
        String result = "";

        if ((data != null) && (!data.isEmpty())) {
            try {

                result = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static String toJSon(Object object) {
        String result = "";
        if (!ObjectUtils.isEmpty(object)) {
            try {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                result = objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }

        return result;
    }

    public static List jsonToList(String json) {
        return parser.parseList(json);
    }

    public static <T>T mapToBean(Map<String,Object> param,T targetBean ){


        Set<String> t=param.keySet();
        for(String o:t)
        {
            Field f= ReflectionUtils.findField(targetBean.getClass(),o);

            if(!ObjectUtils.isEmpty(f))
            {
                ReflectionUtils.makeAccessible(f);
                Class type=f.getType();
                Object value=param.get(o);
                if(ObjectUtils.isEmpty(param.get(o)))
                {
                    continue;
                }
                if(BigDecimal.class == type||Integer.class==type)
                {
                    value= NumberUtils.parseNumber(""+param.get(o),type);
                }else if (Date.class==type)
                {
                    value = DateTimeUtil.strToDateLong(""+param.get(o));
                }
                ReflectionUtils.setField(f,targetBean,value);
            }




        }
        return targetBean;


    }

    public static <T>T mapToBean(Map<String,Object> param,T targetBean,String sourceField,String targetField ){


        Set<String> t=param.keySet();
        for(String o:t)
        {

            Field f=null;
            String field=o;
            if(!StringUtils.isEmpty(sourceField)&&!StringUtils.isEmpty(targetField)&&o.equalsIgnoreCase(sourceField))
            {
                field=targetField;
            }
            f= ReflectionUtils.findField(targetBean.getClass(),field);

            if(!ObjectUtils.isEmpty(f))
            {
                ReflectionUtils.makeAccessible(f);
                Class type=f.getType();
                Object value=param.get(o);
                if(BigDecimal.class == type)
                {
                    value= NumberUtils.parseNumber(""+param.get(o),type);
                }else if (Date.class==type)
                {
                    String date=(""+param.get(o)).replace('T',' ');

                    value = DateTimeUtil.strToDateLong(date);

                }
                ReflectionUtils.setField(f,targetBean,value);
            }

        }
        return targetBean;


    }
    public static Map jsonToMap(String object) {

            Map result = null;

            try {

                result = parser.parseMap(object);
            } catch (Exception e) {

                LOGGER.error("解析"+object+"\n"+e.getMessage(), e);
            }


        return result;
    }

    public static <T> T jsonToJavaBean(String jsonString,Class<T> targetClass)
    {
        T result = null;
        if (!StringUtils.isEmpty(jsonString)) {
            try {

                result = objectMapper.readValue(jsonString,targetClass);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }
        return result;

    }


   /* public static void main(String[] args) {
        try{

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";



            //json字符串转为Map对象
            Map map=mapper.readValue(jsonString, Map.class);
            System.out.println(map);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
*/
}
