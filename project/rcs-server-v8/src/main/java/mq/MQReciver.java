package mq;

import com.aricojf.platform.mina.message.robot.RCSRequestConfigMessage;
import com.aricojf.platform.mina.message.robot.RCSWriteConfigMessage;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.Pod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.kiva.bus.DownCommand;
import com.mushiny.rcs.kiva.bus.RobotCommand;
import com.mushiny.rcs.kiva.bus.UpCommand;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.wcs.WCSChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSMediaChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSScanPodIdPath;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import com.rabbitmq.client.*;
import mq.consumer.MQDefaultConsumer;
import mq.filter.IPathFilter;
import mq.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * On 2017/6/30.
 *
 * @author wangdong
 *         TODO :
 */
public class MQReciver extends MQManager
{
    private Logger LOG = LoggerFactory.getLogger(MQReciver.class.getName());
    
    private boolean autoResponse = true; // mq自动应答（防止消息丢失）

    private Thread connectListener;

    public MQReciver() {
        connectListener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(channel == null
                                || (!channel.isOpen())){
                            boolean flag = connect2mqServer();
                            if(flag){
                                LOG.info("重连成功。。。");
                                Start();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error("重连出错。。。\n", e);
                    }
                }
            }
        });
    }

    /**
     * 开始接收消息
     */
    public void Start()
    {
        receiveRemoveAGVFromRCSCommand();
        receiveParkNearestCommand();
        receiveMapMessage();
        receiveAGVLoginResponse();
        receiveVGASeriesPath();
        receiveUrgentStopCommand();
        receiveCutAllMotorCommand();
        receiveStartSleepCommand();
        receiveStopSleepCommand();
        receiveAGVStartCommand();
        receiveClearPathCommand();
        receiveAllAGVParkingOrStart();
        receiveChargeMessage();
        receiveMideaChargeMessage();
        receiveStopChargeMessage();

        receiveAGVScanPodSeriesPath();// 扫描pod的路径接收

        receiveActionCommand();

        receiveLockCellNeeds();

        getAGVInfo();
        changePodPosition(); // 更新pod位置
        removePod(); // 移除pod
        removeAGVFromMap(); // 移除AGV
        resendAGVPath();

        getItemInfo(); // 获取agv，cell等信息
        clearMediaError(); // 清除美的小车错误信息


        readAGVConfigInfo(); // 配置信息回读
        configAGVInfo(); // 配置小车固件版本更新信息
        executeActionCommandCode();

        receiveClearPathForPositionNoChanging(); // 如果给小车已下发路径， 且小车已经接收到该路径， 想要重新下发路径， 必须调用该接口

        checkAGVPath(); // 路径核查

        sendActiveMessage(); // 发送激活数据包

        AllAGVInfoSender.getInstance(); // 发送agv所有详细信息

    }

    // 启动监听mq连接 断开后重连
    public void startConnectListener(){
        if(connectListener.getState() == Thread.State.NEW){
            connectListener.start();
        }
    }

    private static boolean isRandom = true;

    //将需要人工处理的AGV从RCS队列移除
    private void receiveRemoveAGVFromRCSCommand(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else{
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REMOVE_ERROR_AGV,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REMOVE_ERROR_AGV);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_REMOVE_ERROR_AGV+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    if (null != robotID && 0 != robotID){
                        AGVManager.getInstance().removeAGVFromRCS(robotID);
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("异常消息.....", e);

        }
    }
    //停到最近二维码，减速到0
    private void receiveParkNearestCommand(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.AGV_PARKING_NEAREST,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.AGV_PARKING_NEAREST);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.AGV_PARKING_NEAREST+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
                    if (null != agv){
                        agv.stopNearCodeCommand();
                    }else{
                        LOG.error("当前ID的AGV不存在，无法执行停到最近二维码动作！");
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }
    //急停
    private void receiveUrgentStopCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.URGENT_STOP,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.URGENT_STOP);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                Map<String, Object> message = (Map<String, Object>) toObject(body);
                LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.URGENT_STOP+"）消息："+message);
                Long robotID = (Long) message.get("robotID");
                KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
                if (null != agv)
                {
                    agv.stopImmediatelyCommand();
                }
                else
                {
                    LOG.error("当前ID的AGV不存在，无法执行急停动作！");
                }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //所有电机断电
    private void receiveCutAllMotorCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.AGV_ALL_MOTOR_CUT,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.AGV_ALL_MOTOR_CUT);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.AGV_ALL_MOTOR_CUT+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);

                    if (null != agv)
                    {
                        agv.stopMotoPowerCommand();
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行所有电机断电动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }

    }

    //开始休眠
    private void receiveStartSleepCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.AGV_START_SLEEP,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.AGV_START_SLEEP);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.AGV_START_SLEEP+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);

                    if (null != agv)
                    {
                        agv.beginSleepCommand();
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行开始休眠动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //结束休眠
    private void receiveStopSleepCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_AGV_START,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_AGV_START);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_AGV_START+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
                    if (null != agv)
                    {
                        agv.startCommand();
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行启动动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //启动
    private void receiveAGVStartCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.AGV_STOP_SLEEP,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.AGV_STOP_SLEEP);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.AGV_STOP_SLEEP+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);

                    if (null != agv)
                    {
                        agv.stopSleepCommand();
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行结束休眠动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //清除已下发路径-清除AGV缓存路径和RCS缓存路径
    private void receiveClearPathCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.AGV_CLEAR_ALLPATH,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.AGV_CLEAR_ALLPATH);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.AGV_CLEAR_ALLPATH+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
                    if (null != agv)
                    {
//                        agv.clearRCSBufferPathCommand();
//                        agv.clearAGVBufferPathCommand();
                        LOG.info("清除缓冲路径！！！");
                        agv.clearBufferSP();
//                        agv.clearBufferSP();
                        agv.setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
                        RCSMainServer.getInstance().publisher.publishAGVActionCommondResponse(robotID, SubjectManager.AGV_CLEAR_PATH_RESPONSE);
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行清除已下发路径动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }

    }


    // 已经下发路径给小车， 但是小车不走， 需要重新下发新路径， 需要清除小车以前路径
    private void receiveClearPathForPositionNoChanging()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_CLEAR_PATH_FOR_POSITION_NO_CHANGING,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_CLEAR_PATH_FOR_POSITION_NO_CHANGING);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_CLEAR_PATH_FOR_POSITION_NO_CHANGING+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(robotID);
                    if (null != agv)
                    {
                        LOG.info("清除小车("+agv.getID()+")所有缓冲路径！！！");
                        agv.clearAndUnlockBufferSP();
                    }
                    else
                    {
                        LOG.error("当前ID的AGV不存在，无法执行清除已下发路径动作！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }

    }

    //接收机器登录消息回复
    private void receiveAGVLoginResponse()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_ROBOT_LOGIN_RESPONSE,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_ROBOT_LOGIN_RESPONSE);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_ROBOT_LOGIN_RESPONSE+"）消息："+message);
                    KivaAGV agv = null;
                    int flag = (Integer) message.get("isLogin");
                    long robotID = 0L;

                    if (flag == 1)
                    {
                        robotID = Long.parseLong("" + message.get("robotID"));
                        agv = AGVManager.getInstance().getAGVByID(robotID);
                    }

                    if (null != agv)
                    {
                        agv.sendLoginOKMessage();
                        LOG.info("-----------------------AGV" + agv.getID() + "登录回复包------------------------");
                    }
                    else
                    {
                       LOG.error("收到AGV" + robotID + "的登录包回复包，但是AGV不存在！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息\n", e);
        }

    }

    //接收地图信息
    private void receiveMapMessage()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.RCS_MAP_RESPONSE,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.RCS_MAP_RESPONSE);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.RCS_MAP_RESPONSE+"）消息："+message);
                    RCSMainServer.mapMessage = message;

                    LOG.error("已接收到地图信息...");
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //所有AGV停到最近二维码或者启动
    private void receiveAllAGVParkingOrStart()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_ALL_AGV_PARKING_OR_START_UP,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_ALL_AGV_PARKING_OR_START_UP);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_ALL_AGV_PARKING_OR_START_UP+"）消息："+message);
                    String type = (String) message.get("type");
                    //type的值只能是start或park，其他为错误，命令丢弃
                    if ("start".equals(type))
                    {
                        AGVManager.getInstance().allAGVStartFromStopNearCode();
                    }
                    else if ("park".equals(type))
                    {
                        AGVManager.getInstance().allAGVStopNearCode();
                    }
                    else
                    {
                       LOG.error("所有AGV停到最近二维码或者启动命令参数错误！！！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //路径下发协议
    private void receiveVGASeriesPath()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_AGV_SERIESPATH,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_AGV_SERIESPATH);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_AGV_SERIESPATH+"）消息："+message);
                    //mingchun.mu@mushiny.com  如果是给充电的小车下发任务，需要通知充电桩结束充电，之后将小车改为闲置状态以便执行任务 --------------------------
                    IPathFilter pathFilter = new PathFilter(null, channel, COM_EXCHANGE);
                    pathFilter.doFilter(message);
                    //mingchun.mu@mushiny.com  --------------------------


                    Long robotid = Long.parseLong(String.valueOf(message.get("robotID")));
                    long upOrDownPod = null == message.get("podUpAddress") ? 0 : Long.parseLong(String.valueOf(message.get("podUpAddress")));
                    Long podAddressCodeID = null == message.get("podDownAddress") ? 0 : Long.parseLong(String.valueOf(message.get("podDownAddress")));
                    boolean isRotatePod = message.get("isRotatePod") == null ? false : (boolean)message.get("isRotatePod") ;
                    int rotateTheta = 0;
                    if(message.get("rotateTheta") != null){
                        rotateTheta = Integer.parseInt(String.valueOf(message.get("rotateTheta")));
                    }
                    List<Long> seriesPath = ((List<Long>)message.get("seriesPath"));


                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);

                    int podCodeID = 0;
                    if(message.get("podCodeID") != null){
                        podCodeID = Integer.parseInt(String.valueOf(message.get("podCodeID")));
                        int podWeight = 0;
                        if(message.get("podWeight") != null){
                            try {
                                podWeight = Integer.parseInt(String.valueOf(message.get("podWeight")));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                LOG.error("解析podWeight时，出错。。。", e);
                                podWeight = 0;
                            }
                        }
                        if(podWeight != 0 && PodManager.getInstance().getPod(podCodeID) != null){
                            PodManager.getInstance().getPod(podCodeID).setPodWeight(podWeight);
                        }

                        // 由于小车每次要执行完任务后，才会下发下一条任务，因此收到任务有pod重量直接更新小车重量加速曲线
                        if(podWeight > 0){
                            if(curAGV != null){
                                curAGV.sendWalkParameterConfigMessage(Utils.getWeightClassByWeight(podWeight / 1000.00f)); // podWeight 单位g转kg
                            }
                        }
                    }

                    LOG.error("< < < - - - RCS接收到MQ路径信息<AGV_ID=" + robotid + ">:顶升点："+upOrDownPod+", 下降点："+podAddressCodeID+", POD_ID="+podCodeID+", 是否旋转："+isRotatePod+",\r\n路径："+seriesPath);
                    if (null != curAGV && seriesPath != null){
                        // ? TODO -- 当收到的路径起点与当前直行路径终点 或空闲小车当前的位置不一致， 将该路径舍弃
                        /*boolean isStartEndEquality = false; // 起终点是否一致 false否 true一致
                        if(seriesPath != null
                                && seriesPath.size() > 0){
                            long firstAddressCodeId = seriesPath.get(0);
                            if(curAGV.getAGVStatus() == AGVConfig.AGV_STATUS_STANDBY){
                                if(firstAddressCodeId == curAGV.getCurrentAddressCodeID()){
                                    isStartEndEquality = true;
                                }
                            }else{
                                if(curAGV.getSeriesPathLinkedList() != null
                                        && curAGV.getSeriesPathLinkedList().size() > 0){
                                    CellNode cellNode = curAGV.getSeriesPathLinkedList().get(curAGV.getSeriesPathLinkedList().size() - 1).getPathList().getLast();
                                    if(cellNode != null
                                            && cellNode.getAddressCodeID() == firstAddressCodeId){
                                        isStartEndEquality = true;
                                    }
                                }else {
                                    if(curAGV.getCurrentGlobalSeriesPath() != null
                                            && curAGV.getCurrentGlobalSeriesPath().getPathList() != null
                                            && curAGV.getCurrentGlobalSeriesPath().getPathList().size() > 0){
                                        CellNode cellNode = curAGV.getCurrentGlobalSeriesPath().getPathList().getLast();
                                        if(cellNode != null
                                                && cellNode.getAddressCodeID() == firstAddressCodeId){
                                            isStartEndEquality = true;
                                        }
                                    }
                                }
                            }
                        }*/
                        WCSSeriesPath wcsSeriesPath = new WCSSeriesPath(upOrDownPod, podAddressCodeID, isRotatePod, rotateTheta, new LinkedList<>(seriesPath));
                        wcsSeriesPath.setPodCodeID(podCodeID);// 设置路径中小车举升或下降的podID
                        wcsSeriesPath.setAgvID(curAGV.getID());
                        if (wcsSeriesPath.checkWCSSeriesPath()){
                            LOG.error("put路径ToRCS<AGV_ID=" + robotid + ">" + wcsSeriesPath.toString());
                                /*curAGV.clearBufferSP();
                                curAGV.setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);*/
                            curAGV.putWCSSeriesPath(wcsSeriesPath);
                        }else {
                            LOG.error("收到的WCS路径不合法！！！！");
                        }
                    }else{
                        LOG.error("当前小车的路径下发异常，小车断开连接");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }






    // mingchun.mu@mushiny.com 2017/09/11
    // 扫描 POD 路径下发协议
    private void receiveAGVScanPodSeriesPath()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_PODSCAN_PATH,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_PODSCAN_PATH);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_PODSCAN_PATH+"）消息："+message);

                    Long robotid = (Long) message.get("robotID");
                    List<Long> seriesPath = ((List<Long>)message.get("path"));
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);
                    long startPoint = 0;
                    long endPoint = 0;
                    if(message.get("start") != null){
                        startPoint = Long.parseLong(String.valueOf(message.get("start")));
                    }
                    if(message.get("end") != null){
                        endPoint = Long.parseLong(String.valueOf(message.get("end")));
                    }

                    LOG.info("< < < - - - RCS接收到MQ  扫描POD路径信息<AGV_ID=" + robotid + ">:路径："+seriesPath);

                    if(message.get("agv_config") != null){
                        WCSScanPodIdPath wcsScanPodIdPath = new WCSScanPodIdPath(new LinkedList<>(seriesPath));
                        wcsScanPodIdPath.setAgvID(curAGV.getID());
                        if (wcsScanPodIdPath.checkWCSSeriesPath())
                        {
                            curAGV.putWCSSeriesPath(wcsScanPodIdPath);
                        }
                    }else{
                        if (startPoint >= 0 && endPoint >= 0 && null != curAGV && seriesPath != null)
                        {
                            LinkedList<Long> emptyPathStart = null;
                            LinkedList<Long> scanPodPath = null;
                            LinkedList<Long> emptyPathEnd = null;
                            if(startPoint == 0 && endPoint == 0){
                                scanPodPath = new LinkedList<>(seriesPath);
                            }else if (startPoint == 0 && endPoint != 0){
                                scanPodPath = new LinkedList<>(seriesPath.subList(0, seriesPath.lastIndexOf(endPoint) + 1));
                                emptyPathEnd = new LinkedList<>(seriesPath.subList(seriesPath.lastIndexOf(endPoint), seriesPath.size()));
                            }else if(startPoint != 0 && endPoint == 0){
                                emptyPathStart = new LinkedList<>(seriesPath.subList(0, seriesPath.indexOf(startPoint) + 1));
                                scanPodPath = new LinkedList<>(seriesPath.subList(seriesPath.indexOf(startPoint), seriesPath.size()));
                            }else if(startPoint != 0 && endPoint != 0){
                                emptyPathStart = new LinkedList<>(seriesPath.subList(0, seriesPath.indexOf(startPoint) + 1));
                                scanPodPath = new LinkedList<>(seriesPath.subList(seriesPath.indexOf(startPoint), seriesPath.lastIndexOf(endPoint) + 1));
                                emptyPathEnd = new LinkedList<>(seriesPath.subList(seriesPath.lastIndexOf(endPoint), seriesPath.size()));
                            }

                            if(emptyPathStart != null && emptyPathStart.size() > 1){
                                WCSSeriesPath wcsSeriesPath = new WCSSeriesPath(0, 0, false, 0, emptyPathStart);
                                wcsSeriesPath.setAgvID(curAGV.getID());
                                if(wcsSeriesPath.checkWCSSeriesPath()){
                                    curAGV.putWCSSeriesPath(wcsSeriesPath);
                                }
                            }
                            if(scanPodPath != null && scanPodPath.size() > 1){
                                WCSScanPodIdPath wcsScanPodIdPath = new WCSScanPodIdPath(scanPodPath);
                                wcsScanPodIdPath.setAgvID(curAGV.getID());
                                if (wcsScanPodIdPath.checkWCSSeriesPath())
                                {
                                    curAGV.putWCSSeriesPath(wcsScanPodIdPath);
                                }
                            }
                            if(emptyPathEnd != null && emptyPathEnd.size() > 1){
                                WCSSeriesPath wcsSeriesPath = new WCSSeriesPath(0, 0, false, 0, emptyPathEnd);
                                wcsSeriesPath.setAgvID(curAGV.getID());
                                if(wcsSeriesPath.checkWCSSeriesPath()){
                                    curAGV.putWCSSeriesPath(wcsSeriesPath);
                                }
                            }
                            else {
                                LOG.error("收到的WCS扫描POD路径不合法！！！！");
                            }

                        }
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }





    //接收通用充电请求
    private void receiveChargeMessage()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_AGV_CHARGE,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_AGV_CHARGE);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_AGV_CHARGE+"）消息："+message);
                    Long robotid = (Long) message.get("robotID");
                    int rotateTheta = (int) message.get("rotateTheta");
                    List<Long> seriesPath = (List<Long>) message.get("seriesPath");
                    LOG.error("< < < - - - RCS接收到MQ  接收通用充电请求<AGV_ID=" + robotid + ">: 旋转角度："+rotateTheta+"， 路径："+seriesPath);
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);
                    WCSChargeSeriesPath chargeSeriesPath = new WCSChargeSeriesPath(rotateTheta, new LinkedList<>(seriesPath));
                    chargeSeriesPath.setAgvID(robotid);
                    if(curAGV != null){

                        // 更新小车电池厂家编号
                        if(message.get("batterManufacturerNumber") != null
                                && (!"".equals(String.valueOf(message.get("batterManufacturerNumber")).trim()))){
                            curAGV.setBatterManufacturerNumber(Integer.parseInt(String.valueOf(message.get("batterManufacturerNumber"))));
                        }

                        if(chargeSeriesPath.checkWCSSeriesPath()){
                            curAGV.putWCSSeriesPath(chargeSeriesPath);
                        }
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //接收美的充电请求
    private void receiveMideaChargeMessage()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_MIDEA_CHARGE,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_MIDEA_CHARGE);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_MIDEA_CHARGE+"）消息："+message);
                    Long robotid = (Long) message.get("robotID");
                    int rotateTheta = (int) message.get("rotateTheta");
                    int chargeNum = (int) message.get("chargeNum");
                    String mac = String.valueOf(message.get("mac"));
                    List<Long> seriesPath = (List<Long>) message.get("seriesPath");

                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);

                    String aa = "0013A2004166E894";
                    WCSMediaChargeSeriesPath chargeSeriesPath = new WCSMediaChargeSeriesPath(rotateTheta, new LinkedList<>(seriesPath), aa);
                    chargeSeriesPath.setAgvID(robotid);
                    curAGV.putWCSSeriesPath(chargeSeriesPath);
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    //接收结束充电指令
    private void receiveStopChargeMessage()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_AGV_STOP_CHARGE,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_AGV_STOP_CHARGE);
            Consumer consumer = new MQDefaultConsumer()
            {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_AGV_STOP_CHARGE+"）消息："+message);

                    Long robotid = (Long) message.get("robotID");
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotid);
                    //当AGV在充电状态时，将AGV状态置为等待任务
                    if (3 == curAGV.getAGVStatus())
                    {
                        curAGV.setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
                    }
                    else
                    {
                        LOG.error("AGV<" + robotid + ">目前是" + curAGV.getAGVStatusInfo() +  "; 无法执行结束充电指令！");
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }


    // mingchun.mu@mushiny.com  接收动作指令 ----------------------------------
    private void receiveActionCommand()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_ACTION_COMMAND,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_ACTION_COMMAND);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_ACTION_COMMAND+"）消息："+message);
                    Long robotID = (Long) message.get("robotID");
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV == null){
                        return;
                    }
                    //闲置状态方可操作
                    if(curAGV.getAGVStatus() == 1){
                        int actionFlag = Integer.parseInt(String.valueOf(message.get("actionFlag")));
                        RobotCommand robotCommand = null;
                        switch (actionFlag){
                            case 32:
                                robotCommand = new UpCommand();
                                curAGV.sendActionCommand(robotCommand);
                                LOG.info("发送举升命令！！！");
                                break;
                            case 33:
                                robotCommand = new DownCommand();
                                curAGV.sendActionCommand(robotCommand);
                                LOG.info("发送下降命令！！！");
                            default:
                        }

                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);

        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    // mingchun.mu@mushiny.com  接收解锁或锁定主题 ----------------------------------
    private void receiveLockCellNeeds()
    {
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = queueName = channel.queueDeclare(SubjectManager.WCS_RCS_UPDATE_CELLS,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_UPDATE_CELLS);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange："+COM_EXCHANGE+"， queue:"+SubjectManager.WCS_RCS_UPDATE_CELLS+"）消息："+message);
                    List<Long> availableAddressList = new LinkedList<>();
                    List<Long> unAvailableAddressList = new LinkedList<>();

                    if(message.get("availableAddressList") != null){
                        List<Object> stringList = (List<Object>)message.get("availableAddressList");
                        for(Object temp: stringList){
                            availableAddressList.add(Long.parseLong(String.valueOf(temp)));
                        }
                    }
                    if(message.get("unAvailableAddressList") != null){
                        List<Object> stringList = (List<Object>)message.get("unAvailableAddressList");
                        for(Object temp: stringList){
                            unAvailableAddressList.add(Long.parseLong(String.valueOf(temp)));
                        }
                    }

                    LOG.info("解锁锁格信息：解锁点 availableAddressList="+availableAddressList+", 锁格点 unAvailableAddressList="+unAvailableAddressList);
                    if(availableAddressList != null){
                        kivaMap.unlockCell(availableAddressList);
                    }
                    if(unAvailableAddressList != null){
                        kivaMap.lockCell(unAvailableAddressList);
                    }
                }
            };
           channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }





    // mingchun.mu@mushiny.com  获取agv当前信息 ----------------------------------
    private void getAGVInfo(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REQUEST_ALL_AGV_INFO,true, false, false, null).getQueue();
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REQUEST_ALL_AGV_INFO);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
//                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_REQUEST_ALL_AGV_INFO + "）消息：" + message);
//                    AllAGVInfoSender.getInstance().putAllAGVInfo2AGVInfo(1);
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    // mingchun.mu@mushiny.com  获取agv当前信息 ----------------------------------
    private void getItemInfo(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REQUEST_ITEM_INFO,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REQUEST_ITEM_INFO);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_REQUEST_ITEM_INFO + "）消息：" + message);
                    String itemKey = null;
                    String itemValue = null;
                    if(message.get("itemKey") != null){
                        itemKey = String.valueOf(message.get("itemKey"));
                    }
                    if(message.get("itemValue") != null){
                        itemValue = String.valueOf(message.get("itemValue"));
                    }
                    if("robot".equals(itemKey)){
                        if(itemValue != null
                                && (!("".equals(itemValue)))){
                            Long robotID = Long.parseLong(itemValue);
                            AGVInfoSender.getInstance().putRobotId2AGVInfo(robotID);
                            AGVInfoSender.getInstance().setRequestUuid(String.valueOf(message.get("sessionID")));
                        }
                    }else if("address".equals(itemKey)){
                        if(itemValue != null
                                && (!("".equals(itemValue)))){
                            Long addressCodeID = Long.parseLong(itemValue);
                            MapCellNodeInfoSender.getInstance().putAddressCodeId(addressCodeID);
                            MapCellNodeInfoSender.getInstance().setRequestUuid(String.valueOf(message.get("sessionID")));
                        }
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }


    // mingchun.mu@mushiny.com  更改pod的位置 ----------------------------------
    private void changePodPosition(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_CHANGING_POD_POSITION,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_CHANGING_POD_POSITION);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_CHANGING_POD_POSITION + "）消息：" + message);
                    long podCodeID = 0;
                    if(message.get("podCodeID") != null){
                        podCodeID = Long.parseLong(String.valueOf(message.get("podCodeID")));
                    }
                    long addressCodeID = 0;
                    if(message.get("addressCodeID") != null){
                        addressCodeID = Long.parseLong(String.valueOf(message.get("addressCodeID")));
                    }
                    IPod pod = PodManager.getInstance().getPod(podCodeID);
                    IPod existPod = null;
                    if(addressCodeID != 0){
                        existPod = PodManager.getInstance().getPod(kivaMap.getMapCellByAddressCodeID(addressCodeID));
                    }
                    if(existPod == null){
                        if(pod != null){
                            if(addressCodeID == 0){
                                pod.setCellNode(null);
                            }else {
                                pod.setCellNode(kivaMap.getMapCellByAddressCodeID(addressCodeID));
                            }
                        }else {
                            pod = new Pod(podCodeID, kivaMap.getMapCellByAddressCodeID(addressCodeID));
                            PodManager.getInstance().addPod2Container(pod);
                        }
                    }else{
                        if(existPod.getPodCodeID() != podCodeID){
                           LOG.error("错误信息：地址码addressCodeID="+addressCodeID+"已经被podCodeID="+existPod.getPodCodeID()+"占用！！！");
                        }
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }
    // mingchun.mu@mushiny.com  移除pod  ----------------------------------
    private void removePod(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REMOVING_POD,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REMOVING_POD);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_REMOVING_POD + "）消息：" + message);
                    long podCodeID = 0;
                    if(message.get("podCodeID") != null){
                        podCodeID = Long.parseLong(String.valueOf(message.get("podCodeID")));
                        PodManager.getInstance().removePodFromContainer(podCodeID);
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }
    // mingchun.mu@mushiny.com  从地图中移除小车  ----------------------------------
    private void removeAGVFromMap(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_OFFLINE_ROBOT,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_OFFLINE_ROBOT);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_OFFLINE_ROBOT + "）消息：" + message);

                    long robotID = 0;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                        KivaAGV kivaAGV = AGVManager.getInstance().getAGVByID(robotID);
                        if(kivaAGV != null){

                            kivaAGV.clearAndUnlockBufferSP(); // 清除并解锁当前移除的小车路径

                            kivaAGV.setCurrentAddressCodeID(0);
                            kivaAGV.setCurrentCellNode(null);
                        }
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }
    // 转弯处断网小车没有收到路径需要重发
    private void resendAGVPath(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_AGV_PATH_RESEND,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_AGV_PATH_RESEND);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_AGV_PATH_RESEND + "）消息：" + message);
                    Long robotID = 0L ;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                    }
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV != null){
                        curAGV.sendSeriesPath(curAGV.getLastSendedSeriesPath());
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }
    // 清除美的小车故障信息
    private void clearMediaError(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_WRS_CLEAR_MEDIA_ERROR,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_WRS_CLEAR_MEDIA_ERROR);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_WRS_CLEAR_MEDIA_ERROR + "）消息：" + message);
                    Long robotID = 0L ;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                    }
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV != null){
                        int generalErrorID = 0;
                        if(message.get("generalErrorID") != null){
                            generalErrorID = Integer.parseInt(String.valueOf(message.get("generalErrorID")));
                        }
                        int commonErrorID = 0;
                        if(message.get("commonErrorID") != null){
                            commonErrorID = Integer.parseInt(String.valueOf(message.get("commonErrorID")));
                        }
                        int seriousErrorID = 0;
                        if(message.get("seriousErrorID") != null){
                            seriousErrorID = Integer.parseInt(String.valueOf(message.get("seriousErrorID")));
                        }
                        int logicErrorID = 0;
                        if(message.get("logicErrorID") != null){
                            logicErrorID = Integer.parseInt(String.valueOf(message.get("logicErrorID")));
                        }
                        curAGV.clearMediaError(generalErrorID, commonErrorID, seriousErrorID, logicErrorID);
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    // 回读
    private void readAGVConfigInfo(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_REQUEST_MEDIA_AGV_CONFIG_PARAMETERS + "）消息：" + message);
                    Long robotID = 0L ;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                    }
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV != null){
                        short matchWord = 0;
                        if(message.get("matchWord") != null){
                            matchWord = Short.parseShort(String.valueOf(message.get("matchWord")));
                        }
                        RCSRequestConfigMessage requestConfigMessage = new RCSRequestConfigMessage(curAGV.getID());
                        requestConfigMessage.setMatchWord(matchWord);
                        curAGV.sendMessageToAGV(requestConfigMessage);
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);
        }
    }

    // 配置小车固件更新信息
    private void configAGVInfo(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_UPDATE_MEDIA_AGV_CONFIG_PARAMETERS + "）消息：" + message);
                    Long robotID = 0L ;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                    }
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV != null){
                        short matchWord = 0;
                        if(message.get("matchWord") != null){
                            matchWord = Short.parseShort(String.valueOf(message.get("matchWord")));
                        }
                        short straightSpeed1 = 0;
                        if(message.get("straightSpeed1") != null){
                            straightSpeed1 = Short.parseShort(String.valueOf(message.get("straightSpeed1")));
                        }
                        short straightSpeed2 = 0;
                        if(message.get("straightSpeed2") != null){
                            straightSpeed2 = Short.parseShort(String.valueOf(message.get("straightSpeed2")));
                        }
                        short straightSpeed3 = 0;
                        if(message.get("straightSpeed3") != null){
                            straightSpeed3 = Short.parseShort(String.valueOf(message.get("straightSpeed3")));
                        }
                        short straightSpeed4 = 0;
                        if(message.get("straightSpeed4") != null){
                            straightSpeed4 = Short.parseShort(String.valueOf(message.get("straightSpeed4")));
                        }
                        short straightSpeed5 = 0;
                        if(message.get("straightSpeed5") != null){
                            straightSpeed5 = Short.parseShort(String.valueOf(message.get("straightSpeed5")));
                        }
                        short cornerSpeed1 = 0;
                        if(message.get("cornerSpeed1") != null){
                            cornerSpeed1 = Short.parseShort(String.valueOf(message.get("cornerSpeed1")));
                        }
                        short cornerSpeed2 = 0;
                        if(message.get("cornerSpeed2") != null){
                            cornerSpeed2 = Short.parseShort(String.valueOf(message.get("cornerSpeed2")));
                        }
                        short cornerSpeed3 = 0;
                        if(message.get("cornerSpeed3") != null){
                            cornerSpeed3 = Short.parseShort(String.valueOf(message.get("cornerSpeed3")));
                        }
                        short acceleration = 0;
                        if(message.get("acceleration") != null){
                            acceleration = Short.parseShort(String.valueOf(message.get("acceleration")));
                        }
                        short dragAcceleration = 0;
                        if(message.get("dragAcceleration") != null){
                            dragAcceleration = Short.parseShort(String.valueOf(message.get("dragAcceleration")));
                        }
                        float XP = 0.0f;
                        if(message.get("XP") != null){
                            XP = Float.parseFloat(String.valueOf(message.get("XP")));
                        }
                        float XI = 0.0f;
                        if(message.get("XI") != null){
                            XI = Float.parseFloat(String.valueOf(message.get("XI")));
                        }
                        float XD = 0.0f;
                        if(message.get("XD") != null){
                            XD = Float.parseFloat(String.valueOf(message.get("XD")));
                        }
                        float thetaP = 0.0f;
                        if(message.get("thetaP") != null){
                            XP = Float.parseFloat(String.valueOf(message.get("thetaP")));
                        }
                        float thetaI = 0.0f;
                        if(message.get("thetaI") != null){
                            thetaI = Float.parseFloat(String.valueOf(message.get("thetaI")));
                        }
                        float thetaD = 0.0f;
                        if(message.get("thetaD") != null){
                            thetaD = Float.parseFloat(String.valueOf(message.get("thetaD")));
                        }
                        RCSWriteConfigMessage writeConfigMessage = new RCSWriteConfigMessage(curAGV.getID());
                        writeConfigMessage.setMatchWord(matchWord);
                        writeConfigMessage.setStraightSpeed1(straightSpeed1);
                        writeConfigMessage.setStraightSpeed2(straightSpeed2);
                        writeConfigMessage.setStraightSpeed3(straightSpeed3);
                        writeConfigMessage.setStraightSpeed4(straightSpeed4);
                        writeConfigMessage.setStraightSpeed5(straightSpeed5);
                        writeConfigMessage.setCornerSpeed1(cornerSpeed1);
                        writeConfigMessage.setCornerSpeed2(cornerSpeed2);
                        writeConfigMessage.setCornerSpeed3(cornerSpeed3);
                        writeConfigMessage.setAcceleration(acceleration);
                        writeConfigMessage.setDragAcceleration(dragAcceleration);
                        writeConfigMessage.setXP(XP);
                        writeConfigMessage.setXI(XI);
                        writeConfigMessage.setXD(XD);
                        writeConfigMessage.setThetaP(thetaP);
                        writeConfigMessage.setThetaI(thetaI);
                        writeConfigMessage.setThetaD(thetaD);
                        curAGV.sendMessageToAGV(writeConfigMessage);
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);

        }
    }

    // 执行动作命令码

    /**
     0 - "启动", 1 - "停到最近的二维码，减速到0", 2 - "急停", 3 - "所有电机供电断电",
     4 - "旋转(托盘固定)", 5 - "旋转(托盘单独转动)", 6 - "旋转(托盘固定，且附带顶升)",
     7 - "旋转(托盘固定，且附带降落)", 8 - "顶升", 9 - "下降", 10 - "开始休眠", 11 - "结束休眠",
     12 - "清空已经下发路径节点", 13 - "开始充电（by LSJ）", 14 - "结束充电（by LSJ）"
     */
    private void executeActionCommandCode(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_REQUEST_ACTION_COMMAND,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_REQUEST_ACTION_COMMAND);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_REQUEST_ACTION_COMMAND + "）消息：" + message);
                    Long robotID = 0L ;
                    if(message.get("robotID") != null){
                        robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                    }
                    KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                    if(curAGV != null) {
                        if (message.get("actionType") != null) {
                            int actionCommandCode = Integer.parseInt(String.valueOf(message.get("actionType")));
                            float actionCommandParam = 0;
                            if(message.get("actionValue") != null){
                                actionCommandParam = Float.parseFloat(String.valueOf(message.get("actionValue")));
                            }
                            switch (actionCommandCode) {
                                case 0:
                                    curAGV.startCommand();
                                    break;
                                case 1:
                                    curAGV.stopNearCodeCommand();
                                    break;
                                case 2:
                                    curAGV.stopImmediatelyCommand();
                                    break;
                                case 3:
                                    curAGV.stopMotoPowerCommand();
                                    break;
                                case 4:
                                    curAGV.turn((short)(actionCommandParam % 360));
                                    break;
                                case 5:
                                    curAGV.turnplateRotate((short)(actionCommandParam % 360));
                                    break;
                                case 6:
                                    curAGV.turnAndUp((short)(actionCommandParam % 360));
                                    break;
                                case 7:
                                    curAGV.turnAndDown((short)(actionCommandParam % 360));
                                    break;
                                case 8:
                                    curAGV.up((short)(actionCommandParam));
                                    break;
                                case 9:
                                    curAGV.down((short)(actionCommandParam));
                                    break;
                                case 10:
                                    curAGV.beginSleepCommand();
                                    break;
                                case 11:
                                    curAGV.stopSleepCommand();
                                    break;
                                case 12:
                                    curAGV.clearAGVBufferPathCommand();
                                    break;
                                case 13:
                                    curAGV.startCharging();
                                    break;
                                case 14:
                                    curAGV.endCharging();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);
        }
    }

    private void checkAGVPath(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_CHECKING_AGV_PATH,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_CHECKING_AGV_PATH);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_CHECKING_AGV_PATH + "）消息：" + message);
                    CheckAGVPathResponseSender.getInstance().putAllAGVInfo2AGVInfo(message);
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);
        }
    }


    private void sendActiveMessage(){
        try {
            String queueName = "";
            if(isRandom){
                queueName = channel.queueDeclare().getQueue();
            }else {
                queueName = channel.queueDeclare(SubjectManager.WCS_RCS_SEND_TO_AGV_ACTIVE_MESSAGE,true, false, false, null).getQueue();;
            }
            channel.queueBind(queueName, COM_EXCHANGE, SubjectManager.WCS_RCS_SEND_TO_AGV_ACTIVE_MESSAGE);
            Consumer consumer = new MQDefaultConsumer(){
                public void handleDelivery(String consumerTag, Envelope envelope,AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> message = (Map<String, Object>) toObject(body);
                    LOG.info("收到（exchange：" + COM_EXCHANGE + "， queue:" + SubjectManager.WCS_RCS_SEND_TO_AGV_ACTIVE_MESSAGE + "）消息：" + message);
                    if(message.get("robotID") != null){
                        int robotId = 0;
                        robotId = Utils.object2Int(message.get("robotID"));
                        KivaAGV kivaAGV = AGVManager.getInstance().getAGVByID(robotId);
                        if(kivaAGV != null){
                            kivaAGV.sendActiveMessage(); // 发送激活包
                            LOG.info("AGV("+kivaAGV.getID()+")发送激活信息。。。");
                        }
                    }
                }
            };
            channel.basicConsume(queueName, autoResponse, consumer);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("异常消息：\n", e);
        }
    }







    private Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            LOG.error("异常消息：\n", ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            LOG.error("异常消息：\n", ex);
        }
        return obj;
    }





}
