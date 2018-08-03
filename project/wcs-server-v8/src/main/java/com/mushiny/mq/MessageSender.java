package com.mushiny.mq;

import com.mushiny.beans.BaseObject;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.comm.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Tank.li on 2017/7/3.
 * TODO 此代码要改
 */
@Component
public class MessageSender implements ISender,RabbitTemplate.ConfirmCallback {
    /*  WCS_RCS_MAP_RESPONSE
        WCS_RCS_ROBOT_LOGIN_RESPONSE
        WCS_RCS_AGV_SERIESPATH
        WCS_RCS_AGV_PARKING_NEAREST
        WCS_RCS_URGENT_STOP
        WCS_RCS_ALL_MOTOR_CUT
        WCS_RCS_START_SLEEP
        WCS_RCS_STOP_SLEEP
        ON_WCS_RCS_CLEAR_ALLPATH

        * */

    private final static Logger logger = LoggerFactory.getLogger(MessageSender.class);


    private RabbitTemplate rabbitTemplate;
    @Autowired
    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMsg(String exchange,String routingkey,Map content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingkey, content, correlationId);
    }

    private static final String SECTION = "section";
    private static final String SECTIONID = "sectionID";

    /**
     * 小车请求地图 WCS回复
     * @param msg
     */
    public void WCS_RCS_MAP_RESPONSE(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_MAP_RESPONSE消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_MAP_RESPONSE, msg, correlationId);
    }

    /**
     * 小车登录 WCS回复
     * @param msg
     */
    public void WCS_RCS_ROBOT_LOGIN_RESPONSE(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_ROBOT_LOGIN_RESPONSE消息到"+" robotId:"+msg.get("robotID"));
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_ROBOT_LOGIN_RESPONSE, msg, correlationId);
    }

    /**
     * 路径下发协议
     * @param msg
     */
    public void WCS_RCS_AGV_SERIESPATH(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送路径消息WCS_RCS_AGV_SERIESPATH到"+" robotId:"+msg.get("robotID"));
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_AGV_SERIESPATH, msg, correlationId);
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }

    public void ON_WCS_MAP_CHANGED(String jsonData) {
        logger.info("发送消息到地图: " + jsonData);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_MAP_CHANGED, jsonData, correlationId);
    }

    public void sendWebSocketMsg(String str) {
        logger.info("发送消息到WebSocket: " + str);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(EXCHANGE, WEBSOCKET_RECEIVE_QUEUE, str, correlationId);
    }

    public void WCS_RCS_AGV_CHARGE(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送充电消息到"+" robotId:"+msg.get("robotID"));
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_AGV_CHARGE, msg, correlationId);
    }

    public void sendMsg2MapMonitor(Map data, String queue) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String jsonData = JsonUtils.map2Json(data);
        Long section = (Long) data.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        queue = EXCHANGE.toUpperCase()+"_"+queue;
        data.put("wcsTime", System.currentTimeMillis());
        //logger.debug("给地图监控发送消息:"+data+" QUEUE="+queue);
        rabbitTemplate.convertAndSend("MapMonitor", queue, jsonData, correlationId);
    }

    public static void sendMapMessage(Map msg, String queue) {
        MessageSender messageSender = SpringUtil.getBean(MessageSender.class);
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("往小车:"+msg.get("robotID")+"发送消息至："+queue+" msg:" + msg);
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        messageSender.sendMsg(EXCHANGE,queue,msg);
    }

    public void WCS_RCS_UPDATE_CELLS(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_UPDATE_CELLS消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_UPDATE_CELLS, msg, correlationId);
    }

    public void WCS_RCS_CLEAR_ALLPATH(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_CLEAR_ALLPATH消息到RCS: " + msg);
        //String jsonData = JsonUtils.map2Json(map);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_CLEAR_ALLPATH, msg, correlationId);
    }

    public void WCS_RCS_CHANGING_POD_POSITION(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_CHANGING_POD_POSITION消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_CHANGING_POD_POSITION, msg, correlationId);
    }

    public void WCS_RCS_AGV_PATH_RESEND(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_AGV_PATH_RESEND消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_AGV_PATH_RESEND, msg, correlationId);
    }

    public void WCS_RCS_REQUEST_ITEM_INFO(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_REQUEST_ITEM_INFO消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_REQUEST_ITEM_INFO, msg, correlationId);
    }

    public void WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS, msg, correlationId);
    }

    public void WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS, msg, correlationId);
    }

    public void WCS_RCS_CLEAR_MEDIA_ERROR(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_CLEAR_MEDIA_ERROR消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_CLEAR_MEDIA_ERROR, msg, correlationId);
    }

    public void WCS_RCS_REQUEST_ACTION_COMMAND(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_REQUEST_ACTION_COMMAND消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_REQUEST_ACTION_COMMAND, msg, correlationId);
    }

    public void WCS_RCS_OFFLINE_ROBOT(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_RCS_OFFLINE_ROBOT消息到RCS: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_RCS_OFFLINE_ROBOT, msg, correlationId);
    }

    public void sendMsg2MobileDevice(Map msg) {
        msg.put("wcsTime", System.currentTimeMillis());
        //logger.info("发送RCS_WCS_ROBOT_RT_MD消息到移动监控设备: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        Long section = (Long) msg.get(SECTIONID);
        String EXCHANGE = SECTION+section;
        rabbitTemplate.convertAndSend(EXCHANGE, RCS_WCS_ROBOT_RT_MD, msg, correlationId);
    }

    /**
     * 仓库初始化回复
     * @param msg
     */
    public void WCS_ANY_WAREHOUSE_INIT_RESPONSE(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_ANY_WAREHOUSE_INIT_RESPONSE消息: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String EXCHANGE = "ANY_WAREHOUSE_INIT";
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_ANY_WAREHOUSE_INIT_RESPONSE, msg, correlationId);
    }

    /**
     * 定时查询数据库充电调度单并发送到mq
     * @param msg
     */
    public void WCS_ANY_CHARGE_ORDER(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        //logger.debug("发送WCS_ANY_CHARGE_ORDER消息: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String EXCHANGE = "WCS_ANY_CHARGE_ORDER";
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_ANY_CHARGE_ORDER, msg, correlationId);
    }

    /**
     * 转发小车状态周期包到mq
     * @param msg
     */
    public void WCS_ANY_ROBOT_STATUS(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送WCS_ANY_ROBOT_STATUS消息: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String EXCHANGE = "WCS_ANY_ROBOT_STATUS";
        rabbitTemplate.convertAndSend(EXCHANGE, WCS_ANY_ROBOT_STATUS, msg, correlationId);
    }

    /**
     * 发送任何消息到任何queue
     * @param msg
     */
    public void sendMsgByMap(Map msg){
        msg.put("wcsTime", System.currentTimeMillis());
        logger.info("发送sendMsgByMap消息: " + msg);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        String EXCHANGE = CommonUtils.parseString("exchangeName", msg);
        String routingKey = CommonUtils.parseString("routingKey", msg);
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, msg, correlationId);
    }
}
