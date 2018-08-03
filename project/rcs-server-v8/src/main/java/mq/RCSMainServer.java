package mq;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.ServerStatusChanagedMessageListener;
import com.aricojf.platform.mina.message.ServerStatusMessage;
import com.aricojf.platform.mina.message.robot.*;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseExceptionMessage;
import com.aricojf.platform.mina.message.robot.response.Robot2RCSResponseMessage;
import com.aricojf.platform.mina.server.ServerManager;
import com.aricojf.platform.mina.server.ServerMessageService;
import com.mingchun.mu.aricojf.platform.mina.message.robot.media.error.Robot2RCSMidiaErrorMessage;
import com.mingchun.mu.mushiny.extra.function.AddressCodeIdLostListener;
import com.mingchun.mu.mushiny.extra.function.RealTimeMessageLost;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNode;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNodeManager;
import com.mingchun.mu.mushiny.kiva.path.*;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.Pod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.map.*;
import com.mushiny.kiva.path.RotateArea;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.kiva.pathviewtool.PathViewApplicationForRCS;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.global.CommandActionTypeConfig;
import com.mushiny.rcs.global.RCSLogConfig;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import com.mushiny.rcs.listener.CellListener;
import com.mushiny.rcs.listener.RCSListenerManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.server.RCSStatusService;
import com.mushiny.rcs.server.RCSTimer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.scanPod.ScanPodTest;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

