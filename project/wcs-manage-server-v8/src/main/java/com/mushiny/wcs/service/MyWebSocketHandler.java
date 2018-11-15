package com.mushiny.wcs.service;

import com.mushiny.wcs.business.ModuleBusiness;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/18.
 */
@Service
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);
    //在线用户列表
    private static final Map<Integer, WebSocketSession> users;
    //用户标识
    private static final String CLIENT_ID = "userId";

    static {
        users = new HashMap<>();
    }

    @Autowired
    private ModuleBusiness moduleBusiness;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("成功建立连接");

        session.sendMessage(new TextMessage("成功建立socket连接"));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {


        String params = message.getPayload();
        LOGGER.info("接收到的消息为:" + params);
        if (!StringUtils.isEmpty(params)) {
            Map<String, String> paramsMap = JSONUtil.jsonToMap(params);
            if (!CollectionUtils.isEmpty(paramsMap)) {
                paramsMap.put("moduleDir", "/home/mslab/wms_v8/" + paramsMap.get("moduleName"));
            }
            moduleBusiness.install(session, paramsMap);
        }


    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接已关闭：" + status);

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}
