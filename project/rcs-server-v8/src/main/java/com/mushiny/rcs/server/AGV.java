/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.RCS2RobotPathMessage;
import com.mingchun.mu.log.AGVMessageSendLogger;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNode;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNodeManager;
import com.mingchun.mu.mushiny.kiva.path.IRotationArea;
import com.mingchun.mu.mushiny.kiva.path.RotationAreaManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNewManager;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.Map;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AGV
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public abstract class AGV extends AGVMessage {

    static Logger LOG = LoggerFactory.getLogger(AGV.class.getName());
    //////////////////路径相关///////////////////////
    private RCS2RobotPathMessage pathMessage;
    private SeriesPath lastSendedSeriesPath;//上一次下发给AGV的路径
    private SeriesPath currentSeriesPath;//目前下发给AGV的路径
    private final LinkedList<CellNode> tmpList = new LinkedList();
    private CellNode tmpCellNode;
    private CellNode targetLockCellNode;
    ///=============暂停当前路径下发标志位======================
    private boolean stopSendSeriesPathFlag = false;

    private List<CellNode> globalSeriesPath = new CopyOnWriteArrayList<>();//执行的完整路径串

    private Lock lock = new ReentrantLock();




    public AGV() {
        super();
    }
    public synchronized void clearCurrentGlobalSeriesPath() {

        if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getPathList() != null){
            for(CellNode cellNode : currentGlobalSeriesPath.getPathList()){
                cellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }
        }

        /*if(getLastSendedSeriesPath() != null){
            for(CellNode cellNode : getLastSendedSeriesPath().getPathList()){
                if(cellNode.getAddressCodeID() != getCurrentAddressCodeID()){
                    cellNode.setUnLocked_MapLock(getLastSendedSeriesPath());
                }
            }
        }*/

        currentGlobalSeriesPath=null;
        tempNextGlobalSeriesPath = null;
    }
    public   void clearGlobalSeriesPath() {
        seriesPathLinkedList.clear();
    }



    public abstract void clearAGVBufferPathCommand();
    public abstract void clearAndUnlockBufferSP();

    public void lockTimeoutHandle(){
        if(isLockTimeout()){
            java.util.Map<String, Object> paramMap = new HashMap<>();
            long waitingLockAddressCodeID = 0;
            if(getCurrentGlobalSeriesPath() != null
                    && getCurrentGlobalSeriesPath().getPathList() != null
                    && getCurrentGlobalSeriesPath().getPathList().size() > 1){
                waitingLockAddressCodeID = getCurrentGlobalSeriesPath().getPathList().get(1).getAddressCodeID();
            }
            paramMap.put("waitingLockAddressCodeID", waitingLockAddressCodeID);
            CellNode waitingLockCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(waitingLockAddressCodeID);
            if(waitingLockCellNode != null
                    && !waitingLockCellNode.isUnlockedTimeout()
                    && (!this.equals(waitingLockCellNode.getNowLockedAGV()))){
                IPod pod = PodManager.getInstance().getPod(waitingLockCellNode);
                if(pod != null){
                    paramMap.put("nextLockAddressPodCodeID", pod.getPodCodeID());
                }

                clearAndUnlockBufferSP();
                currentSeriesPath = null;
                LOG.info("小车("+getID()+")重新请求，清除所有缓冲路径！！！");

                AGVTimeoutManager.getInstance().fireOnAGVLockCellTimeout((KivaAGV) this, paramMap);
                LOG.info("AGV("+getID()+")锁格超时重新请求路径！！！");

                List<CellNode> cellNodeList = new ArrayList<>();
                if(waitingLockCellNode.isLocked()){
                    cellNodeList.add(waitingLockCellNode);
                }
                cellNodeList.add(MapManager.getInstance().getMap().getMapCellByAddressCodeID(getCurrentAddressCodeID()));
                MapManager.getInstance().getMap().changingCellsCost(cellNodeList, true);
            }

            getAgvCheckItem().setLastLockCellTime(System.currentTimeMillis());
            setLockTimeout(false);
        }
    }


    private TriangleRotateAreaNewManager rotateAreaNewManager = TriangleRotateAreaNewManager.getInstance();
    private void createLockCellsNum(){ // 更改旋转区的锁格数目
        setLockCellsNum(AGVConfig.AGV_LOCKED_MAX_COUNTS + 1);
        if(rotateAreaNewManager.isInTriangleRotateAreaNew(currentAddressCodeID)){
            setLockCellsNum(2);
        }
    }


    @Override
    public synchronized void runGlobalSeriesPathAndLockCellCheck() {
        createLockCellsNum();
        lockTimeoutHandle();
        runGlobalSeriesPathAndLockCellCheck_();
    }
    /*
       运行全局路径并且锁格检查 -- original
     */
    public synchronized void runGlobalSeriesPathAndLockCellCheck_() {

        if (currentGlobalSeriesPath == null) {
            showAGVDebugMessage(LOG, "####AGV(" + getID() + ")无要运行的路径!,目前RCS缓冲路径数量="+seriesPathLinkedList.size());
            return;
        }
        if (currentGlobalSeriesPath.getCellListSize() == 0) {
            showAGVDebugMessage(LOG, "####AGV(" + getID() + ")路径CELL数量为0!,目前RCS缓冲路径数量="+seriesPathLinkedList.size());
            return;
        }
        if (isRtTimeout()) {
            LOG.warn("####AGV(" + getID() + ")处于数据超时状态!,目前RCS缓冲路径数量="+seriesPathLinkedList.size());
            return;
        }
        if (getAGVStatus() == AGVConfig.AGV_STATUS_NO_CONNECTION) {
            LOG.warn("####AGV(" + getID() + ")处于TCP连接断开状态!,目前RCS缓冲路径数量="+seriesPathLinkedList.size());
            return;
        }
        if (isStopSendSeriesPathFlag()) {
            LOG.warn("####AGV(" + getID() + ")处于路径停止下发状态!,目前RCS缓冲路径数量="+seriesPathLinkedList.size());
            return;
        }
        //========================释放AGV已经跑过的CELL=========================
        unlockCellFromSP();

        IndividualCellNode individualCellNode = IndividualCellNodeManager.getInstance().getIndividualCellNodeByIndividualCellNode(currentCellNode);
        if(individualCellNode != null){
            individualCellNode.getEnterNode().checkAndLocked_MapLock((KivaAGV) this);
        }


        //=========================形成路径并且下发=============================
        if(currentGlobalSeriesPath.getPathList().size() == 0){ // 获取到地址码异常，导致路径全部解锁，不用重发路径
            currentSeriesPath = null;
            lastSendedSeriesPath = null;
            clearBufferSP(); // 清除所有已下发任务
            LOG.info("AGV("+getID()+")获取到地址码异常，导致路径全部解锁，清除所有路径，不用向小车重发路径！！！");
        }else{
            createLockedSP();
        }
        //=========================控制下发条件和频率===========================
//        if(count <= 100)
         sendPathOncondition();
    }


    public void sendPathOncondition(){
        //=========================控制下发条件和频率===========================
        if (getCurrentSeriesPath() != null) {
            if (getCurrentSeriesPath().getCellListSize() > 0) {
                if (!isSamePreviousSeriesPath(currentSeriesPath)) {//和上一次的路径是否相同
                    sendPath();
                }else {
                    if(getPathResponse() != 0){ // 路径非正常应答
                        if (getPathResponse() == 1) { // 收到异常应答包
//                            clearAGVBufferPathCommand(); //清除小车缓冲路径
                            sendPath();
                            LOG.info("AGV("+getID()+")收到路径异常应答  清空小车路径， 重新下发路径: "+getCurrentSeriesPath());
                        }else {
                            if(System.currentTimeMillis() - getSendedPathTime() > AGVConfig.TIMEOUT_PATH_REGAIN_TIME){ // 没有收到应答  按指定时间再次下发路径
                                sendPath();
                                LOG.info("AGV("+getID()+")没有收到应答  按指定时间（"+AGVConfig.TIMEOUT_PATH_REGAIN_TIME+"）再次下发路径: "+getLastSendedSeriesPath().toString());
                            }
                        }
                    }
                }
            }
        }
    }


