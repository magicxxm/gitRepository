package com.mushiny.utils;

/**
 * Created by 123 on 2018/2/22.
 */
public class ContainerTypeUtil {

    /**
     * 静态方法将苏宁的容器类型转换成牧星系统中的容器类型
     */
    public static String revertType(String containerType){
        String type = "";
        switch (containerType){
            case "0":type = "料箱";break;
            case "1":type = "托盘";break;
            case "2":type = "料箱";break;
        }
        return type;
    }

    /**
     * 静态方法将苏宁的容器类型转换成牧星系统中的容器类型
     */
    public static String getBox(String containerType){
        String type = "Box";
        switch (containerType){
            case "0":type = "Box";break;
            case "1":type = "Pallet";break;
            case "2":type = "Box";break;
        }
        return type;
    }
}
