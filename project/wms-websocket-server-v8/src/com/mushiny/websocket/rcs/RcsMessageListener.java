/*
package com.mushiny.websocket.rcs;

import com.mushiny.comm.JSONUtil;
import com.mushiny.websocket.RCSServer;
import com.mushiny.websocket.RcsOBoundServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

*/
/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/9/6.
 *//*


@Component
@Scope("prototype")
public class RcsMessageListener implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(RcsMessageListener.class);
    private LinkedBlockingQueue rcsMessage = new LinkedBlockingQueue(1000);
    private String station;
    private String workStationType;

    private String geneMessage() {
        Map data2 = new HashMap();
        data2.put("sectionId", "test");
        data2.put("pod", "success");
        data2.put("workstation", station);
        return JSONUtil.mapToJSon(data2);
    }

    public String getWorkStationType() {
        return workStationType;
    }

    public void setWorkStationType(String workStationType) {
        this.workStationType = workStationType;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public LinkedBlockingQueue getRcsMessage() {
        return rcsMessage;
    }

    @Override
    public void run() {

        while (true) {
            Session ss = null;
            while (!rcsMessage.isEmpty()) {


                if (workStationType.equals("oBound")) {
                    ss = RcsOBoundServer.workStations.get(station);
                } else {
                    ss = RCSServer.workStations.get(station);
                }

                if (ss == null) {
                    logger.error("未找到工作站{}", station);
                    if (workStationType.equals("oBound")) {
                        logger.error("会话信息为空! RcsOBoundServer.workStations==>{}", RcsOBoundServer.workStations);

                    } else {
                        logger.error("会话信息为空! RCSServer.workStations==>{}", RCSServer.workStations);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                } else {
                    //先发一条测试数据
                    sendMessage(station, ss, geneMessage());
                    sendMessage(station, ss, (String) rcsMessage.poll());
                }

            }


        }

    }

    private void sendMessage(String station, Session session, String text) {
        boolean needClose = false;
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(text);
            } catch (Exception e) {
                logger.error(e.getMessage());

                logger.error(station + "发生错误!");
                needClose = true;

            }
        } else {
            needClose = true;

            if (workStationType.equals("oBound")) {
                RcsOBoundServer.workStations.remove(session);//将映射关系移除

            } else {
                RCSServer.workStations.remove(session);//将映射关系移除
            }

        }
        if (needClose) {
            try {
                session.close();
                logger.error(station + "已经关闭!");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        logger.debug("往工作站{}推送完毕!\n推送的消息为:{}", station, text);
    }
}
*/
