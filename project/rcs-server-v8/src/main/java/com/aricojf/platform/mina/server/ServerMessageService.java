/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.AGVMessageContainer;
import com.aricojf.platform.mina.message.SimpleMessage;
import com.aricojf.platform.mina.message.ServerStatusMessage;
import com.aricojf.platform.mina.message.StatusMessageCode;
import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.rcs.server.RCSStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Thread.sleep;

/**
 * Server端消息服务器
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class ServerMessageService extends AGVMessageContainer implements Runnable {

    static Logger LOG = LoggerFactory.getLogger(ServerMessageService.class.getName());
    private static MinaServer minaServer;
    private static ServerStatusMessage status;
    private RobotMessage dataMessage;
    private static ServerMessageService instance;

    public Thread serverMessageServiceThread;

    private ServerMessageService() {

    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new ServerMessageService();
            minaServer = new MinaServer();
            status = new ServerStatusMessage();
            instance.serverMessageServiceThread = new Thread(instance);
            instance.serverMessageServiceThread.setName("报文处理服务1");
            instance.serverMessageServiceThread.start();
        }
    }
    public static ServerMessageService getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static ServerMessageService getInstance() {
        if (instance == null) {
            instance = new ServerMessageService();
            minaServer = new MinaServer();
            status = new ServerStatusMessage();
            instance.serverMessageServiceThread = new Thread(instance);
            instance.serverMessageServiceThread.setName("报文处理服务1");
            instance.serverMessageServiceThread.start();

           */
/* Thread thread2 = new Thread(instance);
            thread2.setName("报文处理服务2");
            thread2.start();


            Thread thread3 = new Thread(instance);
            thread3.setName("报文处理服务3");
            thread3.start();
*//*


        }
        return instance;
    }
*/

    public void run() {
        while (true) {
            try {
                dataMessage = getReceiveDataMessage();
                //数据消息
                if (dataMessage != null) {
                    fireDataMessage(dataMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("主线程("+serverMessageServiceThread.getName()+")错误：\n"+ ExceptionUtil.getMessage(e));
            }
        }
    }

    public void Begin() {
        status.setStatusCode(StatusMessageCode.S_SERVER_OPENING_SERVICE);
        status.setMessage("正在启动服务...");
        RCSStatusService.getInstance().postServerStatusMessage(status);
        if (minaServer.acceptor == null) {
            while (!minaServer.Begin()) {
                status.setStatusCode(StatusMessageCode.S_SERVER_CLOSE_SERVICE);
                status.setMessage("启动服务失败，1秒后重试...");
                RCSStatusService.getInstance().postServerStatusMessage(status);
                try {
                    sleep(1000);
                } catch (Exception e) {
                }
            }
        } else {
            if (minaServer.acceptor.isDisposed()) {
                while (!minaServer.Begin()) {
                    status.setStatusCode(StatusMessageCode.S_SERVER_CLOSE_SERVICE);
                    status.setMessage("启动服务失败，1秒后重试...");
                    RCSStatusService.getInstance().postServerStatusMessage(status);
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
        status.setStatusCode(StatusMessageCode.S_SERVER_OPEN_SERVICE);
        status.setMessage("服务已启动");
        RCSStatusService.getInstance().postServerStatusMessage(status);
    }

    public void Stop() {
        minaServer.Stop();
        status.setStatusCode(StatusMessageCode.S_SERVER_CLOSE_SERVICE);
        status.setMessage("服务已关闭");
        RCSStatusService.getInstance().postServerStatusMessage(status);
    }

    public boolean isRun() {
        return minaServer.acceptor.isActive();
    }

    //本地处理收到的消息
    public void processMessage(SimpleMessage obj) {
        byte[] tmpBytes;
        if (obj.getMessage() instanceof byte[]) {
            tmpBytes = (byte[]) obj.getMessage();
            LOG.info("处理报文:" + HexBinaryUtil.byteArrayToHexString2(tmpBytes));
        }
    }

}
