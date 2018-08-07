package com.mushiny.utils;

/**
 * Created by 123 on 2018/2/22.
 */
public class StockStateUtil {

    /**
     * 采用静态方法将苏宁接口状态转换为牧星系统的状态
     * @param stockState
     * @return
     */
    public static String toWmsState(String stockState){
        String state = "U";
        switch (stockState){
            case "U":state = "Inventory";break;//可用状态
            case "Q":state = "Inspecting";break;//质检状态
            case "S":state = "Freezing";break;//冻结状态
            case "R":state = "Returning";break;//退回状态
            case "L":state = "Adventing";break;//临期状态
            case "O":state = "Other";break;//其他状态
        }
        return state;
    }

    /**
     * 采用静态方法将牧星系统状态转换为苏宁接口的状态
     * @param stockState
     * @return
     */
    public static String toSuningState(String stockState){
        String state = "Inventory";
        switch (stockState){
            case "Inventory":state = "U";break;//可用状态
            case "Inspecting":state = "Q";break;//质检状态
            case "Freezing":state = "S";break;//冻结状态
            case "Returning":state = "R";break;//退回状态
            case "Adventing":state = "L";break;//临期状态
            case "Other":state = "O";break;//其他状态
        }
        return state;
    }
}
