package com.filter;

import com.aricojf.platform.mina.message.robot.RobotMessage;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.Robot2RCSMessageTypeConfig;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 登录包在指定间隔时间内获取一次
 *
 * Created by Laptop-6 on 2017/8/30.
 * @author mingchun.mu@mushiny.com
 */
public class LoginRequestMessageIntervalGetter {

    Logger LOG = Logger.getLogger(LoginRequestMessageIntervalGetter.class.getName());
    private static long INTERVAL_TIME = 3000;

    private static Map<Long, Long> robotMap = new HashMap<>();

    public boolean handle(RobotMessage data){
        if (data != null) {
            data.toObject();
            if (data.getVelifyFunctionCode() == Robot2RCSMessageTypeConfig.ROBOT_LOGIN_REQUEST_MESSAGE) {
                long robotID = data.getRobotID();
                if(robotMap.get(robotID) == null){
                    robotMap.put(robotID, System.currentTimeMillis());
                    return true;
                }else{
                    if(System.currentTimeMillis() - robotMap.get(robotID) > INTERVAL_TIME){
                        robotMap.put(robotID, System.currentTimeMillis());
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
