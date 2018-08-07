package com.mushiny.utils;

/**
 * Created by 123 on 2018/2/22.
 */
public class OrderTypeUtil {

    public static String revertOrderType(String orderType){
        String type = "";
        switch (orderType){
            case "0000":type = "Customer";break;
            case "0001":type = "Transfer";break;
            case "0002":type = "Customer";break;
            case "0003":type = "Vendor Return";break;
            case "0004":type = "Customer";break;
        }
        return type;
    }
}