public class RCSMainServer implements ServerStatusChanagedMessageListener, AGVListener,
        OnReceiveAGVAllMessageListener, AGVTimeoutListener, CellListener,
        AddressCodeIdLostListener {
    Logger LOG;
    private static RCSMainServer instance;
    private ServerMessageService kivaServer;
    private final MapManager mapManager = MapManager.getInstance();
    public static Map<String, Object> mapMessage;
    private KivaMap kivaMap;
    public MQPublisher publisher;
    private MQReciver reciver;

    public RCSMainServer() {
    }

    public static RCSMainServer getInstance() {
        if(instance == null) {
            instance = new RCSMainServer();
        }

        return instance;
    }

    public static void main(String[] args) {
        getInstance().startServer();
    }

    private void startServer() {
        LOG = LoggerFactory.getLogger(RCSMainServer.class.getName());
        String rcsConfigPath = SubjectManager.RCS_CONFIG_PATH;
//        String userDir = System.getProperty("user.dir");

        /*String rcsConfigPath = System.getProperty("rcsconfig");
        if (null == rcsConfigPath || "".equals(rcsConfigPath))
        {
            rcsConfigPath = "E:\\KivaConfigTool\\config\\RCSConfig.xml";
        }*/

        if(null != rcsConfigPath && !"".equals(rcsConfigPath)) {
            RCSLogConfig.getInstance().setRcsConfigFile(rcsConfigPath);
            RCSLogConfig.getInstance().initRCSGlobalParameter();
            this.reciveMapMessageAndInstallMap(rcsConfigPath);
            this.initMinaServer();
            RCSTimer.getInstance().start();
        } else {
            this.LOG.error("----RCS配置文件解析错误，程序退出-------");
        }

    }

    private void initMinaServer() {
        this.kivaServer = ServerManager.getMessageServerInstance();
        this.kivaServer.registeAGVAllMessageListener(this);
        // mingchun.mu@mushiny.com  添加扫描pod监听
        this.kivaServer.registeAGVAllMessageListener(new ScanPodTest());
        // mingchun.mu@mushiny.com  -------------------------------------------


        RCSStatusService.getInstance().registerRCSStatusListener(this);
        RCSListenerManager.getInstance().registeAGVListener(this);
        RCSListenerManager.getInstance().registeAGVTimeoutListener(this);
        RealTimeMessageLost.getInstance().registerAddressCodeIdLostListener(this);
        this.kivaServer.Begin();
    }


    private XmlParser parser = new XmlParser();

    private void reciveMapMessageAndInstallMap(String confPath) {
        MQManager manager = new MQManager();
        boolean flag = manager.connect2mqServer();
        if(flag) {
            this.reciver = new MQReciver();
            this.reciver.Start();
            reciver.startConnectListener();
            this.publisher = new MQPublisher();

            try {
                while(null == mapMessage) {
                    this.publisher.publishVirtualMapRequest();
                    Thread.sleep(1200L);
                    this.LOG.warn("正在尝试获取地图信息...");
                }

                List<Long> unWalkedCellList = (List<Long>) mapMessage.get("unWalkedCell");
                if (null == unWalkedCellList)
                {
                    unWalkedCellList = new LinkedList<>();
                }
                List<Long> followCellList = (List<Long>) mapMessage.get("followCell");
                if (null == followCellList)
                {
                    followCellList = new LinkedList<>();
                }
                this.kivaMap = new KivaMap(((Integer)mapMessage.get("row")).intValue(), ((Integer)mapMessage.get("column")).intValue());
                this.kivaMap.initKivaMap();
                this.mapManager.installMap(this.kivaMap);
                MQManager.kivaMap = kivaMap; // 初始化MQ使用的地图



                LinkedList<RotateArea> rotateAreaList = new LinkedList();
                HashMap<String, ArrayList<Long>> turnArea = (HashMap)mapMessage.get("stationAndTurnArea");
                Map<String, Object> WS_TOWARD = (Map<String, Object>)mapMessage.get("WS_TOWARD");


                List<Long> workStationIDList = new ArrayList<>();
                try {
                    List<Element> workStationList = parser.getResList("/config/workstations.xml", "/work-stations/work-station");
                    for(Element element : workStationList){
                        workStationIDList.add(Long.parseLong(element.getText().trim()));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(turnArea != null){
                    Iterator var13 = turnArea.entrySet().iterator();
                    while(var13.hasNext()) {
                        Entry<String, ArrayList<Long>> entry = (Entry)var13.next();
                        List<Long> tempList = entry.getValue();
                        LOG.info("旋转区点集："+tempList);
                        if(tempList != null && tempList.size() == 5 && tempList.get(0) == 0){
                            CellNode c1 = this.kivaMap.getMapCellByAddressCodeID(tempList.get(1));
                            CellNode c2 = this.kivaMap.getMapCellByAddressCodeID(tempList.get(2));
                            CellNode c3 = this.kivaMap.getMapCellByAddressCodeID(tempList.get(3));
                            CellNode c4 = this.kivaMap.getMapCellByAddressCodeID(tempList.get(4));
                            RotateArea rotate = new RotateArea(Long.parseLong((String)entry.getKey()), c1, c2, c3, c4);
                            rotateAreaList.add(rotate);
                        }
                        if(tempList != null && tempList.size() ==4){
                            long type_0 = tempList.get(0); // 旋转点
                            long type_1 = tempList.get(1); // 一次入口
                            long type_2 = tempList.get(2); // 二次入口
                            long type_3 = tempList.get(3); //出口
                            if(type_0 != AGVConfig.PRIVILEGE_ROTATION_CELL){
                                if(workStationIDList.contains(Long.parseLong(entry.getKey()))){
                                    IRotationArea rotationArea = new RotationArea(Integer.parseInt(entry.getKey()), type_0, Integer.parseInt(String.valueOf(WS_TOWARD.get(entry.getKey()) == null ? 0 : WS_TOWARD.get(entry.getKey()))));
                                    RotationAreaManager.getInstance().addRotationArea(rotationArea);
                                }else{
                                    TriangleRotateAreaNew triangleRotateAreaNew = new TriangleRotateAreaNew(Long.parseLong(String.valueOf(entry.getKey())), kivaMap.getMapCellByAddressCodeID(type_1), kivaMap.getMapCellByAddressCodeID(type_0));
                                    TriangleRotateAreaNewManager.getInstance().addTriangleRotateAreaNew(triangleRotateAreaNew);
                                }
                            }
                        }
                    }
                }

                //安装不可走点
                kivaMap.installNoWalkedList(new LinkedList<>(unWalkedCellList));
                //安装旋转区
                kivaMap.installRotateArea(rotateAreaList);
                //安装跟车直行点
                kivaMap.installFollowCellNode(new LinkedList<>(followCellList));

                // 安装不用于计算的锁格超时的地址码
                List<Long> unlockedCellTimeoutList = (List<Long>) mapMessage.get("unlockedCellTimeout");
                if(unlockedCellTimeoutList != null){
                    kivaMap.installUnlockedCellTimeoutCellNodes(new LinkedList<Long>(unlockedCellTimeoutList));
                }

                // 安装孤立点
                CellNode enterNode = kivaMap.getMapCellByAddressCodeID(AGVConfig.PRIVILEGE_ENTER_CELL);
                CellNode individualNode = kivaMap.getMapCellByAddressCodeID(AGVConfig.PRIVILEGE_ROTATION_CELL);
                if(enterNode != null
                        && individualNode != null){
                    IndividualCellNode individualCellNode = new IndividualCellNode(enterNode, individualNode);
                    IndividualCellNodeManager.getInstance().addIndividualCellNode(individualCellNode);
                }

                // 安装pods
                List<Map<Long, Long>> pods = (List<Map<Long, Long>>)mapMessage.get("pods");
                if(pods != null){
                    PodManager.getInstance().installCellNodePod(pods, kivaMap);
                }
                // 安装非存储区的pods
                List<Map<Long, Long>> unStoragePods = (List<Map<Long, Long>>)mapMessage.get("unStoragePods");
                if(unStoragePods != null){
                    PodManager.getInstance().installUnStoragePods(unStoragePods);
                }


                // 行走不同距离的点加载
                try {
                    List<Element> elementList = parser.getResList("/config/cells.xml", "/cells/cell");
                    if(elementList != null && elementList.size() > 0){
                        for(Element element : elementList){
                            String text = element.getText();
                            LOG.info("加载行走距离不同的点："+text);
                            String[] texts = text.split("-");
                            CellNode cellNode = kivaMap.getMapCellByAddressCodeID(Long.parseLong(texts[0].trim()));
                            if(cellNode != null){
                                cellNode.setUpDistance(Integer.parseInt(texts[1].trim()));
                                cellNode.setDownDistance(Integer.parseInt(texts[2].trim()));
                                cellNode.setLeftDistance(Integer.parseInt(texts[3].trim()));
                                cellNode.setRightDistance(Integer.parseInt(texts[4].trim()));
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    LOG.info("行走不同距离的点加载异常。。。", e);
                }


                // map安装好后注册cellNodeListener
                kivaMap.registeCellListener(this);

                //初始化界面
                /*PathViewApplicationForRCS application = new PathViewApplicationForRCS(this.kivaMap);
                application.visible();*/

                LOG.info("安装地图完成:sectionID="+mapMessage.get("sectionID")
                        +"\r\n 不可走点-unWalkedCellList="+unWalkedCellList
                        +"\r\n 旋转区 rotateAreaList="+rotateAreaList
                        +"\r\n安装跟车直行点followCellList="+followCellList
                        +"\r\n安装存储区PODS="+pods
                        +"\r\n安装非存储区PODS="+unStoragePods
                        +"\r\n安装不用于计算的锁格超时的地址码 unlockedCellTimeoutList="+unlockedCellTimeoutList);



            } catch (Exception var20) {
                this.LOG.error("地图初始化失败~服务关闭...\n"+ ExceptionUtil.getMessage(var20));
                var20.printStackTrace();
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }



//--------------------------------------------------------------------------------
    private void reciveMapMessageAndInstallMap_(String confPath) {
        String sectID = ConfigParser.parseConfigParmByPath(confPath, "config/section_id");
        if("".equals(sectID)) {
            System.exit(0);
        }

//        this.sectionID = Long.parseLong(sectID);
        String user = ConfigParser.parseConfigParmByPath(confPath, "config/rabbit/user");
        String password = ConfigParser.parseConfigParmByPath(confPath, "config/rabbit/password");
        String url = ConfigParser.parseConfigParmByPath(confPath, "config/rabbit/url");
        String portStr = ConfigParser.parseConfigParmByPath(confPath, "config/rabbit/port");
        int port = "".equals(portStr)?5672:Integer.parseInt(portStr);
        MQManager manager = new MQManager();
        boolean flag = manager.connect2mqServer(url, port, user, password);
        if(flag) {
            this.reciver = new MQReciver();
            this.reciver.Start();
            this.publisher = new MQPublisher();

            try {
                while(null == mapMessage) {
                    this.publisher.publishVirtualMapRequest();
                    Thread.sleep(1200L);
                    this.LOG.warn("正在尝试获取地图信息...");
                }

                List<Long> unWalkedCellList = (List<Long>) mapMessage.get("unWalkedCell");
                if (null == unWalkedCellList)
                {
                    unWalkedCellList = new LinkedList<>();
                }
                List<Long> followCellList = (List<Long>) mapMessage.get("followCell");
                if (null == followCellList)
                {
                    followCellList = new LinkedList<>();
                }
                HashMap<String, ArrayList<Long>> turnArea = (HashMap)mapMessage.get("stationAndTurnArea");
                this.kivaMap = new KivaMap(((Integer)mapMessage.get("row")).intValue(), ((Integer)mapMessage.get("column")).intValue());
                this.kivaMap.initKivaMap();
//                this.mapManager.installMap(this.kivaMap);
                LinkedList<RotateArea> rotateAreaList = new LinkedList();
                Iterator var13 = turnArea.entrySet().iterator();

                while(var13.hasNext()) {
                    Entry<String, ArrayList<Long>> entry = (Entry)var13.next();
                    CellNode c1 = this.kivaMap.getMapCellByAddressCodeID(((Long)((ArrayList)entry.getValue()).get(0)).longValue());
                    CellNode c2 = this.kivaMap.getMapCellByAddressCodeID(((Long)((ArrayList)entry.getValue()).get(1)).longValue());
                    CellNode c3 = this.kivaMap.getMapCellByAddressCodeID(((Long)((ArrayList)entry.getValue()).get(2)).longValue());
                    CellNode c4 = this.kivaMap.getMapCellByAddressCodeID(((Long)((ArrayList)entry.getValue()).get(3)).longValue());
                    RotateArea rotate = new RotateArea(Long.parseLong((String)entry.getKey()), c1, c2, c3, c4);
                    rotateAreaList.add(rotate);
                }

                //安装不可走点
                kivaMap.installNoWalkedList(new LinkedList<>(unWalkedCellList));
                //安装旋转区
                kivaMap.installRotateArea(rotateAreaList);
                //安装跟车直行点
                kivaMap.installFollowCellNode(new LinkedList<>(followCellList));

                // 安装不用于计算的锁格超时的地址码
                List<Long> unlockedCellTimeoutList = (List<Long>) mapMessage.get("unlockedCellTimeout");
                if(unlockedCellTimeoutList != null){
                    kivaMap.installUnlockedCellTimeoutCellNodes(new LinkedList<Long>(unlockedCellTimeoutList));
                }

                //初始化界面
                PathViewApplicationForRCS application = new PathViewApplicationForRCS(this.kivaMap);
                application.visible();

                LOG.info("安装地图完成:sectionID="+3
                        +"\r\n 不可走点-unWalkedCellList="+unWalkedCellList
                        +"\r\n 旋转区 rotateAreaList="+rotateAreaList
                        +"\r\n安装跟车直行点followCellList="+followCellList
                        +"\r\n安装不用于计算的锁格超时的地址码 unlockedCellTimeoutList="+unlockedCellTimeoutList);


            } catch (Exception var20) {
                this.LOG.error("地图初始化失败~服务关闭...\n"+ExceptionUtil.getMessage(var20));
                System.exit(0);
            }
        } else {
            System.exit(0);
        }

    }

    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {
        if(null != agv) {
            try {
                publisher.publishAGVStatus(agv.getID(), oldStatus, newStatus, System.currentTimeMillis() / 1000L);
                LOG.info("AGV-" + agv.getID() + "状态改变：" + oldStatus + "->" + newStatus);
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }
    }

    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {
        /*if(null != agv) {
            try {
                publisher.publishAGVPositionChange(agv.getID(), oldAddressIDCode, newAddressIDCode, System.currentTimeMillis() / 1000L);
                LOG.info("AGV" + agv.getID() + "位置改变 : " + oldAddressIDCode + "->" + newAddressIDCode);
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }*/

    }

    public void OnAGVOpenConnection2RCS(AGVMessage agv) {
        try {
            publisher.publishAGVConnect2RCS(agv, SubjectManager.RCS_ROBOT_CONNECT_RCS);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void OnAGVCloseConnection2RCS(AGVMessage agv) {
        try {
            this.publisher.publishCloseConnectionMessage(agv.getID());
            LOG.warn("机器<" + agv.getID() + ">断开到RCS的连接！");
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void OnAGVRepeatConnection2RCS(AGVMessage agv) {
        try {
            this.publisher.publishAGVConnect2RCS(agv, SubjectManager.ROBOT_RECONNECTED);
            LOG.warn("机器<" + agv.getID() + ">重新连接到RCS！");
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void onAGVBeatOrRTTimeout(KivaAGV agv) {
        try {
            publisher.publishAGVBeatOrRTTimeout(Long.valueOf(agv.getID()));
            LOG.warn("机器<" + agv.getID() + ">心跳或实时包没收到超时！！");
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void onAGVPositionNoChanageTimeout(KivaAGV agv, Map<String, Object> paramMap) {
        try {
            publisher.publishAGVNoMoveTimeOut(agv, paramMap);
            LOG.warn("机器<" + agv.getID() + ">位置不改变超时！！");
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv) {
        try {
            publisher.publishAGVReConnedAndPositionError(agv);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void OnAGVSPUnWalkedCell(AGVMessage agvMessage, CellNode cellNode) {
        try {
            publisher.publishTempUnWalkCell(agvMessage, cellNode.getAddressCodeID());
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void OnAGVRequestWCSPath(AGVMessage agvMessage) {
        try {
            publisher.publishRequestPath(agvMessage);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {
        try {
            publisher.publishChargingPileConnecting(agv.getID(), addressCodeID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAGVLockCellTimeout(KivaAGV kivaAGV, Map<String, Object> paramMap) {
        try {
            publisher.publishLockCellTimeoutOrRequestPath(
                    kivaAGV.getID(),
                    kivaAGV.getCurrentAddressCodeID(),
                    Long.parseLong(String.valueOf(paramMap.get("waitingLockAddressCodeID"))),
                    SubjectManager.RCS_WCS_LOCK_CELL_TIMEOUT);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data) {


        String type = "";

        try {
            this.publisher.publishActionCommandMessage(data);
            if(data.getCommandParameter() == -16) {
                type = "停到最近二维码，减速到0";
                this.publisher.publishAGVActionCommondResponse(Long.valueOf(data.getRobotID()),
                        SubjectManager.AGV_PARKING_RESPONSE);
                this.LOG.warn("------------停到最近二维码，减速到0回复-------------");
            } else if(data.getCommandParameter() == -15) {
                type = "急停";
                this.publisher.publishAGVActionCommondResponse(Long.valueOf(data.getRobotID()),
                        SubjectManager.URGENT_STOP_RESPONSE);
                this.LOG.warn("------------急停回复-------------");
            } else if(data.getCommandParameter() == -14) {
                type = "断电";
                this.publisher.publishAGVActionCommondResponse(Long.valueOf(data.getRobotID()),
                        SubjectManager.AGV_ALL_MOTOR_CUT_RESPONSE);
                this.LOG.warn("------------断电回复-------------");
            } else if(data.getCommandParameter() == 64) {
                type = "开始休眠";
                this.publisher.publishAGVActionCommondResponse(Long.valueOf(data.getRobotID()),
                        SubjectManager.AGV_START_SLEEP_RESPONSE);
                this.LOG.warn("--------------开始休眠回复-------------");
            } else if(data.getCommandParameter() == 65) {
                type = "结束休眠";
                this.publisher.publishAGVActionCommondResponse(Long.valueOf(data.getRobotID()),
                        SubjectManager.AGV_STOP_SLEEP_RESPONSE);
                this.LOG.warn("----------结束休眠回复--------------");
            } else if (data.getCommandParameter() == CommandActionTypeConfig.START_COMMAND) {
                //启动
                type = "启动";
                publisher.publishAGVActionCommondResponse(data.getRobotID(),
                        SubjectManager.WCS_RCS_AGV_START_RESPONSE);
                LOG.warn("----------启动回复--------------");
            }
        } catch (IOException var4) {
            this.LOG.warn("AGV动作命令消息<" + type + ">回复异常.");
            var4.printStackTrace();
        }

    }

    public void onReceivedAGVLoginMessage(RobotLoginRequestMessage data) {
        try {
            this.publisher.publishLoginMessage(data);
        } catch (IOException var3) {
            this.LOG.warn("机器->" + data.getRobotID() + "登录包发布异常..");
            var3.printStackTrace();
        }

    }

    public void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {
        // 取消心跳消息发送
        try {
            this.publisher.publishHeartBeatMessage(data);
        } catch (IOException var3) {
            this.LOG.warn("机器->" + data.getRobotID() + "心跳包发布异常..");
            var3.printStackTrace();
        }

    }

    public void onReceivedAGVRTMessage(RobotRTMessage data) {
        try {
//            updatePod(data);
            this.publisher.publishRTMessage(data);
        } catch (IOException var3) {
            this.LOG.warn("机器->" + data.getRobotID() + "实时包发布异常..");
            var3.printStackTrace();
        }

    }

    /**
     * 更新POD
     * @param data
     */
    private void updatePod(RobotRTMessage data) {
        IPod pod = PodManager.getInstance().getPod(data.getPodCodeID());
        if(pod != null){
            pod.setCellNode(MapManager.getInstance().getMap().getMapCellByAddressCodeID(data.getAddressCodeID()));
        }
        if(pod == null && data.getPodCodeID() > 0){
            PodManager.getInstance().addPod2Container(new Pod(data.getPodCodeID(), MapManager.getInstance().getMap().getMapCellByAddressCodeID(data.getAddressCodeID())));
        }
    }


    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {
        try {
            this.publisher.publishErrorMessage(data);
        } catch (IOException var3) {
            this.LOG.warn("机器->" + data.getRobotID() + "错误包发布异常..");
            var3.printStackTrace();
        }

    }

    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {
        try {
            this.publisher.publishStatusMessage(data);
        } catch (IOException var3) {
            this.LOG.warn("机器->" + data.getRobotID() + "周期性状态数据包发布异常..");
            var3.printStackTrace();
        }

    }

    public void onAGVBeatTimeout(KivaAGV agv) {
    }

    public void onAGVRTTimeout(KivaAGV agv) {
    }

    public void onServerStatusChanaged(ServerStatusMessage status) {
    }

    public void OnSendGlobalPath2AGV(AGVMessage agv, SeriesPath globalPath) {
    }

    public void OnAGVUnLockedCell(AGVMessage agv, LinkedList<CellNode> unLockedCellNodeList) {
    }

    public void OnAGVLockedCell(AGVMessage agv, LinkedList<CellNode> lockedCellNodeList) {
    }

    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv, SeriesPath globalPath) {
    }


    @Override
    public void onReceiveRobot2RCSActionFinishedMessageListener(Robot2RCSActionFinishedMessage data) {
        try {
            data.toObject();
            publisher.publishInitPodInfo(data);
            publisher.publishActionFinishedInfo(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data) {
        try {
            this.publisher.publishMediaErrorMessage(data);
            LOG.info(" < < < - - - 收到美的错误信息："+ HexBinaryUtil.byteArrayToHexString2((byte[]) data.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data) {
        try {
            this.publisher.publishResponseConfigMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCellWalkable(CellNode cellNode) {

    }

    @Override
    public void OnCellUnWalkable(CellNode cellNode) {

    }

    @Override
    public void OnCellUnLocked(CellNode unLockedCellNode) {
        try {
            if(unLockedCellNode != null){
                this.publisher.publishUnlockedCell(unLockedCellNode.getAddressCodeID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCellLocked(CellNode lockedCellNode) {

    }

    @Override
    public void OnCellInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath) {

    }

    @Override
    public void OnCellNoInSeriesPath(CellNode cellNode, SeriesPath globalSeriesPath) {

    }

    @Override
    public void OnCellCommonUpdate(CellNode cellNode) {

    }

    @Override
    public void onAddressCodeIdLostListener(long robotID, String lostMsg) {
        try {
            publisher.publishAddressCodeIdLostInfo(robotID, lostMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
