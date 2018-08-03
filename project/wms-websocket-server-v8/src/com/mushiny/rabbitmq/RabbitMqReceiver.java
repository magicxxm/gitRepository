package com.mushiny.rabbitmq;

import com.mushiny.comm.JSONUtil;
import com.mushiny.comm.SpringUtil;
import com.mushiny.websocket.RCSServer;
import com.mushiny.websocket.WSServer;
import com.mushiny.websocket.WebSocketListenerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.websocket.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Laptop-8 on 2017/7/19.
 */
@Component
public class RabbitMqReceiver {
    private static Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    ThreadPoolTaskExecutor rcsExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);
    //public static Map<String, RcsMessageListener> rcsAtations = Collections.synchronizedMap(new HashMap<>(12));
   // public static Map<String, RcsMessageListener> ObBoundStations = Collections.synchronizedMap(new HashMap<>(12));
//WEBSOCKET.RECEIVE.QUEUE
    @RabbitListener(queues = "WEBSOCKET.RECEIVE.QUEUE")
    public void receiveMapMessage(String message) {
        logger.info("硬件信息推送过来是:" + message);
        Map data = null;
        try {
            data = JSONUtil.jsonToMap(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        logger.info("灯和工作站的对应关系为{}" + WSServer.diglabs);
        String station = (String) WSServer.diglabs.get(data.get("labelId"));

        logger.debug("往工作站{}推送消息{}", station, message);
        if (StringUtils.isEmpty(station)) {
            logger.error("{} 没有对应的station! ss==>", data.get("labelId"));
            return;
        }
        Session ss = WSServer.workStations.get(station);//获取该工作站的websocket会话
        if (ss == null) {
            logger.error("会话信息为空! ss==>" + ss);
            logger.error("会话信息为空! WSServer.workStations==>" + WSServer.workStations);
            return;
        }
        sendMessage(station, ss, message);


    }

   /* @RabbitListener(queues = "WEBSOCKET.TEST.RECEIVE2.QUEUE")*/
    public void receiveMapMessage2(String message) {
        logger.info("测试推送过来是:" + message);
        Map data = null;
        try {
            data = JSONUtil.jsonToMap(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        String station = (String) data.get("workStationId");

        logger.debug("往工作站{}推送消息{}", station, message);
        if (StringUtils.isEmpty(station)) {
            logger.error("{} 没有对应的station! ss==>", data.get("labelId"));
            return;
        }
        Session ss = WebSocketListenerServer.workStations.get(station);//获取该工作站的websocket会话
        if (ss == null) {
            logger.error("会话信息为空! ss==>" + ss);
            logger.error("会话信息为空! WebSocketListenerServer.workStations==>" + WebSocketListenerServer.workStations);
            return;
        }
        sendMessage(station, ss, message);


    }


    // WEBSOCKET.RCS.RECEIVE.QUEUE WEBSOCKET.TEST.RECEIVE.QUEUE
    private String geneMessage(String station ) {
        Map data2 = new HashMap();
        data2.put("sectionId", "test");
        data2.put("pod", "success");
        data2.put("workstation", station);
        return JSONUtil.mapToJSon(data2);
    }

    @RabbitListener(queues = "WEBSOCKET.RCS.RECEIVE.QUEUE")
    public void podObBoundMapMessage(String message) {
        logger.info("监听到消息为：" + message);
        try {
            Map<String,String> data = JSONUtil.jsonToMap(message);

            if(!CollectionUtils.isEmpty(data))
            {
                String station = data.get("workstation");
                if(!StringUtils.isEmpty(station))
                {
                    Session ss =RCSServer.workStations.get(station);//获取该工作站的websocket会话
                    if (ss == null) {
                        logger.error("会话信息为空! station " + station);
                        logger.error("会话信息为空! RCSServer.workStations==>" + RCSServer.workStations);
                        return;
                    }else{
                        sendMessage(station, ss, geneMessage(station));
                        sendMessage(station, ss, message);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        //schedul(rcsExecutor, rcsAtations, message, "inBound");
    }

    /*private void schedul(ThreadPoolTaskExecutor executor, Map<String, RcsMessageListener> station, String message, String stationType) {
        Map data = null;
        try {
            data = JSONUtil.jsonToMap(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }

        if (!CollectionUtils.isEmpty(data)) {

            String workstation = (String) data.get("workstation");

            if (!StringUtils.isEmpty(workstation) && !StringUtils.isEmpty(data.get("pod"))) {
                if (!station.containsKey(workstation)) {

                    RcsMessageListener listener = SpringUtil.getBean(RcsMessageListener.class);
                    listener.getRcsMessage().offer(message);
                    listener.setStation(workstation);
                    station.put(workstation, listener);
                    listener.setWorkStationType(stationType);
                    executor.execute(listener);
                } else {
                    station.get(workstation).getRcsMessage().offer(message);
                }
            } else {
                logger.info("未正确解析消息--->{}", message);
            }
        } else {
            logger.debug("硬件信息推送过来消息为空");
            return;
        }
    }*/
    public static void sendMessage(String station, Session session, String text) {
        boolean needClose = false;
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(text);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(session.getId() + "发生错误!");
                needClose = true;
            }
        } else {
            needClose = true;
            WSServer.workStations.remove(session);//将映射关系移除
        }
        if (needClose) {
            try {
                session.close();
                logger.error(station + "已经关闭!");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.debug("给工作站{}消息推送完毕!", station);
    }
}
