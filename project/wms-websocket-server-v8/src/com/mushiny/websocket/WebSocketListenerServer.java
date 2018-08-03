package com.mushiny.websocket;

import com.mushiny.comm.SpringUtil;
import com.mushiny.rabbitmq.RabbitMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;


@ServerEndpoint(value = "/listener/{user}")

public class WebSocketListenerServer {
    private static Logger LOGGER = LoggerFactory.getLogger(WebSocketListenerServer.class);
    public static Set sessions = Collections.synchronizedSet(new HashSet<>());
    public static Map<String, Session> workStations = Collections.synchronizedMap(new HashMap<>(20));
    private static final RabbitMessageSender sender = SpringUtil.getBean(RabbitMessageSender.class);
    public static Map<Object, Object> diglabs = Collections.synchronizedMap(new HashMap<>(20));

    //连接打开时执行
    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        workStations.put(user, session);
        LOGGER.debug("有新连接加入！workstationID:" + user + ",当前在线人数为" + workStations.size());
        LOGGER.debug("开启WebSocket监听....");
         LOGGER.debug("客户端连接启动结束.");
    }
    @OnMessage
    public String onMessage(String message, Session session) {

        LOGGER.info("接收到{}的消息为:{} ", getStationUser(session), message);
        String returnMsg = "Success";
        try {

        } catch (Exception e) {
            LOGGER.error("接收消息失败!", e);
            returnMsg = "false";
        }
        return returnMsg;
    }


    //连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        String user="";
        try {
            user = getStationUser(session);
            if (!StringUtils.isEmpty(user)) {
                workStations.remove(user);
            }
            LOGGER.debug(String.format("workstationID %s Session %s closed because of %s", user, session.getId(), closeReason));
            if(session.isOpen())
            {
               session.close(closeReason);
            }
        } catch (IOException e) {
            LOGGER.error("关闭"+user+"的连接出错", e);
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
            if(session.isOpen())
            {
                session.close(new CloseReason(CloseReason.CloseCodes.getCloseCode(4666),"服务器出错啦!"));
            }

        } catch (Exception e1) {
            LOGGER.debug(t.getMessage(), t);

        }

    }


}

