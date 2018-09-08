package com.mushiny.wms.application.domain.enums;

public class RobotState {

    public static final int ERROR = -1;

    public static final int SLEEPING = 0;

    public static final int IDLE = 1;

    public static final int WORKING = 2;

    public static final int CHARGING = 3;

    public static final int OFFLINE = 4;//断开链接

    public static final int HEARTBEATTIMEOUT = 5; //心跳超时

    public static final int NOMOVETIMEOUT = 6;  //位置改变超时


}
