package com.mushiny.beans.enums;

/**
 * Created by Tank.li on 2017/8/18.
 */
public enum OrderPositionStatus {
    Available("Available"),
    Process("Process"),
    Not_Finished("Not_Finish"),
    Finished("Finish");


    private String type;
    OrderPositionStatus(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
