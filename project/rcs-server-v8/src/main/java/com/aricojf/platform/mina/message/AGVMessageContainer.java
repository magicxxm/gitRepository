/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message;

import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * AGV消息容器
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVMessageContainer {
    private RobotRTMessage rtMessage = new RobotRTMessage();
    private  RobotStatusMessage statusMessage = new RobotStatusMessage();
    private  RobotHeartBeatRequestMessage heartMessage = new RobotHeartBeatRequestMessage();
    private  RobotErrorMessage errorMessage = new RobotErrorMessage();
    private  RobotLoginRequestMessage loginMessage = new RobotLoginRequestMessage();
    private Robot2RCSActionCommandResponseMessage actionCommandReponseMessage = new Robot2RCSActionCommandResponseMessage();

    // mingchun.mu@mushiny.com --------------------------------
    private Robot2RCSActionFinishedMessage actionFinishedMessage = new Robot2RCSActionFinishedMessage(); // 动作完成命令包
    private Robot2RCSResponseMessage responseMessage = new Robot2RCSResponseMessage(); // 小车正常应答回复包
    private Robot2RCSResponseExceptionMessage rcsResponseExceptionMessage = new Robot2RCSResponseExceptionMessage(); // 小车异常应答包
    private Robot2RCSMidiaErrorMessage mediaErrorMessage = new Robot2RCSMidiaErrorMessage(); // 小车异常应答包
    private RobotResponseConfigMessage responseConfigMessage = new RobotResponseConfigMessage(); // 配置回读响应包
    // mingchun.mu@mushiny.com --------------------------------


    static Logger LOG = LoggerFactory.getLogger(AGVMessageContainer.class.getName());
    protected LinkedBlockingQueue<RobotMessage> dataMessageBufferList = new LinkedBlockingQueue();
    private  List<OnReceivedDataMessageListener> dataMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVAllMessageListener> agvAllMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVRTMessageListener> agvRTMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVStatusMessageListener> agvStatusMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVHeartBeatMessageListener> agvHeartMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVErrorMessageListener> agvErrorMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVLoginMessageListener> agvLoginRequestMessageListenerList = new CopyOnWriteArrayList();
    private  List<OnReceiveAGVActionCommandResponseMessageListener> agvActionCommandResponseMessageListenerList = new CopyOnWriteArrayList();

    //===========================消息出入队列===================================
    //缓冲数据消息入栈
    public void postReceiveDataMessage(RobotMessage data) {
        if(data==null) {
            LOG.error("####严重错误,RCS缓冲数据压栈时出错,消息为空!");
            return;
        }
        try {
            dataMessageBufferList.put(data);
        } catch (InterruptedException ie) {
            LOG.error("####严重错误,RCS缓冲数据压栈时被中断:\n" + ExceptionUtil.getMessage(ie));
        }
    }


    //从缓冲数据得到消息
    public RobotMessage getReceiveDataMessage() {
        try {
            return dataMessageBufferList.take();
        } catch (InterruptedException ie) {
            LOG.error("####严重错误,RCS缓冲数据GET时被中断:\n" + ExceptionUtil.getMessage(ie));
            return null;
        }
    }
    
     //分发数据消息
    public void fireDataMessage(RobotMessage data) {
        if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_REALTIME_MESSAGE) {
            rtMessage.setMachine(data.getMachine());
            rtMessage.setMessage(data.getMessage());
            rtMessage.setSession(data.getSession());
            rtMessage.toObject();
            for (OnReceiveAGVRTMessageListener listener : agvRTMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVRTMessage(rtMessage);
                }

            }
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVRTMessage(rtMessage);
                }
            }
        }
        if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_STATUS_MESSAGE) {
            statusMessage.setMachine(data.getMachine());
            statusMessage.setMessage(data.getMessage());
            statusMessage.setSession(data.getSession());
            statusMessage.toObject();
            for (OnReceiveAGVStatusMessageListener listener : agvStatusMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVStatusMessage(statusMessage);
                }
            }
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVStatusMessage(statusMessage);
                }
            }
        }
        if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_ERROR_MESSAGE) {
            errorMessage.setMachine(data.getMachine());
            errorMessage.setMessage(data.getMessage());
            errorMessage.setSession(data.getSession());
            errorMessage.toObject();
            for (OnReceiveAGVErrorMessageListener listener : agvErrorMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVErrorMessage(errorMessage);
                }
            }
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVErrorMessage(errorMessage);
                }
            }
        }
        if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.HEART_BEAT_REQUEST_MESSAGE) {
            heartMessage.setMachine(data.getMachine());
            heartMessage.setMessage(data.getMessage());
            heartMessage.setSession(data.getSession());
            heartMessage.toObject();
            for (OnReceiveAGVHeartBeatMessageListener listener : agvHeartMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVHeartBeatMessage(heartMessage);
                }
            }
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVHeartBeatMessage(heartMessage);
                }
            }
        }
        if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_LOGIN_REQUEST_MESSAGE) {
            loginMessage.setMachine(data.getMachine());
            loginMessage.setMessage(data.getMessage());
            loginMessage.setSession(data.getSession());
            loginMessage.toObject();
            for (OnReceiveAGVLoginMessageListener listener : agvLoginRequestMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVLoginMessage(loginMessage);
                }
            }
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVLoginMessage(loginMessage);
                }
            }
        }
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_REPONSE_ACTION_COMMAND_MESSAGE) {
            actionCommandReponseMessage.setMachine(data.getMachine());
            actionCommandReponseMessage.setMessage(data.getMessage());
            actionCommandReponseMessage.setSession(data.getSession());
            actionCommandReponseMessage.toObject();
             for (OnReceiveAGVActionCommandResponseMessageListener listener : agvActionCommandResponseMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVActionCommandResponseMessage(actionCommandReponseMessage);
                }
            }
             for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceivedAGVActionCommandResponseMessage(actionCommandReponseMessage);
                }
            }
        }

        // mingchun.mu@mushiny.com --------------------------------------------------
        // 动作命令完成  消息分发
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_ACTION_FINISHED_COMMAND_MESSAGE) {
            actionFinishedMessage.setMachine(data.getMachine());
            actionFinishedMessage.setMessage(data.getMessage());
            actionFinishedMessage.setSession(data.getSession());
            actionFinishedMessage.toObject();
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceiveRobot2RCSActionFinishedMessageListener(actionFinishedMessage);
                }
            }
        }
        // 正常应答包
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_NORMAL) {
            responseMessage.setMachine(data.getMachine());
            responseMessage.setMessage(data.getMessage());
            responseMessage.setSession(data.getSession());
            responseMessage.toObject();
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.OnReceiveRobot2RCSResponseMessage(responseMessage);
                }
            }
        }
        // 异常应答包
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_EXCEPTION) {
            rcsResponseExceptionMessage.setMachine(data.getMachine());
            rcsResponseExceptionMessage.setMessage(data.getMessage());
            rcsResponseExceptionMessage.setSession(data.getSession());
            rcsResponseExceptionMessage.toObject();
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.OnReceiveRobot2RCSResponseExceptionMessage(rcsResponseExceptionMessage);
                }
            }
        }
        // 美的故障数据包
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_MEDIA_ERROR_MESSAGE) {
            mediaErrorMessage.setMachine(data.getMachine());
            mediaErrorMessage.setMessage(data.getMessage());
            mediaErrorMessage.setSession(data.getSession());
            mediaErrorMessage.toObject();
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceiveRobot2RCSMediaErrorMessageListener(mediaErrorMessage);
                }
            }
        }
        // 配置响应包
        if(data.getVelifyFunctionCode()==Robot2RCSMessageTypeConfig.ROBOT_RESPONSE_CONFIG_MESSAGE) {
            responseConfigMessage.setMachine(data.getMachine());
            responseConfigMessage.setMessage(data.getMessage());
            responseConfigMessage.setSession(data.getSession());
            responseConfigMessage.toObject();
            for (OnReceiveAGVAllMessageListener listener : agvAllMessageListenerList) {
                if (listener != null) {
                    listener.onReceiveRobot2RCSResponseConfigMessageListener(responseConfigMessage);
                }
            }
        }
        // mingchun.mu@mushiny.com --------------------------------------------------





        for (OnReceivedDataMessageListener listener : dataMessageListenerList) {
            if (listener != null) {
                listener.onReceivedMessage(data);
            }
        }
    }


    //===========================监听注册========================================
     //注册报文消息监听
    public void registeReceiveDataMessageListener(OnReceivedDataMessageListener listener) {
        if (listener == null) {
            return;
        }
        if (!dataMessageListenerList.contains(listener)) {
            dataMessageListenerList.add(listener);
        }
    }

    //移除报文消息监听
    public void removeReceiveDataMessageListener(OnReceivedDataMessageListener listener) {
        if(listener==null) {
            return;
        }
        if (dataMessageListenerList.contains(listener)) {
            dataMessageListenerList.remove(listener);
        }
    }
    public void registeAGVAllMessageListener(OnReceiveAGVAllMessageListener listener) {
        if(listener==null) {
            return;
        }
        if (!agvAllMessageListenerList.contains(listener)) {
            agvAllMessageListenerList.add(listener);
        }
    }

    public void removeAGVAllMessageListener(OnReceiveAGVAllMessageListener listener) {
        if(listener==null) {
            return;
        }
        if (agvAllMessageListenerList.contains(listener)) {
            agvAllMessageListenerList.remove(listener);
        }
    }

    public void registeAGVRTMessageListener(OnReceiveAGVRTMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (!agvRTMessageListenerList.contains(listener)) {
            agvRTMessageListenerList.add(listener);
        }
    }

    public void removeAGVRTMessageListener(OnReceiveAGVRTMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (agvRTMessageListenerList.contains(listener)) {
            agvRTMessageListenerList.remove(listener);
        }
    }

    public void registeAGVStatusMessageListener(OnReceiveAGVStatusMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (!agvStatusMessageListenerList.contains(listener)) {
            agvStatusMessageListenerList.add(listener);
        }
    }

    public void removeAGVStatusMessageListener(OnReceiveAGVStatusMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (agvStatusMessageListenerList.contains(listener)) {
            agvStatusMessageListenerList.remove(listener);
        }
    }

    public void registeAGVHeartMessageListener(OnReceiveAGVHeartBeatMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (!agvHeartMessageListenerList.contains(listener)) {
            agvHeartMessageListenerList.add(listener);
        }
    }

    public void removeAGVHeartMessageListener(OnReceiveAGVHeartBeatMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (agvHeartMessageListenerList.contains(listener)) {
            agvHeartMessageListenerList.remove(listener);
        }
    }

    public void registeAGVErrorMessageListener(OnReceiveAGVErrorMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (!agvErrorMessageListenerList.contains(listener)) {
            agvErrorMessageListenerList.add(listener);
        }
    }

    public void removeAGVErrorMessageListener(OnReceiveAGVErrorMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (agvErrorMessageListenerList.contains(listener)) {
            agvErrorMessageListenerList.remove(listener);
        }
    }

    public void registerAGVLoginMessageListener(OnReceiveAGVLoginMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (!agvLoginRequestMessageListenerList.contains(listener)) {
            agvLoginRequestMessageListenerList.add(listener);
        }
    }

    public void removeAGVLoginMessageListener(OnReceiveAGVLoginMessageListener listener) {
         if(listener==null) {
            return;
        }
        if (agvLoginRequestMessageListenerList.contains(listener)) {
            agvLoginRequestMessageListenerList.add(listener);
        }
    }
    public void registeOnReceiveAGVActionCommandResponseMessageListener(OnReceiveAGVActionCommandResponseMessageListener listener) {
        if(listener == null) {
            return;
        }
        if(!agvActionCommandResponseMessageListenerList.contains(listener)) {
            agvActionCommandResponseMessageListenerList.add(listener);
        }
    }
     public void removeOnReceiveAGVActionCommandResponseMessageListener(OnReceiveAGVActionCommandResponseMessageListener listener) {
        if(listener == null) {
            return;
        }
        if(agvActionCommandResponseMessageListenerList.contains(listener)) {
            agvActionCommandResponseMessageListenerList.remove(listener);
        }
    }

}
