package com.mushiny.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mushiny.comm.SpringUtil;
import com.mushiny.mq.ConfigAnalysis;
import com.mushiny.mq.MQCommon;
import com.mushiny.mq.MQReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MqReceiveThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(MqReceiveThread.class);

    @Override
    public void run() {
        MQReceiver mqReceiver = new MQReceiver(ConfigAnalysis.getProperty(MQCommon.MQSERVER_RECEIVEQUEUE));
        mqReceiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        logger.debug("收到消息:" + text);
                        sendMessage(text);
                    } catch (JMSException e) {
                        throw new RuntimeException("获取消息失败!");
                    }
                } else {
                    logger.error("收到非TextMessage对象" + message);
                }
            }
        });
        mqReceiver.receiveMapMessage();
    }

    public static void sendMessage(String text) {
/*{"resTime":1497866129879,"labelId":"5a5f54e5-39a8-4376-8909-94678bb8a75e","description":"确认","cmd":"1","state":0}
            resTime 时间戳  labelId灯的ID

        //text = "{\"resTime\":1497866129879,\"labelId\":\"5a5f54e5-39a8-4376-8909-94678bb8a75e\",\"description\":\"确认\",\"cmd\":\"1\",\"state\":0}";*/
        logger.info("硬件信息推送过来是:" + text);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map data = mapper.readValue(text, Map.class);
            String digitalID = (String) data.get("labelId");//TODO
            WorkStationHardwareMapper whm = SpringUtil.getBean(WorkStationHardwareMapper.class);
            String workStationID = (String) whm.getMapper().get(digitalID);
            logger.debug("往工作站推送消息....workStationID=" + workStationID);
            if (workStationID == null || "".equals(workStationID)) {
                logger.error("没有该工作站:workStationID=" + workStationID);
                return;
            }
            javax.websocket.Session ss = (Session) WSServer.workStations.get(workStationID);//获取该工作站的websocket会话
            if (ss == null) {
                logger.error("会话信息为空! ss==>" + ss);
                logger.error("会话信息为空! WSServer.workStations==>" + WSServer.workStations);
                return;
            }
            boolean needClose = false;
            if (ss.isOpen()) {
                try {
                    ss.getBasicRemote().sendText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(ss.getId() + "发生错误!");
                    needClose = true;
                    //whm.getMapper().remove(digitalID);//将映射关系移除
                }
            } else {
                needClose = true;
                //whm.getMapper().remove(digitalID);//将映射关系移除
            }
            if (needClose) {
                try {
                    ss.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("消息推送完毕!");
    }

    private static void sendMessage2(String text) {
        logger.debug("往工作站推送消息....");
        Iterator iterator = WSServer.sessions.iterator();
        logger.debug("客户端数量:" + WSServer.sessions.size());

        while (iterator.hasNext()) {
            javax.websocket.Session ss = (javax.websocket.Session) iterator.next();
            boolean needClose = false;
            if (ss.isOpen()) {
                try {
                    ss.getBasicRemote().sendText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(ss.getId() + "发生错误!");
                    needClose = true;
                    WSServer.sessions.remove(ss);
                }
            } else {
                needClose = true;
                WSServer.sessions.remove(ss);
            }
            if (needClose) {
                try {
                    ss.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        logger.debug("消息推送完毕!");
    }
}