//    private int count = 0;
    private void sendPath(){
//        LOG.info(" - - - - > > > > AGV("+getID()+")路径已下发：" + currentSeriesPath);
        sendSeriesPath(getCurrentSeriesPath());
        setSendedPathTime(System.currentTimeMillis());
//        count++;
        setPathResponse(-1); // 路径下发后，等待回复
        lastSendedSeriesPath = getCurrentSeriesPath();
        setSendSPCountForTimeout(1);
        getAgvCheckItem().setLastLockCellTime(System.currentTimeMillis());  // 新路径下发，更新最后锁格时间
        getAgvCheckItem().setLastPositionChanageTime(System.currentTimeMillis()); // 新路径下发，更新最后位置不改变时间
    }

    /*
     形成路径
     */
    public void createLockedSP() {
        //-- currentSeriesPath = new SeriesPath(currentGlobalSeriesPath.getUuid());
        LOG.info("AGV("+getID()+") 创建锁格开始 - - - - - ");
        LOG.info("AGV("+getID()+") currentGlobalSeriesPath="+currentGlobalSeriesPath);
        SeriesPath tmpSeriesPath = new SeriesPath(currentGlobalSeriesPath.getUuid());
        for (int i = 1; i <= getLockCellsNum(); i++) {
            if ((i - 1) < currentGlobalSeriesPath.getCellListSize()) {
                tmpCellNode = currentGlobalSeriesPath.getCellNodeByIndex(i - 1);

                if(isLift()){ // 举升锁格逻辑

                    if (((i == 1)||(i > 1 && PodManager.getInstance().getPod(tmpCellNode) == null)) && tmpCellNode.checkAndLocked_MapLock((KivaAGV) this)) {
                        tmpSeriesPath.addPathCell(tmpCellNode);
                        LOG.info("#### AGV(" + getID() + ")格子可锁=" + tmpCellNode);

                        lockRotationArea();

//                    showAGVDebugMessage(LOG, "## AGV(" + getID() + ")格子可锁=" + tmpCellNode + "(AGV_ID=" + getID() + ")");
                    } else {
                        if(tmpCellNode.getNowLockedAGV() != null){
                            LOG.info("#### AGV(" + getID() + ")格子不可锁=" + tmpCellNode.getAddressCodeID() + "被锁AGV_ID=" + ((MapCellNode) tmpCellNode).getNowLockedAGV().getID());
                        }

//                    showAGVDebugMessage(LOG, "#### AGV(" + getID() + ")格子不可锁=" + tmpCellNode + "被锁AGV_ID=" + ((MapCellNode) tmpCellNode).getNowLockedAGV() + ",(AGV_ID=" + getID() + ")");
//                    pathMessage.setSeriesPath(getCurrentSeriesPath());
                        if (tmpSeriesPath.getCellListSize() > 0) {
                            currentSeriesPath = tmpSeriesPath;
                            LOG.info("#### AGV(" + getID() + ") 创建锁格下发路径成功：currentSeriesPath="+currentSeriesPath);
                        }
                        break;
                    }
                    currentSeriesPath = tmpSeriesPath;
                }else{
                    if (tmpCellNode.checkAndLocked_MapLock((KivaAGV) this)) {
                        tmpSeriesPath.addPathCell(tmpCellNode);
                        LOG.info("#### AGV(" + getID() + ")格子可锁=" + tmpCellNode);

                        lockRotationArea();

//                    showAGVDebugMessage(LOG, "## AGV(" + getID() + ")格子可锁=" + tmpCellNode + "(AGV_ID=" + getID() + ")");
                    } else {

                        if(tmpCellNode.getNowLockedAGV() != null){
                            LOG.info("#### AGV(" + getID() + ")格子不可锁=" + tmpCellNode.getAddressCodeID() + "被锁AGV_ID=" + ((MapCellNode) tmpCellNode).getNowLockedAGV().getID());
                        }

//                    showAGVDebugMessage(LOG, "#### AGV(" + getID() + ")格子不可锁=" + tmpCellNode + "被锁AGV_ID=" + ((MapCellNode) tmpCellNode).getNowLockedAGV() + ",(AGV_ID=" + getID() + ")");
//                    pathMessage.setSeriesPath(getCurrentSeriesPath());
                        if (tmpSeriesPath.getCellListSize() > 0) {
                            currentSeriesPath = tmpSeriesPath;
                            LOG.info("#### AGV(" + getID() + ") 创建锁格下发路径成功：currentSeriesPath="+currentSeriesPath);
                        }
                        break;
                    }
                    currentSeriesPath = tmpSeriesPath;
                }
            }
        }
        LOG.info("AGV("+getID()+") 创建锁格结束 - - - - - ");

    }

    private void lockRotationArea(){
        IRotationArea rotationArea = RotationAreaManager.getInstance().getRotationArea(tmpCellNode.getAddressCodeID());
        if(rotationArea != null){
            rotationArea.lockRotationArea((KivaAGV) this);
        }
    }

    /*
     解锁已经跑过的CELL
     */
    public synchronized void unlockCellFromSP() {
        LOG.info("AGV("+getID()+") 解锁开始 - - - - - ");
        LOG.info("AGV("+getID()+") currentGlobalSeriesPath="+currentGlobalSeriesPath);
        if (getLastSendedSeriesPath() != null) {
            int indexUnlocked = 0;
            LOG.info("####当前AGV位置=" + getCurrentAddressCodeID() + "(AGV_ID=" + getID() + ")");
            LOG.info("####当前路径剩余CELL数量=" + currentGlobalSeriesPath.getPathList() + "(AGV_ID=" + getID() + ")");

//            showAGVDebugMessage(LOG, "####当前AGV位置=" + getCurrentAddressCodeID() + "(AGV_ID=" + getID() + ")");
//            showAGVDebugMessage(LOG, "####当前路径剩余CELL数量=" + currentGlobalSeriesPath.getPathList() + "(AGV_ID=" + getID() + ")");
            for (CellNode cellNode : currentGlobalSeriesPath.getPathList()) {
                if (getCurrentAddressCodeID() == cellNode.getAddressCodeID()) {
                     break;
                }
                indexUnlocked++;
            }

            /*// mingchun.mu@mushiny.com -- 实时包中的地址码是否正确
            boolean isRightAddressCode = false;
            for(CellNode node: currentGlobalSeriesPath.getPathList()){
                if(getCurrentAddressCodeID() == node.getAddressCodeID()){
                    isRightAddressCode = true;
                    break;
                }
            }
            if(!isRightAddressCode){
                indexUnlocked = 0;
            }
            // mingchun.mu@mushiny.com --------------------------*/


            LOG.info("####解锁数量=" + indexUnlocked + "(AGV_ID=" + getID() + ")");

//            showAGVDebugMessage(LOG, "####解锁数量=" + indexUnlocked + "(AGV_ID=" + getID() + ")");
            tmpList.clear();

            for (int i = 0; i < indexUnlocked; i++) {
                tmpCellNode = currentGlobalSeriesPath.getPathList().remove(0);
                List<RobotAction> actionList = tmpCellNode.getRobotActionList(getLastSendedSeriesPath());
                if (tmpCellNode.setUnLocked_MapLock(getLastSendedSeriesPath())) {
                    LOG.info("AGV("+getID()+") --  ##格子(" + tmpCellNode.getAddressCodeID() + ")解锁  移除格子动作（"+actionList+"）  并从路径（"+currentGlobalSeriesPath+"）中释放  ");
//                    showAGVDebugMessage(LOG, "##格子(" + tmpCellNode.getAddressCodeID() + ")解锁" + "(AGV_ID=" + getID() + ")");
                    tmpList.addLast(tmpCellNode);
                }
                tmpCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
            }
            LOG.info("AGV("+getID()+") 解锁结束 - - - - - ");


            if (!tmpList.isEmpty()) {
                fireOnAGVUnLockedCell(tmpList);
            }
        }
    }



    public void clearBufferSP(){

    }

    @Override
    public void errorPodHandle(){
        if(getLastSendedSeriesPath() != null && getLastSendedSeriesPath().getPathList() != null){
            for(CellNode cellNode: getLastSendedSeriesPath().getPathList()){
                if(!currentCellNode.equals(cellNode)){
                    cellNode.setUnLocked();
                    LOG.info("pod扫描有误：小车自动清除已经下发路径，rcs将已下发路径解锁，解锁格子("+cellNode.getAddressCodeID()+")");
                }
            }
        }
        clearBufferSP();
        setLastSendedSeriesPath(null);

        // 没有扫描到pod， 将小车置为下降状态
        setLift(false);
        LOG.info("AGV("+getID()+")没有扫描到所需pod， 将小车置为下降状态，更改举升状态isLift="+isLift());

    }

    /*
     判断指定的路径与上一次的路径是否"相同"
     */
    public boolean isSamePreviousSeriesPath(SeriesPath sp) {
        if (getLastSendedSeriesPath() == null) {
            return false;
        }
        if (getLastSendedSeriesPath().getCellListSize() != sp.getCellListSize()) {
            return false;
        }
        int count = getLastSendedSeriesPath().getCellListSize();
        for (int i = 0; i < count; i++) {
            if (getLastSendedSeriesPath().getCellNodeByIndex(i).getAddressCodeID() != sp.getCellNodeByIndex(i).getAddressCodeID()) {
                return false;
            }
        }
        return true;
    }

    public void sendSeriesPath(SeriesPath sp) {
//        showAGVDebugMessage(LOG, "####下发路径信息:" + sp + "(AGV_ID=" + getID() + ")");
        AGVMessageSendLogger.logInfo(" - - - - > > > > AGV("+getID()+")路径已下发：" + currentSeriesPath);
        pathMessage = new RCS2RobotPathMessage(getID());
        pathMessage.setSeriesPath(getCurrentSeriesPath());
        pathMessage.toMessage();
        lockedLastCellAddressCodeID = getCurrentSeriesPath().getPathList().getLast().getAddressCodeID();
//        LOG.info("####下发路径指令(AGV_ID=" + getID() + "):" + HexBinaryUtil.byteArrayToHexString2((byte[]) pathMessage.getMessage()));
        sendMessageToAGV(pathMessage);
        AGVMessageSendLogger.logInfo("####下发路径指令(AGV_ID=" + getID() + "):" + HexBinaryUtil.byteArrayToHexString2((byte[]) pathMessage.getMessage()));
    }

    /*
     put WCS路径
     */
    public synchronized void putWCSSeriesPath(WCSSeriesPath wCSSeriesPath) {

        LOG.info("AGV("+getID()+") 获取到新路径 --> "+wCSSeriesPath);

        if(wCSSeriesPath==null) {
            LOG.error("####AGV接受到的WCS下发路径为空！,舍弃执行！！");
            return;
        }
        if (!wCSSeriesPath.checkWCSSeriesPath()) {
            LOG.error("####AGV接受到的WCS下发路径错误！,舍弃执行！！");
            return;
        }
        if (wCSSeriesPath.getAgvID() != getID()) {
            LOG.error("####AGV接受到的WCS下发路径错误！,路径与AGVID不对应！！，舍弃执行！！");
            return;
        }


        LinkedList<SeriesPath> spList = wCSSeriesPath.getRSSeriesPathList();
        if (spList == null) {
            LOG.error("####AGV接受到的WCS下发路径错误！,舍弃执行！！");
            return;
        }

/*
        if(seriesPathLinkedList != null && seriesPathLinkedList.size() > 0){
            if(!tmpList.getFirst().equals(seriesPathLinkedList.get(seriesPathLinkedList.size() - 1))){
                LOG.error("####AGV下发的路径与上一次的终点不一致！！");
                return;
            }
        }
*/

        for (SeriesPath tmp : spList) {
            putGlobalSeriesPath(tmp);

           /* for(CellNode cellNode: tmp.getPathList()){
                globalSeriesPath.add(cellNode);
            }*/
        }

        pathTargetAddressCodeID = wCSSeriesPath.getAddressCodeIDList().getLast();
        for (SeriesPath tmp : spList) {
            for(CellNode cellNode: tmp.getPathList()){
                globalSeriesPath.add(cellNode);
            }
        }


        showAGVDebugMessage(LOG, "####AGV(ID" + getID() + ")收到WCS路径串压栈!");
        //--globalSeriesPathCheck();
    }

    /**
     * @return the stopSendSeriesPathFlag
     */
    public boolean isStopSendSeriesPathFlag() {
        return stopSendSeriesPathFlag;
    }

    /**
     * @param stopSendSeriesPathFlag the stopSendSeriesPathFlag to set
     */
    public void setStopSendSeriesPathFlag(boolean stopSendSeriesPathFlag) {
        this.stopSendSeriesPathFlag = stopSendSeriesPathFlag;
    }

    /**
     * @return the currentSeriesPath
     */
    public SeriesPath getCurrentSeriesPath() {
        return currentSeriesPath;
    }

    /**
     * @return the lastSendedSeriesPath
     */
    public SeriesPath getLastSendedSeriesPath() {
        return lastSendedSeriesPath;
    }

    public void setLastSendedSeriesPath(SeriesPath sp) {
        this.lastSendedSeriesPath = sp;
    }
    public abstract void setSendSPCountForTimeout(int sendSPCountForTimeout) ;
    

}
