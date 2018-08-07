package com.mushiny.mq;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.constants.MQCommon;
import com.mushiny.handler.Handler;
import com.mushiny.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Laptop-8 on 2017/7/19.
 */
@Component
public class RabbitMqReceiver {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @Autowired
    private Handler handler;

    /**
     *获取商品信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_ITEMDATA_QUEUE)
    public void receiveItemMessage(String message) {
        String msg = "";
        logger.info("获取商品 mq 信息是：" + message);
        try {

            handler.handleMessage(CommonUtil.ITEMDATA_TYPE,message);
        }catch (Exception e){
            logger.error("商品同步出现异常 e：" + e.getMessage(),e);
        }
        logger.info("商品同步结果: " + msg );
//        rabbitTemplate.convertAndSend(MQCommon.ACK_QUEUE,msg);
    }

    /**
     *获取商品条码信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_ITEMDATA_SKU_NO_QUEUE)
    public void receiveSkuNoMessage(String message) {
        String msg = "";
        logger.info("获取商品条码 mq 信息是：" + message);
        try {

            msg = handler.handleMessage(CommonUtil.SKU_NO_TYPE,message);
        }catch (Exception e){
            logger.error("获取商品条码出现异常 e：" + e.getMessage(),e);
            msg = "";
        }
        logger.info("商品条码同步结果 ： "+ msg);
//        rabbitTemplate.convertAndSend(MQCommon.ACK_QUEUE,msg);
    }
    /**
     *获取串码信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_SEQUENCE_NO_QUEUE)
    public void receiveSeqnenceMessage(String message) {
        String msg = "";
        logger.info("获取商品串码 mq 信息是：" + message);
        try{

            msg = handler.handleMessage(CommonUtil.SEQUENCE_TYPE,message);
        }catch (Exception e){
            logger.error("获取商品串码出现异常 e：" + e.getMessage(),e);
        }
        logger.info("商品串码同步结果： " + msg);

        /*rabbitTemplate.convertAndSend(MQCommon.ACK_QUEUE,msg);*/
    }
    /**
     *获取出库单信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_OUTBOUND_QUEUE)
    public void receiveOutboundMessage(String message) {
        AccessDTO accessDTO = new AccessDTO();
        String msg = "";
        logger.info("获取出库单 mq 信息是：" + message);
        try {

            msg = handler.handleMessage(CommonUtil.OUTBOUND_TYPE,message);
        }catch (Exception e){
            logger.error("出库单同步出现异常 e：" + e.getMessage(),e);
        }finally {
            rabbitTemplate.convertAndSend(MQCommon.OUTBOUND_ACK_QUEUE,msg);
            logger.info("出库单同步反馈结果 ： " + msg);
        }

    }
    /**
     *获取上架信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_INBOUND_QUEUE)
    public void receiveInboundMessage(String message) {
        String msg = "";
        logger.info("获取上架单 mq 信息是：" + message);
        try {
            msg = handler.handleMessage(CommonUtil.INBOUND_TYPE,message);
        }catch (Exception e){
            logger.error("上架单同步出现异常 e："+ e.getMessage(),e);
        }
        rabbitTemplate.convertAndSend(MQCommon.INBOUND_ACK_QUEUE,msg);
        logger.info("上架单同步反馈结果 ： " + msg);
    }
    /**
     *获取优先级信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_PRIORITY_QUEUE)
    public void receivePriorityMessage(String message) {
        String msg = "";
        logger.info("获取优先级 mq 信息是：" + message);
        try {

            msg = handler.handleMessage(CommonUtil.PRIORITY,message);
        }catch (Exception e){
            logger.error("获取订单优先级出现异常 e：" + e.getMessage(),e);
        }
        logger.info(" 优先级处理结果 ： " + msg);
//        rabbitTemplate.convertAndSend(MQCommon.ACK_QUEUE,msg);
    }
    /**
     *获取库存调整信息
     * @param message
     */
    @RabbitListener(queues = MQCommon.AGVJN_STOCKUNIT_CHANGE_QUEUE)
    public void receiveChangeMessage(String message) {
        String msg = "";
        logger.info("获取库存调整 mq 信息是：" + message);
        try {

             msg = handler.handleMessage(CommonUtil.CHANGE_TYPE,message);
        }catch (Exception e){
            logger.error("库存调整出现异常 e ：" + e.getMessage(),e);
        }
        rabbitTemplate.convertAndSend(MQCommon.CHANGE_ACK_QUEUE,msg);
        logger.info(" 库存调整的反馈结果 ： " + msg);
    }

}
