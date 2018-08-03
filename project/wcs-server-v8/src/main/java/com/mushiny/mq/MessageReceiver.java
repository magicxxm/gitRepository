package com.mushiny.mq;

import com.mushiny.beans.Robot;
import com.mushiny.business.RobotManager;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Tank.li on 2017/6/22.
 */
@Component
public class MessageReceiver implements IReceiver {

    private static final String RCS_WCS_SP_UNWALK_CELL = "RCS_WCS_SP_UNWALK_CELL";
    private static final String WCS_RCS_UPDATE_CELLS = "WCS_RCS_UPDATE_CELLS";

    private final static Logger logger = LoggerFactory.getLogger(MessageReceiver.class);
    //给地图发送的充电桩信息
    private static final String MAP_CHARGER_BOARD_NET = "MAP_CHARGER_BOARD_NET";

    public static Map receiveRcsItemInfoMap = new HashMap();
    public static Map receiveRcsMediaAGVConfigParametersMap = new HashMap();
    public static Map receiveRcsActionCommandMap = new HashMap();
    public static Map receiveRcsMediaErrorMap = new HashMap();


    @Autowired
    private RobotManager robotManager;
    @Autowired
    private MessageSender messageSender;

    /*地图需要的几个消息*/
    public static final String RCS_WCS_AGV_STATUS_NET = "RCS_WCS_AGV_STATUS_NET";
    public static final String RCS_WCS_ROBOT_STATUS_NET = "RCS_WCS_ROBOT_STATUS_NET";
    public static final String RCS_WCS_ROBOT_RT_NET = "RCS_WCS_ROBOT_RT_NET";

