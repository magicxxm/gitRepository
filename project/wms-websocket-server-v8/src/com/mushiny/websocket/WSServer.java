package com.mushiny.websocket;

import com.mushiny.comm.JSONUtil;
import com.mushiny.comm.SpringUtil;
import com.mushiny.rabbitmq.RabbitMessageSender;
import com.mushiny.websocket.TaskSchedul.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;


@ServerEndpoint(value = "/ws/{user}")

public class WSServer {
    private static Logger LOGGER = LoggerFactory.getLogger(WSServer.class);
    public static Set sessions = Collections.synchronizedSet(new HashSet<>());
    public static Map<String, Session> workStations = Collections.synchronizedMap(new HashMap<>(20));
    private static final RabbitMessageSender sender = SpringUtil.getBean(RabbitMessageSender.class);
    public static Map<Object, Object> diglabs = Collections.synchronizedMap(new HashMap<>(20));

    //连接打开时执行
    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        LOGGER.debug("开启WebSocket监听....");
        workStations.put(user,session);
        LOGGER.debug("有新连接加入！workstationID:" + user + ",当前在线人数为" + workStations.size());
        LOGGER.debug("客户端连接启动结束.");
    }
    @OnMessage
    public String onMessage(String message, Session session) {
        //LOGGER.debug(session.getId() + "：" + message);
        LOGGER.info("接收到{} session id {} 的消息为:{} ", getStationUser(session), session.getId(),message);
        String returnMsg = "Success";
        try {
            Map pickTemp = JSONUtil.jsonToMap(message);
            if (!CollectionUtils.isEmpty(pickTemp)) {
                if (pickTemp.containsKey("shootLight")) {
                    if(!StringUtils.isEmpty(pickTemp.get("labelId")))
                    {
                        if (!diglabs.containsKey(pickTemp.get("labelId"))) {
                            diglabs.put(pickTemp.get("labelId"), pickTemp.get("workStationId"));
                        } else {
                            diglabs.replace(pickTemp.get("labelId"), pickTemp.get("workStationId"));
                        }
                        sender.sendMessage(message);
                    }else{
                        LOGGER.warn("接收到拍灯信息未包含灯id,丢弃{} ",message);
                    }

                } else {

                    TaskSchedule.schedule(pickTemp);
                }

            }
        } catch (Exception e) {
            LOGGER.error("接收消息失败!", e);
            returnMsg = "false";
        }
        return returnMsg;
    }


    //连接关闭时执行
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        String user = getStationUser(session);
        if (!StringUtils.isEmpty(user)) {
            workStations.remove(user);
            LOGGER.debug(String.format("工作站 %s 对应的websocket连接 %s 关闭,因为 %s", user, session.getId(), closeReason));

        }else{
            LOGGER.debug("未找到websocket连接{}对应的,关闭原因{}",session.getId(),closeReason);

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
                session.close();
            }
        } catch (Exception e1) {
            LOGGER.error(t.getMessage(), t);

        }

    }


}

