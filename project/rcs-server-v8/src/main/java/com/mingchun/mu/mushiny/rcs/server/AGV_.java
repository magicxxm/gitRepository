/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*

package com.mingchun.mu.mushiny.rcs.server;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.RCS2RobotPathMessage;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AGV_ extends AGVMessage_ {

    static Logger LOG = Logger.getLogger(AGV_.class.getName());
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




    public AGV_() {
        super();
    }
    public  void clearCurrentGlobalSeriesPath() {

        if(currentGlobalSeriesPath != null && currentGlobalSeriesPath.getPathList() != null){
            for(CellNode cellNode : currentGlobalSeriesPath.getPathList()){
                cellNode.setNoInGlobalPath(currentSeriesPath);
            }
        }

        currentGlobalSeriesPath=null;
    }
    public   void clearGlobalSeriesPath() {
        seriesPathLinkedList.clear();
    }



    // mingchun.mu@mushiny.com  -->  如果pod扫描错误  清空路径  不在执行
    private int podCounts;
    @Override
    public void runGlobalSeriesPathAndLockCellCheck(){
        if(currentGlobalSeriesPath == null){
            return;
        }
        if(isSendedCurrentGlobalSeriesPath && currentGlobalSeriesPath.getUpCellNode() != null && currentAddressCodeID == globalPathFirstCellNode.getAddressCodeID()){ // 当前路径中存在举升点(第一个点)
            LOG.info("podID扫描正确条件：currentAddressCodeID："+currentAddressCodeID+"=UpCellNode:"+currentGlobalSeriesPath);
            if(currentGlobalSeriesPath.getUpCellNode().getAddressCodeID() == currentAddressCodeID){ // 当前地址码在举升点
                if(currentGlobalSeriesPath.getUpPodID() == podCodeID){ // 扫描到的podID 正是所需 --继续锁格下发
                    runGlobalSeriesPathAndLockCellCheck_();
                    podCounts = 0;
                }else { // 扫描podID错误  或没有扫到
                    if(podCounts < 20){ // 最多扫描实时包20次， 如果podID不正确，则停下来
                        podCounts++;
                        return;
                    }
                    currentGlobalSeriesPath.setUpCellNode(null);
                    seriesPathLinkedList.clear();
                    */
/*for(CellNode node: currentGlobalSeriesPath.getPathList()){
                        node.setNoInGlobalPath(currentGlobalSeriesPath);
                    }*//*

                    for(CellNode node: getLastSendedSeriesPath().getPathList()){
                        if(node.getAddressCodeID() != currentAddressCodeID){
                            node.setUnLocked();
                        }
                    }
                    clearCurrentGlobalSeriesPath();
                    setAGVStatus(AGVConfig.AGV_STATUS_STANDBY);
                    isRunGlobalSeriesPath = false;
                    podCounts = 0;
                    return;
                }
            }else { // 当前地址码没在举升点， 继续锁格下发
                runGlobalSeriesPathAndLockCellCheck_();
            }
        }else{ // 没有举升点  正常锁格下发
            runGlobalSeriesPathAndLockCellCheck_();
        }
    }

    */
/*
       运行全局路径并且锁格检查
     *//*

//    @Override
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
        //=========================形成路径并且下发=============================
        createLockedSP();
        //=========================控制下发条件和频率===========================
        sendPathOncondition();
    }


    public void sendPathOncondition(){
        //=========================控制下发条件和频率===========================
        if (getCurrentSeriesPath() != null) {
            if (getCurrentSeriesPath().getCellListSize() > 0) {
                if (!isSamePreviousSeriesPath(currentSeriesPath)) {//和上一次的路径是否相同
                    LOG.info(" - - - - > > > > AGV("+getID()+")路径已下发：" + currentSeriesPath);
                    isSendedCurrentGlobalSeriesPath = true;
                    sendSeriesPath(getCurrentSeriesPath());
                    lastSendedSeriesPath = getCurrentSeriesPath();
                    setSendSPCountForTimeout(1);
                    getAgvCheckItem().setLastLockCellTime(System.currentTimeMillis());
                }
            }
        }
    }

    */
/*
     形成路径
     *//*

    public void createLockedSP() {
        //-- currentSeriesPath = new SeriesPath(currentGlobalSeriesPath.getUuid());
        LOG.info("AGV("+getID()+") 创建锁格开始 - - - - - ");
        LOG.info("AGV("+getID()+") currentGlobalSeriesPath="+currentGlobalSeriesPath);
        SeriesPath tmpSeriesPath = new SeriesPath(currentGlobalSeriesPath.getUuid());
        for (int i = 1; i <= AGVConfig.AGV_LOCKED_MAX_COUNTS + 1; i++) {
            if ((i - 1) < currentGlobalSeriesPath.getCellListSize()) {
                tmpCellNode = currentGlobalSeriesPath.getCellNodeByIndex(i - 1);
                if (tmpCellNode.checkAndLocked_MapLock((KivaAGV) this)) {
                    tmpSeriesPath.addPathCell(tmpCellNode);
                    LOG.info("#### AGV(" + getID() + ")格子可锁=" + tmpCellNode);
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
        LOG.info("AGV("+getID()+") 创建锁格结束 - - - - - ");

    }

    */
/*
     解锁已经跑过的CELL
     *//*

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
            LOG.info("####解锁数量=" + indexUnlocked + "(AGV_ID=" + getID() + ")");

//            showAGVDebugMessage(LOG, "####解锁数量=" + indexUnlocked + "(AGV_ID=" + getID() + ")");
            tmpList.clear();

            for (int i = 0; i < indexUnlocked; i++) {
                tmpCellNode = currentGlobalSeriesPath.getPathList().remove(0);
                List<RobotAction> actionList = tmpCellNode.getRobotActionList(getLastSendedSeriesPath());
                if (tmpCellNode.setUnLocked_MapLock(getLastSendedSeriesPath())) {
                    LOG.info("AGV("+getID()+") --  ##格子(" + tmpCellNode.getAddressCodeID() + ")解锁  移除格子动作（"+actionList+"）  并从路径（"+currentGlobalSeriesPath+"）中释放  ");
//                    showAGVDebugMessage(LOG, "##格子(" + tmpCellNode.getAddressCodeID() + ")解锁" + "(AGV_ID=" + getID() + ")");
                    tmpCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
                    tmpList.addLast(tmpCellNode);
                }
            }
            LOG.info("AGV("+getID()+") 解锁结束 - - - - - ");


            if (!tmpList.isEmpty()) {
                fireOnAGVUnLockedCell(tmpList);
            }
        }
    }


    */
/**
     *   // mingchun.mu@mushiny.com  解锁已经跑过的CELL(不管实时包在路径的任何位置出现  都解锁之前所有cell)
     *//*

    public void unlockCellFromSP_() {
        LOG.debug("---------AGV(" + getID() + ")解锁路径开始------");
        if (getLastSendedSeriesPath() != null) {
            int indexUnlocked = 0;
            showAGVDebugMessage(LOG, "####当前AGV位置=" + getCurrentAddressCodeID() + "(AGV_ID=" + getID() + ")");
            showAGVDebugMessage(LOG, "####当前路径剩余CELL数量=" + currentGlobalSeriesPath.getPathList() + "(AGV_ID=" + getID() + ")");
            for (CellNode cellNode : globalSeriesPath) {
                if (getCurrentAddressCodeID() == cellNode.getAddressCodeID()) {
                    break;
                }
                indexUnlocked++;
            }
            showAGVDebugMessage(LOG, "####解锁数量=" + indexUnlocked + "(AGV_ID=" + getID() + ")");
            tmpList.clear();


            for (int i = 0; i < indexUnlocked; i++) {
                tmpCellNode = globalSeriesPath.remove(0);
                if(currentGlobalSeriesPath.getPathList().contains(tmpCellNode)){
                    currentGlobalSeriesPath.removePathCell(tmpCellNode);
                    tmpCellNode.setNoInGlobalPath(currentGlobalSeriesPath);
                }
                if (tmpCellNode.setUnLocked_MapLock(getLastSendedSeriesPath())) {
                    showAGVDebugMessage(LOG, "##格子(" + tmpCellNode.getAddressCodeID() + ")解锁" + "(AGV_ID=" + getID() + ")");
                    tmpList.addLast(tmpCellNode);
                }
            }
            if (!tmpList.isEmpty()) {
                fireOnAGVUnLockedCell(tmpList);
            }

            LOG.debug("---------AGV(" + getID() + ")解锁路径结束------");
        }
    }

    */
/*
     判断指定的路径与上一次的路径是否"相同"
     *//*

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
        showAGVDebugMessage(LOG, "####下发路径信息:" + sp + "(AGV_ID=" + getID() + ")");
        pathMessage = new RCS2RobotPathMessage(getID());
        pathMessage.setSeriesPath(getCurrentSeriesPath());
        pathMessage.toMessage();
        lockedLastCellAddressCodeID = getCurrentSeriesPath().getPathList().getLast().getAddressCodeID();
        showAGVDebugMessage(LOG, "####下发路径指令:" + HexBinaryUtil.byteArrayToHexString2((byte[]) pathMessage.getMessage()) + "(AGV_ID=" + getID() + ")");
        sendMessageToAGV(pathMessage);
    }

    */
/*
     put WCS路径
     *//*

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

*/
/*
        if(seriesPathLinkedList != null && seriesPathLinkedList.size() > 0){
            if(!tmpList.getFirst().equals(seriesPathLinkedList.get(seriesPathLinkedList.size() - 1))){
                LOG.error("####AGV下发的路径与上一次的终点不一致！！");
                return;
            }
        }
*//*


        for (SeriesPath tmp : spList) {
            putGlobalSeriesPath(tmp);

           */
/* for(CellNode cellNode: tmp.getPathList()){
                globalSeriesPath.add(cellNode);
            }*//*

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

    */
/**
     * @return the stopSendSeriesPathFlag
     *//*

    public boolean isStopSendSeriesPathFlag() {
        return stopSendSeriesPathFlag;
    }

    */
/**
     * @param stopSendSeriesPathFlag the stopSendSeriesPathFlag to set
     *//*

    public void setStopSendSeriesPathFlag(boolean stopSendSeriesPathFlag) {
        this.stopSendSeriesPathFlag = stopSendSeriesPathFlag;
    }

    */
/**
     * @return the currentSeriesPath
     *//*

    public SeriesPath getCurrentSeriesPath() {
        return currentSeriesPath;
    }

    */
/**
     * @return the lastSendedSeriesPath
     *//*

    public SeriesPath getLastSendedSeriesPath() {
        return lastSendedSeriesPath;
    }

    public void setLastSendedSeriesPath(SeriesPath sp) {
        this.lastSendedSeriesPath = sp;
    }
    public abstract void setSendSPCountForTimeout(int sendSPCountForTimeout) ;
    

}
*/