    @RabbitListener(queues = RCS_WCS_QUERYROBOT_RESPONSE)
    public void RCS_WCS_QUERYROBOT_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_QUERYROBOT_RESPONSE:"+data);
        try {
            this.robotManager.ON_RCS_WCS_QUERYROBOT_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*POD出现在错误的位置
    * 1、下发去举升的PODID，但小车没有扫到POD
    * 2、下发去举升的PODID，但小车找到了其他POD
    *
    * 第一种情况，说明下发的POD不在当前位置
    * 第二种情况，说明两个POD都不在正确的位置
    * */
    @RabbitListener(queues = RCS_WCS_POD_ERRORPLACE)
    public void RCS_WCS_POD_ERRORPLACE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_POD_ERRORPLACE:"+data);
        try {
            this.robotManager.ON_RCS_WCS_POD_ERRORPLACE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_SCAN_POD)
    public void RCS_WCS_SCAN_POD(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_SCAN_POD:"+data);
        try {
            this.robotManager.ON_RCS_WCS_SCAN_POD(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @RabbitListener(queues = RCS_WCS_SP_UNWALK_CELL)
    public void RCS_WCS_SP_UNWALK_CELL(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_SP_UNWALK_CELL:"+data);
        try {
            this.robotManager.ON_RCS_WCS_SP_UNWALK_CELL(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*WCS_RCS_UPDATE_CELLS*/
    //{"sectionId":"ec229eb7-7e2b-43a8-b1c7-91bd807e91cf",
    // "availableAddressList":["346"],"blockedAddressList":null}
    /**
     * 接收地图监控发过来的信息 JSON格式
     * @param message
     */
    @RabbitListener(queues = MAP_WCS_UPDATE_CELLS)
    public void MAP_WCS_UPDATE_CELLS(Message message) {
        byte[] body = message.getBody();
        String jsonData = new String(body);
        Map data = JsonUtils.json2Map(jsonData);
        logger.info("收到地图消息MAP_WCS_UPDATE_CELLS:"+data);
        try {
            this.robotManager.ON_WCS_RCS_UPDATE_CELLS(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 小车重新请求路径
     * @param message
     */
    @RabbitListener(queues = RCS_WCS_AGV_REQUEST_PATH)
    public void RCS_WCS_AGV_REQUEST_PATH(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_AGV_REQUEST_PATH:"+data);
        try {
            this.robotManager.ON_RCS_WCS_AGV_REQUEST_PATH(data);
           } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 小车实时信息
     *
     * @param message
     */
    @RabbitListener(queues = RCS_WCS_ROBOT_RT)
    public void RCS_WCS_ROBOT_RT(Message message) {
        if(!this.robotManager.isFlag()){
            return;
        }

        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        try {
            //String jsonData = JsonUtils.map2Json(data);
            Robot robot = this.robotManager.getRegistedRobot(robotId);
            if(robot == null){
                logger.error("小车"+robotId+"不存在! 可能未登录!");
                return;
            }
            //转发实时包到移动监控设备
            //sn+180度偏移量
            int podCodeInfoTheta_changed = CommonUtils.podCorrectTheta;
            //logger.debug("实时包纠偏角度podCodeInfoTheta_changed="+podCodeInfoTheta_changed);
            int podCodeInfoTheta = this.robotManager.toAdjustTheta(CommonUtils.parseInteger("podCodeInfoTheta", data));
            data.put("podCodeInfoTheta", (podCodeInfoTheta+podCodeInfoTheta_changed)%360);
            this.messageSender.sendMsg2MobileDevice(data);
            this.messageSender.sendMsg2MapMonitor(data, RCS_WCS_ROBOT_RT_NET);
            if(sameQueueMsg(robot,data)){//如果消息内容一样 就不执行了
                //logger.error("小车"+robotId+" 收到与上次相同内容的消息,丢弃:"+data);
                return;
            }
            logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_ROBOT_RT:"+data);
            robot.setQueueMsg(data);
            Long preAddr = 0L;
            if(robot.getStatus() != Robot.NOTCHECKED){
                preAddr = Long.parseLong(robot.getAddressId());
            }
            this.robotManager.ON_RCS_WCS_ROBOT_RT(data);
            //地址码ID	Long	addressCodeID
            Long curAddr = CommonUtils.parseLong("addressCodeID",data);
            //RCS发上的位移包是个大坑
            if(!Objects.equals(preAddr, curAddr)){

                /* 机器ID	Long	robotID
                    仓库sectionID 	Long	sectionID
                    前一个位置地址码	Long	previousAddress
                    当前位置地址码	Long	currentAddress
                    时间戳	Long	time*/
                Map move = new HashMap();
                move.put("robotID",data.get("robotID"));
                move.put("sectionID",data.get("sectionID"));
                move.put("previousAddress",preAddr);
                move.put("currentAddress",curAddr);
                move.put("time",System.currentTimeMillis());
                this.robotManager.ON_RCS_WCS_AGV_POSITION_CHANGE(move);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*机器ID	Long	robotID
        地址码ID	Long	addressCodeID
        货架ID	Long	podCodeID
        货架偏移角	Float	podCodeInfoTheta
     */
    private boolean sameQueueMsg(Robot robot, Map data) {
        Map queueMsg = robot.getQueueMsg();
        if(queueMsg == null){
            return false;
        }
        return Objects.equals(queueMsg.get("robotID"),data.get("robotID"))
                && Objects.equals(queueMsg.get("addressCodeID"),data.get("addressCodeID"))
                && Objects.equals(queueMsg.get("podCodeInfoTheta"),data.get("podCodeInfoTheta"))
                && Objects.equals(queueMsg.get("podCodeID"),data.get("podCodeID"));
    }

    @RabbitListener(queues = RCS_WCS_ROBOT_LOGIN)
    public void RCS_WCS_ROBOT_LOGIN(Message message) {
        if(!this.robotManager.isFlag()){
            return;
        }
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_ROBOT_LOGIN:"+data);
        try {
            this.robotManager.ON_RCS_WCS_ROBOT_LOGIN(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * AGV位置改变
     *
     * @param message
     */
    //@RabbitListener(queues = RCS_WCS_AGV_POSITION_CHANGE)
    public void RCS_WCS_AGV_POSITION_CHANGE(Message message) {
        if(!this.robotManager.isFlag()){
            return;
        }
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_AGV_POSITION_CHANGE:"+data);
        try {
            this.robotManager.ON_RCS_WCS_AGV_POSITION_CHANGE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_MAP_REQUEST)
    public void RCS_WCS_MAP_REQUEST(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_MAP_REQUEST:"+data);
        try {
            this.robotManager.ON_RCS_WCS_MAP_REQUEST(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费AGV状态事件
     *
     * @param message
     */
    @RabbitListener(queues = RCS_WCS_AGV_STATUS)
    public void RCS_WCS_AGV_STATUS(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_AGV_STATUS:"+data);
        try {
            this.robotManager.ON_RCS_WCS_AGV_STATUS(data);
            //String jsonData = JsonUtils.map2Json(data);
            this.messageSender.sendMsg2MapMonitor(data,RCS_WCS_AGV_STATUS_NET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费周期性状态数据包
     *
     * @param message
     */
    @RabbitListener(queues = RCS_WCS_ROBOT_STATUS)
    public void RCS_WCS_ROBOT_STATUS(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_ROBOT_STATUS:"+data);
        try {
            this.messageSender.WCS_ANY_ROBOT_STATUS(data);
            this.robotManager.ON_RCS_WCS_ROBOT_STATUS(data);
            //String jsonData = JsonUtils.map2Json(data);
            this.messageSender.sendMsg2MapMonitor(data, RCS_WCS_ROBOT_STATUS_NET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //@RabbitListener(queues = RCS_WCS_ROBOT_HEART_BEAT) 不处理
    public void RCS_WCS_ROBOT_HEART_BEAT(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_ROBOT_HEART_BEAT(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //错误POD码的消息从这里来
    @RabbitListener(queues = RCS_WCS_ROBOT_ERROR)
    public void RCS_WCS_ROBOT_ERROR(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_ROBOT_ERROR(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_ROBOT_CONNECT_RCS)
    public void RCS_WCS_ROBOT_CONNECT_RCS(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_ROBOT_CONNECT_RCS(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_ROBOT_CLOSE_CONNECTION)
    public void RCS_WCS_ROBOT_CLOSE_CONNECTION(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_ROBOT_CLOSE_CONNECTION(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_AGV_NOMOVE_TIMMEOUT)
    public void RCS_WCS_AGV_NOMOVE_TIMMEOUT(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_AGV_NOMOVE_TIMMEOUT:"+data);
        try {
            this.robotManager.ON_RCS_WCS_AGV_NOMOVE_TIMMEOUT(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_AGV_HEART_RT_TIMEOUT)
    public void RCS_WCS_AGV_HEART_RT_TIMEOUT(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_AGV_HEART_RT_TIMEOUT(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_AGV_PARKING_RESPONSE)
    public void RCS_WCS_AGV_PARKING_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_AGV_PARKING_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_URGENT_STOP_RESPONSE)
    public void RCS_WCS_URGENT_STOP_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_URGENT_STOP_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_ALL_MOTOR_CUT_RESPONSE)
    public void RCS_WCS_ALL_MOTOR_CUT_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_ALL_MOTOR_CUT_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_START_SLEEP_RESPONSE)
    public void RCS_WCS_START_SLEEP_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_START_SLEEP_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = RCS_WCS_STOP_SLEEP_RESPONSE)
    public void RCS_WCS_STOP_SLEEP_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_RCS_WCS_STOP_SLEEP_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_CLEAR_PATH_RESPONSE)
    public void RCS_WCS_CLEAR_PATH_RESPONSE(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_CLEAR_PATH_RESPONSE:"+data);
        try {
            this.robotManager.ON_RCS_WCS_CLEAR_PATH_RESPONSE(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR)
    public void AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        try {
            this.robotManager.ON_AGV_REPEAT_CONNECT2_RCS_POSITION_ERROR(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*  MAP_CHARGER_BOARD 充电桩状态*/
    @RabbitListener(queues = MAP_CHARGER_BOARD)
    public void MAP_CHARGER_BOARD(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        //Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("收到RCS消息MAP_CHARGER_BOARD:"+data);
        try {
            this.robotManager.ON_MAP_CHARGER_BOARD(data);
            //String jsonData = JsonUtils.map2Json(data);
            this.messageSender.sendMsg2MapMonitor(data,MAP_CHARGER_BOARD_NET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_LOCK_CELL_TIMEOUT)
    public void RCS_WCS_LOCK_CELL_TIMEOUT(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_LOCK_CELL_TIMEOUT:"+data);
        try {
            this.robotManager.ON_RCS_WCS_LOCK_CELL_TIMEOUT(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_UNLOCKED_CELL_LIST)
    public void RCS_WCS_UNLOCKED_CELL_LIST(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        Long robotId = CommonUtils.parseLong("robotID", data);
        logger.info("小车:"+robotId+"收到RCS消息RCS_WCS_UNLOCKED_CELL_LIST:"+data);
        try {
            this.robotManager.ON_RCS_WCS_UNLOCKED_CELL_LIST(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_RESPONSE_ITEM_INFO)
    public void RCS_WCS_RESPONSE_ITEM_INFO(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("收到RCS消息RCS_WCS_RESPONSE_ITEM_INFO:"+data);
        try {
            String sessionID = (String)data.get("sessionID");
            if (CommonUtils.isEmpty(sessionID)){
                logger.debug("RCS_WCS_RESPONSE_ITEM_INFO的sessionID为空，不处理");
                return;
            }
            receiveRcsItemInfoMap.put(sessionID, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS)
    public void RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("收到RCS消息RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS:"+data);
        try {
            String robotID = data.get("robotID")+"";
            if (CommonUtils.isEmpty(robotID)){
                logger.debug("RCS_WCS_RESPONSE_MEDIA_AGV_CONFIG_PARAMETERS的robotID为空，不处理");
                return;
            }
            receiveRcsMediaAGVConfigParametersMap.put(robotID, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_RESPONSE_MEDIA_ERROR)
    public void RCS_WCS_RESPONSE_MEDIA_ERROR(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("收到RCS消息RCS_WCS_RESPONSE_MEDIA_ERROR:"+data);
        try {
            String robotID = data.get("robotID")+"";
            if (CommonUtils.isEmpty(robotID)){
                logger.debug("RCS_WCS_RESPONSE_MEDIA_ERROR的robotID为空，不处理");
                return;
            }
            receiveRcsMediaErrorMap.put(robotID, data);
            this.robotManager.ON_RCS_WCS_RESPONSE_MEDIA_ERROR(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_RESPONSE_ACTION_COMMAND)
    public void RCS_WCS_RESPONSE_ACTION_COMMAND(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("收到RCS消息RCS_WCS_RESPONSE_ACTION_COMMAND:"+data);
        try {
            /*String sessionID = (String)data.get("sessionID");
            if (CommonUtils.isEmpty(sessionID)){
                logger.debug("RCS_WCS_RESPONSE_ACTION_COMMAND的sessionID为空，不处理");
                return;
            }
            receiveRcsActionCommandMap.put(sessionID, data);*/
            String robotID = data.get("robotID")+"";
            if (CommonUtils.isEmpty(robotID)){
                logger.debug("RCS_WCS_RESPONSE_ACTION_COMMAND的robotID为空，不处理");
                return;
            }
            receiveRcsActionCommandMap.put(robotID, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = ANY_WCS_WAREHOUSE_INIT_REQUEST)
    public void ANY_WCS_WAREHOUSE_INIT_REQUEST(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("WCS收到消息ANY_WCS_WAREHOUSE_INIT_REQUEST:"+data);
        try {
            this.robotManager.ON_ANY_WCS_WAREHOUSE_INIT_REQUEST(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RCS_WCS_ACTION_FINISHED_COMMAND)
    public void RCS_WCS_ACTION_FINISHED_COMMAND(Message message) {
        byte[] body = message.getBody();
        Map data = (Map) CommonUtils.toObject(body);
        logger.info("WCS收到消息RCS_WCS_ACTION_FINISHED_COMMAND:"+data);
        try {
            this.robotManager.RCS_WCS_ACTION_FINISHED_COMMAND(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
