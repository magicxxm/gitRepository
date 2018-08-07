package com.mushiny.utils;

/**
 * Created by 123 on 2018/2/23.
 * 商品类型转换
 */
public class ItemUtil {

    /**
     * 转换商品分组
     * @param type
     * @return
     */
    public static String changeGroup(String type){
        String itemGroup  = "百货";
        switch (type){
            case "A":itemGroup = "食品";break;
            case "B":itemGroup = "电器";break;
            case "C":itemGroup = "危险品";break;
            case "D":itemGroup = "图书";break;
            case "E":itemGroup = "高价品";break;
        }

        return itemGroup;
    }

    /**
     * 商品单位转换
     * @param s
     * @return
     */
    public static String changeUnit(String s){
        String unit = "单品";
        switch (s){
            case "件":unit = "单品";break;
            case "个":unit = "单品";break;
        }
        return unit;
    }

    /**
     * 商品有效期管理转换,, 到期日EXPIRATION,  生产日期MANUFACTURE
     * @param s
     * @return
     */
    public static String changeLot(int shelflife){
        String lot = "";
        if(shelflife > 0){
            lot = "EXPIRATION";
        }
        return lot;
    }
    /**
     * 商品序列号管理转换 NO_RECORD不记录，，，ALWAYS_RECORD记录
     */
    public static String changeSerialNo(String s){
        String serialNoType = "NO_RECORD";
        switch (s){
            case "0":serialNoType = "NO_RECORD";break;
            case "1":serialNoType = "ALWAYS_RECORD";break;
        }
        return serialNoType;
    }
}
