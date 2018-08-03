package view;

import com.aricojf.platform.mina.common.MinaConfig;
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
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.RCSLogConfig;
import com.mushiny.rcs.listener.AGVListener;
import com.mushiny.rcs.listener.AGVTimeoutListener;
import com.mushiny.rcs.listener.CellListener;
import com.mushiny.rcs.listener.RCSListenerManager;
import com.mushiny.rcs.server.*;
import com.mushiny.rcs.wcs.WCSChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import mq.RCSMainServer;
import mq.SubjectManager;
import mq.XmlParser;
import org.apache.mina.util.CopyOnWriteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.bean.PathBean;
import view.bean.ViewAGVPathContainer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/13 0013.
 */
public class PathConfigMainServer implements ServerStatusChanagedMessageListener, AGVListener,
        OnReceiveAGVAllMessageListener, AGVTimeoutListener, CellListener,
        AddressCodeIdLostListener {
    private static PathConfigMainServer instance = new PathConfigMainServer();
    private Logger LOG = LoggerFactory.getLogger(RCSMainServer.class.getName());
    private KivaMap kivaMap;
    private final MapManager mapManager = MapManager.getInstance();
    private ServerMessageService kivaServer;

    private volatile Map<Long, ViewAGVPathContainer> agvPathContainerMap = new CopyOnWriteMap<>();

    private volatile int row;
    private volatile int col;

    public static PathConfigMainServer getInstance() {
        return instance;
    }

    public void stopServer(){
        this.kivaServer.Stop();
    }

    private XmlParser parser = new XmlParser();
    public void startServer() {
        String rcsConfigPath = SubjectManager.RCS_CONFIG_PATH;
        MinaConfig.PORT = Integer.parseInt(parser.getText(rcsConfigPath, "config/mina_port"));
        if(null != rcsConfigPath && !"".equals(rcsConfigPath)) {
            RCSLogConfig.getInstance().setRcsConfigFile(rcsConfigPath);
            RCSLogConfig.getInstance().initRCSGlobalParameter();
            this.reciveMapMessageAndInstallMap();
            this.initMinaServer();
            RCSTimer.getInstance().start();
        } else {
            this.LOG.error("----RCS配置文件解析错误，程序退出-------");
        }

    }


    private void reciveMapMessageAndInstallMap() {
        this.kivaMap = new KivaMap(row, col);
        this.kivaMap.initKivaMap();
        this.mapManager.installMap(this.kivaMap);
    }

    private void initMinaServer() {
        this.kivaServer = ServerManager.getMessageServerInstance();
        this.kivaServer.registeAGVAllMessageListener(this);

        RCSStatusService.getInstance().registerRCSStatusListener(this);
        RCSListenerManager.getInstance().registeAGVListener(this);
        RCSListenerManager.getInstance().registeAGVTimeoutListener(this);
        RealTimeMessageLost.getInstance().registerAddressCodeIdLostListener(this);
        this.kivaServer.Begin();
    }


    public void sendPath(PathBean pathBean){
        if(pathBean == null){
            return;
        }
        AutoPathView.getInstance().printLogAndClearLogBySize("RCS接收到路径:"+pathBean);
        long robotId = pathBean.getRobotId();
        long upAddressCodeId = pathBean.getUpAddressCodeId();
        Long downAddressCodeId = pathBean.getDownAddressCodeId();
        boolean isRotatePod = false ;
        int rotateTheta = pathBean.getRotateTheta();
        List<Long> seriesPath = pathBean.getPathList();
        KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotId);
        if (null != curAGV && seriesPath != null){
            WCSSeriesPath wcsSeriesPath = new WCSSeriesPath(upAddressCodeId, downAddressCodeId, isRotatePod, rotateTheta, new LinkedList<>(seriesPath));
            wcsSeriesPath.setPodCodeID(pathBean.getPodCodeId());// 设置路径中小车举升或下降的podID
            wcsSeriesPath.setAgvID(curAGV.getID());
            if (wcsSeriesPath.checkWCSSeriesPath()){
                curAGV.putWCSSeriesPath(wcsSeriesPath);
            }else {
                AutoPathView.getInstance().printLogAndClearLogBySize("收到的WCS路径不合法！！！！");
            }
        }else{
            AutoPathView.getInstance().printLogAndClearLogBySize("当前小车的路径下发异常，小车断开连接");
        }
    }


    public void sendChargingPath(PathBean pathBean){
        if(pathBean == null){
            return;
        }
        AutoPathView.getInstance().printLogAndClearLogBySize("RCS接收到充电路径:"+pathBean);
        long robotId = pathBean.getRobotId();
        int rotateTheta = pathBean.getRotateTheta();
        List<Long> seriesPath = pathBean.getPathList();
        KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotId);
        if (null != curAGV && seriesPath != null){
            WCSChargeSeriesPath chargeSeriesPath = new WCSChargeSeriesPath(rotateTheta, new LinkedList<>(seriesPath));
            chargeSeriesPath.setAgvID(curAGV.getID());
            curAGV.setBatterManufacturerNumber(2);
            if (chargeSeriesPath.checkWCSSeriesPath()){
                curAGV.putWCSSeriesPath(chargeSeriesPath);
            }else {
                AutoPathView.getInstance().printLogAndClearLogBySize("收到的WCS路径不合法！！！！");
            }
        }else{
            AutoPathView.getInstance().printLogAndClearLogBySize("当前小车的路径下发异常，小车断开连接");
        }
    }

    @Override
    public void onAddressCodeIdLostListener(long robotID, String lostMsg) {

    }

    @Override
    public void onReceiveRobot2RCSResponseConfigMessageListener(RobotResponseConfigMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseMessage(Robot2RCSResponseMessage data) {

    }

    @Override
    public void OnReceiveRobot2RCSResponseExceptionMessage(Robot2RCSResponseExceptionMessage data) {

    }

    @Override
    public void onReceiveRobot2RCSMediaErrorMessageListener(Robot2RCSMidiaErrorMessage data) {

    }

    @Override
    public void onReceivedAGVRTMessage(RobotRTMessage data) {

    }

    @Override
    public void onReceivedAGVErrorMessage(RobotErrorMessage data) {

    }

    @Override
    public void onReceivedAGVLoginMessage(RobotLoginRequestMessage data) {
        if(AGVManager.getInstance().getAGVByID(data.getRobotID()) != null){
            AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+data.getRobotID()+"登录成功！");
            AGVManager.getInstance().getAGVByID(data.getRobotID()).sendLoginOKMessage();
        }
    }

    @Override
    public void onReceivedAGVStatusMessage(RobotStatusMessage data) {
        if(getAgvPathContainerMap().get(data.getRobotID()) != null
                &&(getAgvPathContainerMap().get(data.getRobotID()).getExecutingPathFlag() + 1 == getAgvPathContainerMap().get(data.getRobotID()).getPathList().size())){
            AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+data.getRobotID()+" -- 剩余电量(%):"+(data.getShengYuDianLiang()/10) +", 当前电压(mv):"+(data.getBatteryVoltage()*10)); // 转完一圈显示小车的电池电压
        }
    }

    @Override
    public void onReceivedAGVHeartBeatMessage(RobotHeartBeatRequestMessage data) {

    }

    @Override
    public void onServerStatusChanaged(ServerStatusMessage status) {

    }

    @Override
    public void onReceivedAGVActionCommandResponseMessage(Robot2RCSActionCommandResponseMessage data) {

    }

    private long curAddrCache = 0;
    private long curRobotIdCache = 0;
    @Override
    public void onReceiveRobot2RCSActionFinishedMessageListener(Robot2RCSActionFinishedMessage data) {
        if(data.getActedType() == (byte) 0x0f && (curRobotIdCache != data.getRobotID() || curAddrCache != data.getAddressCodeID())){
            curAddrCache = data.getAddressCodeID();
            curRobotIdCache = data.getRobotID();
            AutoPathView.getInstance().printLogAndClearLogBySize("收到(小车ID="+data.getRobotID()+")整条路径结束数据包！");
            final ViewAGVPathContainer viewAGVPathContainer = agvPathContainerMap.get(data.getRobotID());

            if(viewAGVPathContainer.getPathList() != null
                    && viewAGVPathContainer.getPathList().size() == 1){
                PathBean tempPathBean = viewAGVPathContainer.getPathList().get(0);
                if(tempPathBean.getPathList().get(0) == tempPathBean.getPathList().get(tempPathBean.getPathList().size() - 1)){
                    curAddrCache = 0;
                    curRobotIdCache = 0;
                }
            }

            if(viewAGVPathContainer != null
                    && viewAGVPathContainer.isAutoSendFlag()
                    && viewAGVPathContainer.getPathList() != null
                    && viewAGVPathContainer.getPathList().size() > 0){
                if(viewAGVPathContainer.getExecutingPathFlag() + 1 == viewAGVPathContainer.getPathList().size()){
                    KivaAGV agv = AGVManager.getInstance().getAGVByID(data.getRobotID());
                    viewAGVPathContainer.setExecutingPathFlag(0);
                }else{
                    viewAGVPathContainer.setExecutingPathFlag(viewAGVPathContainer.getExecutingPathFlag() + 1);
                }
                PathBean sendPathBean = viewAGVPathContainer.getPathList().get(viewAGVPathContainer.getExecutingPathFlag());
                if(sendPathBean.getWaitTime() > 0){
                    new Thread(new WaitingPathSender(sendPathBean)).start();
                }else{
                    sendPath(sendPathBean);
                    AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+data.getRobotID()+"自动循环路径已下发(+)！");
                }
            }
        }
    }

    class WaitingPathSender implements Runnable{
        private PathBean pathBean;
        public WaitingPathSender(PathBean pathBean) {
            this.pathBean = pathBean;
        }

        @Override
        public void run() {
            try {
                AutoPathView.getInstance().printLogAndClearLogBySize("小车ID=" + pathBean.getRobotId() + "将在此等待" + pathBean.getWaitTime() + "ms 。。。");
                Thread.sleep(pathBean.getWaitTime());
                sendPath(pathBean);
                AutoPathView.getInstance().printLogAndClearLogBySize("小车ID=" + pathBean.getRobotId() + "停止" + pathBean.getWaitTime() + "ms后，自动循环路径已下发！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAGVBeatTimeout(KivaAGV agv) {

    }

    @Override
    public void onAGVRTTimeout(KivaAGV agv) {

    }

    @Override
    public void onAGVBeatOrRTTimeout(KivaAGV agv) {

    }

    @Override
    public void onAGVPositionNoChanageTimeout(KivaAGV agv, Map<String, Object> paramMap) {

    }

    @Override
    public void onAGVLockCellTimeout(KivaAGV agv, Map<String, Object> paramMap) {
        long robotId = agv.getID();
        AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+robotId+"锁格超时，请求重新下发！");
        long curAddr = agv.getCurrentAddressCodeID();
        ViewAGVPathContainer viewAGVPathContainer = agvPathContainerMap.get(robotId);
        if(viewAGVPathContainer != null
                && viewAGVPathContainer.getPathList() != null
                && viewAGVPathContainer.getPathList().size() > 0){
            if(viewAGVPathContainer.getExecutingPathFlag() < viewAGVPathContainer.getPathList().size()){
                PathBean tempPathBean = viewAGVPathContainer.getPathList().get(viewAGVPathContainer.getExecutingPathFlag());
                PathBean pathBean = new PathBean();
                pathBean.setRobotId(robotId);
                pathBean.setUpAddressCodeId(tempPathBean.getUpAddressCodeId());
                pathBean.setDownAddressCodeId(tempPathBean.getDownAddressCodeId());
                pathBean.setPathList(tempPathBean.getPathList().subList(tempPathBean.getPathList().indexOf(curAddr), tempPathBean.getPathList().size()));
                sendPath(pathBean);
                AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+robotId+"锁格超时重新下发："+pathBean);
            }
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
    public void OnSendGlobalPath2AGV(AGVMessage agv, SeriesPath globalPath) {

    }

    @Override
    public void OnAGVUnLockedCell(AGVMessage agv, LinkedList<CellNode> unLockedCellNodeList) {

    }

    @Override
    public void OnAGVLockedCell(AGVMessage agv, LinkedList<CellNode> lockedCellNodeList) {

    }

    @Override
    public void OnAGVArriveAtGlobalPathTargetCell(AGVMessage agv, SeriesPath globalPath) {

    }

    @Override
    public void OnAGVStatusChange(AGVMessage agv, int oldStatus, int newStatus) {

    }

    @Override
    public void OnAGVPositionChange(AGVMessage agv, long oldAddressIDCode, long newAddressIDCode) {

    }

    @Override
    public void OnAGVOpenConnection2RCS(AGVMessage agv) {

    }

    @Override
    public void OnAGVCloseConnection2RCS(AGVMessage agv) {
        AutoPathView.getInstance().printLogAndClearLogBySize("小车ID="+agv.getID()+"断开连接！");
    }

    @Override
    public void OnAGVRepeatConnection2RCS(AGVMessage agv) {

    }

    @Override
    public void OnAGVRepeatConnection2RCS_PositionError(AGVMessage agv) {

    }

    @Override
    public void OnAGVSPUnWalkedCell(AGVMessage agv, CellNode cellNode) {

    }

    @Override
    public void OnAGVRequestWCSPath(AGVMessage agv) {

    }

    @Override
    public void onArrivedChargingPile(AGVMessage agv, long addressCodeID) {

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ServerMessageService getKivaServer() {
        return kivaServer;
    }

    public Map<Long, ViewAGVPathContainer> getAgvPathContainerMap() {
        return agvPathContainerMap;
    }

}
