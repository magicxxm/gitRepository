package com.mushiny.websocket;

import com.mushiny.rabbitmq.RabbitMqReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;


@ServerEndpoint(value = "/getPod/{user}")

public class RCSServer {
    private static Logger LOGGER = LoggerFactory.getLogger(RCSServer.class);
    public static final Map<String, Session> workStations = Collections.synchronizedMap(new HashMap<>(20));

    //连接打开时执行
    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        LOGGER.debug("开启WebSocket监听....");
        workStations.put(user, session);
        LOGGER.debug("有新连接加入！workstationID:" + user + ",当前在线人数为" + workStations.size());
        LOGGER.debug("客户端连接启动结束.");
    }
    @OnMessage
    public String onMessage(String message, Session session) {
        String returnMsg = "success";
        return returnMsg;
    }

    //连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        String user = getStationUser(session);

        if (!StringUtils.isEmpty(user)) {
            LOGGER.debug(String.format("workstationID %s Session %s closed because of %s", user, session.getId(), closeReason));
            workStations.remove(user);
            /*LOGGER.info("工作站{}退出,释放当前工作对应的pod信息", user);
            if (!ObjectUtils.isEmpty(RabbitMqReceiver.rcsAtations.get(user))) {
                LinkedBlockingQueue messageBlockingQueue = RabbitMqReceiver.rcsAtations.get(user).getRcsMessage();
                synchronized (messageBlockingQueue) {

                    if (!CollectionUtils.isEmpty(messageBlockingQueue)) {
                        LOGGER.info("释放的pod为:{}", StringUtils.collectionToCommaDelimitedString(messageBlockingQueue));
                        messageBlockingQueue.clear();
                    } else {
                        LOGGER.info("当前工作站{}对应的pod信息为空，不需要释放", user);
                    }
                }
            }*/


        }


    }


    private String getStationUser(Session session) {
        String result = "";
        for (Map.Entry<String, Session> temp : workStations.entrySet()) {

            if (ObjectUtils.nullSafeEquals(session, temp.getValue())) {
                result = temp.getKey();
                break;
            }
        }
        return result;
    }

    //连接错误时执行
    @OnError
    public void onError(Throwable t, Session session) {

        try {
            session.close();
        } catch (Exception e1) {
            LOGGER.debug(t.getMessage(), t);

        }

    }


}

