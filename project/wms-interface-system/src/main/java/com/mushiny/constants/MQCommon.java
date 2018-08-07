package com.mushiny.constants;

/**
 * Created by 123 on 2018/3/20.
 */
public class MQCommon {

    /**
     * 接收消息队列
     */
    public static final String AGVJN_ITEMDATA_QUEUE= "AGVJN.ITEMDATA.QUEUE";
    public static final String AGVJN_ITEMDATA_SKU_NO_QUEUE = "AGVJN.ITEMDATA.SKU.NO.QUEUE";
    public static final String AGVJN_SEQUENCE_NO_QUEUE = "AGVJN.SEQUENCE.NO.QUEUE";
    public static final String AGVJN_OUTBOUND_QUEUE = "AGVJN.OUTBOUND.QUEUE";
    public static final String AGVJN_INBOUND_QUEUE = "AGVJN.INBOUND.QUEUE";
    public static final String AGVJN_PRIORITY_QUEUE = "AGVJN.PRIORITY.QUEUE";
    public static final String AGVJN_STOCKUNIT_CHANGE_QUEUE = "AGVJN.CHANGE.QUEUE";

    /**
     * 反馈消息队列
     */
    public static final String OUTBOUND_ACK_QUEUE = "AGVJN.OUTBOUND.ACK.QUEUE";
    public static final String INBOUND_ACK_QUEUE = "AGVJN.INBOUND.ACK.QUEUE";
    public static final String CHANGE_ACK_QUEUE = "AGVJN.CHANGE.ACK.QUEUE";
}
