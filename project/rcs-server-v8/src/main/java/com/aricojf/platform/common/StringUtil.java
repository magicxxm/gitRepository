package com.aricojf.platform.common;

import com.mingchun.mu.util.ExceptionUtil;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public StringUtil() {
        // TODO Auto-generated constructor stub
    }
    
    //

    /**
     * 获得MD5散列值方法
     */
    public static String getMD5ofStr(String origString) {
        String origMD5 = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] result = md5.digest(origString.getBytes());
            origMD5 = byteArray2HexStr(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return origMD5;
    }

    /**
     * 处理字节数组得到MD5散列的方法
     */
    private static String byteArray2HexStr(byte[] bs) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bs) {
            sb.append(byte2HexStr(b));
        }
        return sb.toString();
    }

    /**
     * 字节标准移位转十六进制方法
     */
    private static String byte2HexStr(byte b) {
        String hexStr = null;
        int n = b;
        if (n < 0) {
            // 若需要自定义加密,请修改这个移位算法即可
            n = b & 0x7F + 128;
        }
        hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
        return hexStr.toUpperCase();
    }

    /**
     * MD5多次加密方法
     */
    public static String getMD5ofStr(String origString, int times) {
        String md5 = getMD5ofStr(origString);
        for (int i = 0; i < times - 1; i++) {
            md5 = getMD5ofStr(md5);
        }
        return getMD5ofStr(md5);
    }

    /**
     * 密码验证方法
     */
    public static boolean verifyPassword(String inputStr, String MD5Code) {
        return getMD5ofStr(inputStr).equals(MD5Code);
    }

    /**
     * 多次加密时的密码验证方法
     */
    public static boolean verifyPassword(String inputStr, String MD5Code,
            int times) {
        return getMD5ofStr(inputStr, times).equals(MD5Code);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E kk点mm分");
        return sdf.format(date);
    }
//=======================================================================
    //判断输入的字符串是否为整数

    public static boolean IsInt(String str) {
        if(str == null || "".equals(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //判断输入的字符串是否为浮点性

    public static boolean IsFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//=============================================================

    /**
     * ***************************************************************
     * 3.输入字符串，如：1234abc 返回1234和abc
     *     
*****************************************************************
     */
    public static ArrayList getStrNumCns(String str) {
        ArrayList li = new ArrayList();
        if (str != null & !"".equals(str)) {

            Matcher cs = Pattern.compile("\\D+").matcher(str);
            Matcher nm = Pattern.compile("\\d+").matcher(str);
            if (nm.find()) {
                li.add(nm.group(0));
            }
            if (cs.find()) {
                li.add(cs.group(0));

            }

        }
        return li;
    }

    //判断字符串是否只有”字母“和”数字“

    public static boolean IsCharAndInt(String str) {
        boolean isNumber = true; //定义一个boolean值，用来表示是否包含数字
        for (int i = 0; i < str.length(); i++) {//循环遍历字符串
            if (Character.isDigit(str.charAt(i))
                    || Pattern.compile("(?i)[a-z]").matcher(str).find()) {//用char包装类中的判断数字的方法判断每一个字符
                //isNumber=true; // 循环完毕以后，如果isNumber为true，则代表字符串中包含数字，否则不包含
            } else {
                isNumber = false;
            }
        }
        return isNumber;
    }

    //判断表示是否全为英文
    public static boolean strIsEnglish(String word) {
        boolean sign = true; // 初始化标志为为'true'
        for (int i = 0; i < word.length(); i++) {
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z')) {
                return false;
            }
        }
        return true;
    }
}
