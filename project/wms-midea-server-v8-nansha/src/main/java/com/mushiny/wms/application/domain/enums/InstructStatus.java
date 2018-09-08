package com.mushiny.wms.application.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/9.
 */
public enum InstructStatus {
    ACCEPT("ACCEPT"),
    RUNNING("RUNNING"),
    STOCKIN("STOCKIN"),
    STOCKOUT("STOCKOUT"),
    CANCEL("CANCEL"),
    CREATED("CREATED"),
    DISPATCH("DISPATCH");

    private final String status;
    InstructStatus(String statu)
    {
        this.status=statu;
    }

    public String getStatus() {
        return status;
    }

    private static Map<String,InstructStatus> cash=new HashMap<>();

    static {
        for(InstructStatus tt: values())
        {
            cash.put(tt.getStatus(),tt);
        }
    }

    public static InstructStatus parserInstructStatus(String type){
        return cash.get(type);
    }
}
