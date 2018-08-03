package com.mushiny.beans.enums;

/**
 * Created by Tank.li on 2017/8/18.
 */
public class TripStatus {
    public static final String AVAILABLE = "Available"; //可运行的都是Avaliable
    public static final String NEW = "New"; //新建的都是New
    public static final String PROCESS = "Process";
    public static final String LEAVING = "Leaving"; //回库房 可以分配下一个了 进入小车的下一个调度单组的
    public static final String NOT_FINISHED = "NotFinish";  //需要检查子菜单的状态 可能运行一半又释放回去了
    public static final String FINISHED = "Finish";
}
