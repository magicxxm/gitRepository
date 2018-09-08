package test.com.mushiny.wms.application.common.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by mingchun.mu@mushiny.com on 2018/7/7 0007.
 */
public class CommonUtil {
    private static Logger LOG = LoggerFactory.getLogger(CommonUtil.class.getName());
    /**
     * 判断num是否为正整数
     * @param num
     * @return boolean
     */
    public static boolean isPositiveInteger(Integer num){
        if(num > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * 如果是美的的接口，用此方法将其转换
     *
     * midea 的字符串转成可以变成javabean的字符串
     * @param jsonStr
     * @return
     */
    public static String mideaParamJsonModify(String jsonStr){
        try {
            Map<String, Object> map = JSONUtil.jsonToMap(jsonStr);
            Map<String, Object> resMap = getStringObjectMap(map);
            return JSONUtil.mapToJSon(resMap);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(" 异常字符串：" + jsonStr);
            LOG.error(" json字符串转换异常。。。", e );
        }
        return "";
    }

    private static Map<String, Object> getStringObjectMap(Map<String, Object> map) {
        Map<String, Object> resMap = new HashMap<>();
        map.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String s, Object o) {
                String key = s;
                if(s.indexOf("_") != -1){
                    String start = s.substring(0, s.indexOf("_"));
                    String end = s.substring(s.indexOf("_"));
                    key = start.toLowerCase() + end;
                }else{
                    key = s.toLowerCase();
                }
                resMap.put(key, o);
            }
        });
        return resMap;
    }

    /**
     *
     * 如果是美的的接口，用此方法将其转换
     *
     * midea 的字符串转成可以变成javabean的字符串
     * @param jsonStr
     * @return
     */
    public static <T> List<T> mideaListParamJsonModify(String jsonStr, Class<T> targetClass){
        try {
            LinkedList<T> resList = new LinkedList<T>();
            List<Map<String, Object>> mapList = JSONUtil.jsonToList(jsonStr);
            mapList.forEach(new Consumer<Map<String, Object>>() {
                @Override
                public void accept(Map<String, Object> stringObjectMap) {
                    String s = mideaParamJsonModify(JSONUtil.mapToJSon(stringObjectMap));
                    Class<T> c = targetClass;
                    T bean = JSONUtil.jsonToJavaBean(s, c);
                    resList.add(bean);
                }
            });
            return resList;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(" 异常字符串：" + jsonStr);
            LOG.error(" json字符串转换异常。。。", e );
        }
        return null;
    }

    /**
     * 日期格式化为字符串
     * @param date
     * @return
     */
    public static String dateFormat(Date date){
        if(date == null){
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /***
    *
    * @Author: mingchun.mu@mushiy.com
    */
    public static boolean isEmpty(String str){
        if(str == null){
            return true;
        }
        if("".equals(str.trim())){
            return true;
        }
        return false;
    }


    public static List<Integer> string2List(String string, String split){
        if(string == null){
            return null;
        }
        if("".equals(string.trim())){
            return null;
        }
        LinkedList<Integer> res = new LinkedList<>();
        String[] strs = string.split(split);
        for(int i = 0, len = strs.length; i < len; i++){
            res.add(Integer.parseInt(strs[i]));
        }
        return res;
    }
    public static List<Integer> string2List(String string){
        return string2List(string, ",");
    }


}
