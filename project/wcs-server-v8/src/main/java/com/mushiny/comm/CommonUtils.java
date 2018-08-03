package com.mushiny.comm;

import com.mushiny.beans.Address;
import com.mushiny.business.RobotManager;
import javassist.ClassPool;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by Tank.li on 2017/6/25.
 */
@Component
public class CommonUtils {
    private final static Logger logger = LoggerFactory.getLogger(RobotManager.class);
    @Value("${pod.correctTheta}")
    public void setTheta(String theta) {
        podCorrectTheta = Integer.parseInt(theta);
    }

    public static int podCorrectTheta;

    public static String face2WorkStation(int podFace, int wsFace){
        //现在工作站的朝向相当于POD的面
        int[] fourFace = {podFace,(podFace+270)%360,(podFace+180)%360,(podFace+90)%360};
        int index = 0;
        for (int i = 0; i < fourFace.length; i++) {
            int face = fourFace[i];
            if(wsFace == face){
                index = i;
                break;
            }
        }
        index = (index+2)%4;
        return "ABCD".charAt(index)+"";
    }

    public static int aFaceToward(int podFace,int wsFace,String needFace){
        /*String curFace2Ws = face2WorkStation(podFace,wsFace);
        //System.out.println("当前面对工作站:"+curFace2Ws +" 需要:"+needFace);
        int diff = "ADCB".indexOf(curFace2Ws) - "ADCB".indexOf(needFace);
        //System.out.println("顺时针相差:"+diff);
        int re = (diff*90 + podFace + 360)%360;
        return (re + 180)%360;*/
        int correct = 0;
        if("A".equals(needFace)){
            correct = (wsFace+180)%360;
        }
        if("B".equals(needFace)){
            //correct = (wsFace+90)%360;    纠偏角度为180的算法
            //correct = (wsFace+270)%360;   纠偏角度为0的算法
            correct = (wsFace+(270+podCorrectTheta)%360)%360;   //兼容纠偏
        }
        if("C".equals(needFace)){
            correct = wsFace%360;
        }
        if("D".equals(needFace)){
            //correct = (wsFace+270)%360;   纠偏角度为180的算法
            //correct = (wsFace+90)%360;    纠偏角度为0的算法
            correct = (wsFace+(90+podCorrectTheta)%360)%360;    //兼容纠偏
        }
        logger.debug("计算旋转角度纠偏角度podCorrectTheta="+podCorrectTheta);
        return (correct + podCorrectTheta)%360;//不知道是什么脑子写出这种垃圾代码
    }



    /**
     * 按格式转换时间
     * @param format
     * @return
     */
    public static String now2String(String format){
        LocalDateTime localDateTime = LocalDateTime.now();
        //yyyyMMddHHmmss
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        //System.out.println(df.format(localDateTime));
        return df.format(localDateTime);
    }



    /**
     * 把日期类型格式化成字符串
     * @param date
     * @param format
     * @return
     */
    public static String convert2String(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.format(date);
        } catch (Exception e) {
            return null;
        }
    }



    public static String genUUID(){
       return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
       /* System.out.println(now2String("yyyyMMddHHmmss"));
        System.out.println(genUUID());*/
        /*System.out.println(aFaceToward(0,0,"A"));
        System.out.println(aFaceToward(0,0,"B"));
        System.out.println(aFaceToward(0,0,"C"));
        System.out.println(aFaceToward(0,0,"D"));*/
        Random random = new Random();
        int i = 0;
        /*while (i<1000) {
            System.out.println(random.nextInt(100));
            i++;
        }*/
        Long ss = Long.parseLong("0");
        System.out.println(ss==0);

       /* Map addr = new HashMap();
        addr.put("ADDRESSID","111");*/

        //Address address = (Address) map2Bean(Address.class,addr);
        //System.out.println(aFaceToward(90,0,"D"));

       /* System.out.println(convert2String(new Date(System.currentTimeMillis()),"YYYY-MM-dd HH:mm:ss"));

        String ss = loadFromFile("lisi.txt");//"Kleannara/可绿纳乐-\"天然纯棉系列\"天然卫生巾（日用加长）260mm *16片";
        System.out.println(ss);
        ss = ss.replaceAll("\"","\\\\\\\"");
        System.out.println(ss);*/

    }

    private static String loadFromFile(String s) {
        InputStream inputStream = CommonUtils.class.getResourceAsStream("/lisi.txt");
        Reader reader = new InputStreamReader(inputStream);
        int tempchar;
        StringBuffer sb = new StringBuffer();
        try {
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    sb.append((char) tempchar);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 行数据转换成对象 TODO 用javaassist
     * @param tClass
     * @param data
     * @return
     */
    public static Object map2Bean(Class tClass, Map data){
        Object instance = null;
        try {
            instance = tClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Field[] fields = tClass.getDeclaredFields();
        Map<String,Method> methodNames = new HashMap();
        Method[] methods = tClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            methodNames.put(method.getName().toUpperCase(),method);
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            //包含get/set方法
            if(methodNames.get("GET"+fieldName.toUpperCase())!=null
                    && methodNames.get("SET"+fieldName.toUpperCase())!=null
                    && data.get(fieldName.toUpperCase())!=null){
                Method methodSet = methodNames.get("SET"+fieldName.toUpperCase());//获取set方法
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
     * @param obj
     * @return
     */
    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
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


    public static void genUselessInfo(Map record) {
        //record.put("ID",genUUID());
        record.put("CREATED_DATE",new Timestamp(System.currentTimeMillis()));
        record.put("CREATED_BY","SYSTEM");
        /*record.put("CLIENT_ID","SYSTEM");*/
        record.put("WAREHOUSE_ID","JN1");
        record.put("ENTITY_LOCK",0);
        record.put("VERSION",0);
    }

    public static void modifyUselessInfo(Map record) {
        record.put("MODIFIED_BY","SYSTEM");
        record.put("MODIFIED_DATE",new Timestamp(System.currentTimeMillis()));
    }

    public static Long parseLong(String key, Map data) {
        Object value = data.get(key);
        if(value == null){
            return 0l;
        }
        return Long.parseLong(""+data.get(key));
    }

    public static Integer parseInteger(String key, Map data) {
        Object value = data.get(key);
        if(value == null){
            return 0;
        }
        return (new Double(value+"")).intValue();
    }

    public static String parseString(String key, Map data) {
        Object value = data.get(key);
        if(value == null){
            return null;
        }
        return value.toString();
    }

    public static double parseDouble(String key, Map data) {
        Object value = data.get(key);
        if(value == null){
            return 0.0;
        }
        return Double.parseDouble(value.toString()+"");
    }

    public static boolean parseBoolean(String key, Map map) {
        Object value = map.get(key);
        if(value == null){
            return false;
        }
        if(value.toString().equals("1")){
            return true;
        }
        return Boolean.parseBoolean(value.toString()+"");
    }


    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static short parseShort(String key, Map data) {
        Object value = data.get(key);
        if(value == null){
            return 0;
        }
        return (new Double(value+"")).shortValue();
    }

    public static Integer long2Int(Long srcAddr) {
        return (new Double(srcAddr+"")).intValue();
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String formatDate(Date date) {
        return CommonUtils.convert2String(date,"YYYY-MM-dd HH:mm:ss");
    }
}
