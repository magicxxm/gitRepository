package com.mushiny.beans.enums;

import com.mushiny.beans.order.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tank.li on 2017/11/7.
 */
public class OrderErrorMessage {
    private static Map<Integer,String> errorMsg = new HashMap();
    static {
        /*public static final int ERROR_POD_NONEEDFACE = 7; //TRIPPOSITION表记录没有需要的面
        public static final int ERROR_POD_ONPATH = 8;     //重车路径的存储位上有POD
        public static final int ERROR_NO_HOTADDRESS = 9;  //热度计算没有可用的存储位
        public static final int ERROR_NOPOD = 10;         //调度单没有POD
        public static final int SUCCESS = 0;
        public static final int ERROR_EMPTY_PATH = 1;//空车路径计算失败!
        public static final int ERROR_HEAVY_PATH = 2;//重车路径计算失败!
        public static final int ERROR_START_POINT = 3;//小车不在路径起始位置
        public static final int NO_END_POINT = 4;//没有终点位置
        public static final int ERROR_ENDPOINT_NOTMATCH = 11;//终点位置不是路径最后一个节点
        public static final int ERROR_NO_WCSPATH = 5;//没有下发的路径 长度是0或1
        public static final int ERROR_POD = 6;//没有扫到POD 或者POD错误 被使用*/
        errorMsg.put(Order.ERROR_POD_NONEEDFACE,"7:TRIPPOSITION表记录没有需要的面");
        errorMsg.put(Order.ERROR_POD_ONPATH,"8:重车路径的存储位上有POD");
        errorMsg.put(Order.ERROR_NO_HOTADDRESS,"9:热度计算没有可用的存储位");
        errorMsg.put(Order.ERROR_NOPOD,"10:调度单没有POD");
        errorMsg.put(Order.ERROR_EMPTY_PATH,"1:空车路径计算失败");
        errorMsg.put(Order.ERROR_HEAVY_PATH,"2:重车路径计算失败");
        errorMsg.put(Order.ERROR_START_POINT,"3:小车不在路径起始位置");
        errorMsg.put(Order.NO_END_POINT,"4:没有终点位置");
        errorMsg.put(Order.ERROR_ENDPOINT_NOTMATCH,"11:终点位置不是路径最后一个节点");
        errorMsg.put(Order.ERROR_NO_WCSPATH,"5:没有下发的路径 长度是0或1");
        errorMsg.put(Order.ERROR_POD,"6:没有扫到POD 或者POD错误 被使用");
    }

    public static String getMsg(Integer key){
        return errorMsg.get(key);
    }
}
