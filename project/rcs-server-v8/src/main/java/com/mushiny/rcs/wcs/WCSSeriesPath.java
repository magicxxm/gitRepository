/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.wcs;

import com.mingchun.mu.manager.IOriginCodes;
import com.mingchun.mu.manager.KivaConfigToolModifyManager;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNodeManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateArea;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaManager;
import com.mingchun.mu.mushiny.kiva.path.TriangleRotateAreaNewManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.RotateAreaActionManager;
import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.SeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 *
 * @author 陈庆余 <13469592826@163.com>
 */
public class WCSSeriesPath {

    private static Logger LOG = LoggerFactory.getLogger(WCSSeriesPath.class.getName());
    private String wcsSPID="";
    private long agvID;

    private long upPodAddressCodeID;//pod升降地址码，UP时，此值必须提供，否则系统忽略此值
    private long downPodAddressCodeID;//pod降地址码，DOWN时，此值必须提供，否则系统忽略此值

    private int podCodeID;//货架码ID

    private boolean isRotate = false;//是否旋转
    private int rotateTheta = 0;//POD角度旋转量,UP POD=1时有效，此值必须提供，否则系统忽略此值
    private long enterAddressCodeID = 0;//如果经过旋转区，旋转区入口
    private long exitAddressCodeID = 0;//如果经过旋转区，旋转区出口
    protected LinkedList<Long> addressCodeIDList = new LinkedList();
    private LinkedList<CellNode> cellNodeList = new LinkedList();

    private final RotateAreaActionManager rotateAreaActionManager;
    private final RotateAreaManager rotateAreaManager;

    private CellNode enterCellNode;
    private CellNode exitCellNode;
    private CellNode previousEnterCellNode;
    private CellNode nextExitCellNode;
    private boolean isRotateArea = false;
    private LinkedList<SeriesPath> rotateAreaSeriesPathList = new LinkedList();
    private LinkedList<SeriesPath> rsSeriesPathList = new LinkedList();

    private KivaConfigToolModifyManager kivaConfigToolModifyManager = KivaConfigToolModifyManager.getInstance();

    public WCSSeriesPath(long upPodAddressCodeID, long downPodAddressCodeID, boolean isRotate, int rotateTheta, LinkedList<Long> addressCodeIDList) {
        this.upPodAddressCodeID = upPodAddressCodeID;
        this.downPodAddressCodeID = downPodAddressCodeID;
        this.isRotate = isRotate;
        this.rotateTheta = rotateTheta;
        this.addressCodeIDList = addressCodeIDList;
        rotateAreaActionManager = RotateAreaActionManager.getInstance();
        rotateAreaManager = RotateAreaManager.getInstance();
    }

    /*
     得到返回路径
     */
    public WCSSeriesPath getReturnSeriesPaths() {
        LinkedList<Long> returnLinkedList = new LinkedList();
        for (long addressCodeID : getAddressCodeIDList()) {
            returnLinkedList.addFirst(addressCodeID);
        }
        WCSSeriesPath returnWCSSeriesPath = new WCSSeriesPath(getDownPodAddressCodeID(), getUpPodAddressCodeID(), isIsRotate(), getRotateTheta(), returnLinkedList);
        if (!returnWCSSeriesPath.checkWCSSeriesPath()) {
            return null;
        }
        return returnWCSSeriesPath;
    }

    /*
     判断路径串的地址码是否合法
    WCS下发的路径都要进行合法性校验,这些合法性校验是必须的.
    因为这些路径有可能存在潜在的安全问题.
     */
    public boolean checkWCSSeriesPath_() {
        LOG.info("WCSSeriesPath路径合法性检测....");
        getCellNodeList().clear();
        if (getAddressCodeIDList() == null) {
            LOG.error("####错误==>>WCS下发的路径不合法(0)，路径为空！");
            return false;
        }
        if (getAddressCodeIDList().size() < 2) {
            LOG.error("####错误==>>WCS下发的路径不合法(0)，路径至少经过两个CELL,不合法！");
            return false;
        }

        //1.地址码列表是否位空
        if (getAddressCodeIDList().isEmpty()) {
            LOG.error("####错误==>>WCS下发的路径不合法(1)，地址码列表位空！");
            return false;
        }
        //2.地址码是否都是可WALKED
        CellNode tmpCellNode;
        int maxAddressCodeID = MapManager.getInstance().getMap().getMaxAddressCodeID();
        for (long addressCondeID : getAddressCodeIDList()) {
            if (addressCondeID <= 0 || addressCondeID > maxAddressCodeID) {
                LOG.error("####错误==>>WCS下发的路径不合法(2)，地址码不在地图中！");
                return false;
            }
            tmpCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCondeID);
            if (tmpCellNode == null) {
                LOG.error("####错误==>>WCS下发的路径不合法(3)，地址码为空(" + addressCondeID + ")！");
                return false;
            } else {
                getCellNodeList().addLast(tmpCellNode);
            }
            if (!tmpCellNode.isWalkable()) {
                LOG.error("####错误==>>WCS下发的路径不合法(4)，地址码(" + addressCondeID + ")是不可通过的！");
                return false;
            }
        }
        //举升地址码 合法性校验
        if (getUpPodAddressCodeID
                () > 0) {
            //举升地址码是否存在且一次
            int flag = 0;
            for (long v : getAddressCodeIDList()) {
                if (v == getUpPodAddressCodeID()) {
                    flag++;
                }
            }
            if (flag != 1) {
                LOG.error("####错误==>>WCS下发的路径不合法(5)，举升地址码(" + getUpPodAddressCodeID() + ")在WCS路径中不是存在1次！");
//                return false;
            }

            tmpCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(getUpPodAddressCodeID());
            if (tmpCellNode == null) {
                LOG.error("####错误==>>WCS下发的路径不合法(6)，举升地址码(" + getUpPodAddressCodeID() + ")不在地图中！");
                return false;
            }
            if (!tmpCellNode.isWalkable()) {
                LOG.error("####错误==>>WCS下发的路径不合法(7)，举升地址码(" + getUpPodAddressCodeID() + ")是不可通过的！");
                return false;
            }
            if (null != RotateAreaManager.getInstance().getRotateAreaByCellNode(getUpPodAddressCodeID())) {
                LOG.error("####错误==>>WCS下发的路径不合法(8)，举升地址码(" + getUpPodAddressCodeID() + ")在旋转区！");
                return false;
            }
        }
        //降落地址码 合法性校验
        if (getDownPodAddressCodeID() > 0) {
            int flag = 0;
            for (long v : getAddressCodeIDList()) {
                if (v == getDownPodAddressCodeID()) {
                    flag++;
                }
            }
            if (flag != 1) {
                LOG.error("####错误==>>WCS下发的路径不合法(9)，降落地址码(" + getDownPodAddressCodeID() + ")在WCS路径中不是存在1次！");
//                return false;
            }

            tmpCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(getDownPodAddressCodeID());
            if (tmpCellNode == null) {
                LOG.error("####错误==>>WCS下发的路径不合法(10)，降落地址码(" + getDownPodAddressCodeID() + ")不在地图中！");
                return false;
            }
            if (!tmpCellNode.isWalkable()) {
                LOG.error("####错误==>>WCS下发的路径不合法(11)，降落地址码(" + getDownPodAddressCodeID() + ")是不可通过的！");
                return false;
            }
            if (null != RotateAreaManager.getInstance().getRotateAreaByCellNode(getDownPodAddressCodeID())) {
                LOG.error("####错误==>>WCS下发的路径不合法(12)，降落地址码(" + getDownPodAddressCodeID() + ")在旋转区！");
                return false;
            }
        }
        int flag = 0;
        for (long v : getAddressCodeIDList()) {
            if (RotateAreaManager.getInstance().getRotateAreaByCellNode(v) != null) {
                flag = 1;
                break;
            }
            if (TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v) != null) {
                flag = 2;
                break;
            }
            if (IndividualCellNodeManager.getInstance().getIndividualCellNodeByIndividualCellNode(v) != null) {
                flag = 3;
                break;
            }
            if (TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByCellNode(v) != null) {
                flag = 4;
                break;
            }
        }
        if(flag == 1){
            if(!checkRotateArea()){
                return false;
            }
        }else if(flag == 2){
            if(!checkTriRotateArea()){
                return false;
            }
        }else if(flag == 4){}




        return true;
    }

    /**
     *
     * 通过三角旋转区校验
     *
     * @return
     */
    private boolean checkTriRotateArea(){
        //4.是否经过旋转区
        boolean flag = false;
        for (long v : getAddressCodeIDList()) {
            if (TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v) != null) {
                flag = true;
                break;
            }
        }
        if (isIsRotate()) {
            if(!flag){ // 路径不过旋转区
                LOG.error("####错误==>>WCS下发的路径不合法(30)，要求过旋转区， 但路径中没有过旋转区的点！");
                return false;
            }
        }

        // 如果路径过旋转区，但是是否经过旋转区为false，则路径不合法
        if(!isIsRotate()){
            if(flag){
                LOG.error("####错误==>>WCS下发的路径不合法(31)，没有要求过旋转区， 但路径中有过旋转区的点！");
                return false;
            }
        }

        //6.如果POD需要旋转，则旋转角度必须合法
        if (isIsRotateArea()) {
            if (getRotateTheta() != 0
                    && getRotateTheta() != 90
                    && getRotateTheta() != 180
                    && getRotateTheta() != 270) {
                LOG.error("####错误==>>WCS下发的路径不合法(32)，POD旋转的角度(" + getRotateTheta() + ")不合法!");
                return false;
            }
        }

        // 路径过旋转区的顺序： 进口点 --> 旋转点 --> 出口点
        TriangleRotateArea triangleRotateArea = null;
        long[] sequence = new long[3];
        for (long v : getAddressCodeIDList()) {
            if (TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v) != null) {
                if(triangleRotateArea == null){
                    triangleRotateArea = TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v);
                }
                if(sequence[0] == 0){
                    sequence[0] = v;
                }else if(sequence[1] == 0){
                    sequence[1] = v;
                }else if(sequence[2] == 0){
                    sequence[2] = v;
                }
            }
        }
        if(triangleRotateArea != null){
            if(triangleRotateArea.getEnterCellNode().getAddressCodeID() != sequence[0]
                    || triangleRotateArea.getRotationCellNode().getAddressCodeID() != sequence[1]
                    || triangleRotateArea.getExitCellNode().getAddressCodeID() != sequence[2]){
                LOG.error("####错误==>>WCS下发的路径不合法(33）， 路径进入旋转区时， 没有按顺序进入!");
                return false;
            }
        }

        return true;
    }
    /**
     *
     * 通过新三角旋转区校验
     *
     * @return
     */
    private boolean checkTriRotateAreaNew(){
        //4.是否经过旋转区
        boolean flag = false;
        for (long v : getAddressCodeIDList()) {
            if (TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v) != null) {
                flag = true;
                break;
            }
        }
        if (isIsRotate()) {
            if(!flag){ // 路径不过旋转区
                LOG.error("####错误==>>WCS下发的路径不合法(40)，要求过旋转区， 但路径中没有过旋转区的点！");
                return false;
            }
        }

        // 如果路径过旋转区，但是是否经过旋转区为false，则路径不合法
        if(!isIsRotate()){
            if(flag){
                LOG.error("####错误==>>WCS下发的路径不合法(41)，没有要求过旋转区， 但路径中有过旋转区的点！");
                return false;
            }
        }

        //6.如果POD需要旋转，则旋转角度必须合法
        if (isIsRotateArea()) {
            if (getRotateTheta() != 0
                    && getRotateTheta() != 90
                    && getRotateTheta() != 180
                    && getRotateTheta() != 270) {
                LOG.error("####错误==>>WCS下发的路径不合法(42)，POD旋转的角度(" + getRotateTheta() + ")不合法!");
                return false;
            }
        }

        // 路径过旋转区的顺序： 进口点 --> 旋转点
        TriangleRotateArea triangleRotateArea = null;
        long[] sequence = new long[3];
        for (long v : getAddressCodeIDList()) {
            if (TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v) != null) {
                if(triangleRotateArea == null){
                    triangleRotateArea = TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(v);
                }
                if(sequence[0] == 0){
                    sequence[0] = v;
                }else if(sequence[1] == 0){
                    sequence[1] = v;
                }else if(sequence[2] == 0){
                    sequence[2] = v;
                }
            }
        }
        if(triangleRotateArea != null){
            if(triangleRotateArea.getEnterCellNode().getAddressCodeID() != sequence[0]
                    || triangleRotateArea.getRotationCellNode().getAddressCodeID() != sequence[1]
                    || triangleRotateArea.getExitCellNode().getAddressCodeID() != sequence[2]){
                LOG.error("####错误==>>WCS下发的路径不合法(33）， 路径进入旋转区时， 没有按顺序进入!");
                return false;
            }
        }

        return true;
    }

    /**
     *
     * 通过旋转区校验
     *
     * @return
     */
    private boolean checkRotateArea(){
        //3.是否经过旋转区
        if (isIsRotate()) {
            setIsRotateArea(true);
        } else {
            setIsRotateArea(false);
        }

        // 如果路径过旋转区，但是是否经过旋转区为false，则路径不合法
        if(!isIsRotate()){
            for (long v : getAddressCodeIDList()) {
                if (RotateAreaManager.getInstance().getRotateAreaByCellNode(v) != null) {
                    return false;
                }
            }
        }

        //6.如果POD需要旋转，则旋转角度必须合法
        if (isIsRotateArea()) {
            if (getRotateTheta() != 0
                    && getRotateTheta() != 90
                    && getRotateTheta() != 180
                    && getRotateTheta() != 270) {
                LOG.error("####错误==>>WCS下发的路径不合法(14)，POD旋转的角度(" + getRotateTheta() + ")不合法!");
                return false;
            }
        }
        //6.路径经过旋转区,合法性校验
        if (isIsRotateArea()) {
            int enterExitCellNodeCount = 0;
            for (long v : getAddressCodeIDList()) {
                if (RotateAreaManager.getInstance().getRotateAreaByCellNode(v) != null) {
                    if (enterExitCellNodeCount == 0) {
                        setEnterAddressCodeID(v);
                    }
                    if (enterExitCellNodeCount == 1) {
                        setExitAddressCodeID(v);
                    }
                    enterExitCellNodeCount++;
                }
            }
            if (enterExitCellNodeCount == 0) {
                LOG.error("####错误==>>WCS下发的路径不合法(15)，POD旋转角度(" + getRotateTheta() + "),路径中不存在旋转区!");
                return false;
            }
            if (enterExitCellNodeCount == 1) {
                LOG.error("####错误==>>WCS下发的路径不合法(16)，POD旋转角度(" + getRotateTheta() + "),路径中不存在旋转区的入口或出口!");
                return false;
            }
            if (enterExitCellNodeCount > 2) {
                LOG.error("####错误==>>WCS下发的路径不合法(17)，POD旋转角度(" + getRotateTheta() + "),路径中存在跨旋转区!");
                return false;
            }

            if (getEnterAddressCodeID() == getExitAddressCodeID()) {
                LOG.error("####错误==>>WCS下发的路径不合法(18)，POD旋转角度(" + getRotateTheta() + "),路径中的旋转区入口和出口相同!");
                return false;
            }
            if (!rotateAreaManager.isInSameRotateArea(enterAddressCodeID, exitAddressCodeID)) {
                LOG.error("####错误==>>WCS下发的路径不合法(19)，POD旋转角度(" + getRotateTheta() + "),路径中的旋转区入口和出口不在同一旋转区域!");
                return false;
            }
        }

        //8.经过旋转区
        if (isIsRotateArea()) {
            //经过旋转区的路径至少有四个点
            if (getCellNodeList().size() < 4) {
                LOG.error("####错误==>>WCS下发的路径不合法(20)，需要旋转,需要旋转的路径至少需要四个CELL！");
                return false;
            }
            //经过旋转区的路径有四个点
            if (getCellNodeList().size() == 4) {
                if (getCellNodeList().get(1).getAddressCodeID() != getEnterAddressCodeID()
                        || getCellNodeList().get(2).getAddressCodeID() != getExitAddressCodeID()) {
                    LOG.error("####错误==>>WCS下发的路径不合法(21)，需要旋转,需要旋转的路径中只有四个CELL,第2个必须是入口，第3个必须是出口！");
                    return false;
                }
            }

            for (int i = 0; i < getCellNodeList().size(); i++) {
                if (getEnterAddressCodeID() == getCellNodeList().get(i).getAddressCodeID()) {
                    setEnterCellNode(getCellNodeList().get(i));
                    LOG.info("设置旋转区入口点："+getCellNodeList().get(i).getAddressCodeID());

                    if ((i - 1) >= 0) {
                        setPreviousEnterCellNode(getCellNodeList().get(i - 1));
                    }
                }
                if (getExitAddressCodeID() == getCellNodeList().get(i).getAddressCodeID()) {

                    LOG.info("设置旋转区出口点："+getCellNodeList().get(i).getAddressCodeID());

                    setExitCellNode(getCellNodeList().get(i));
                    if ((i + 1) < getCellNodeList().size()) {
                        setNextExitCellNode(getCellNodeList().get(i + 1));
                    }
                }
            }
            if (getPreviousEnterCellNode() == null
                    || getEnterCellNode() == null
                    || getExitCellNode() == null
                    || getNextExitCellNode() == null) {
                LOG.error("####错误==>>WCS下发的路径不合法(22)，需要旋转,旋转区合法性校验错误，入口前一个CELL，入口，出口，出口后一个CELL，不存在！");
                return false;
            }

            rotateAreaSeriesPathList = rotateAreaActionManager.getRotateAreaEnterSeriesPath(getPreviousEnterCellNode(), enterCellNode, exitCellNode, getNextExitCellNode(), getRotateTheta());
            if (rotateAreaSeriesPathList == null) {
                LOG.error("####错误==>>WCS下发路径不合法(25)[旋转区路径出错!]");
                return false;
            }
            if (rotateAreaSeriesPathList.size() != 4) {
                LOG.error("####错误==>>WCS下发路径不合法(26)[旋转区路径出错!]");
                return false;
            }
        }
        return true;
    }




    public void analyzePath() {
        kivaConfigToolModifyManager.modifyWCSSeriesPath(new IOriginCodes() {
            @Override
            public void usingOriginCodes() {
                WCSeriesPathTool wcsTool = new WCSeriesPathTool(WCSSeriesPath.this);
                wcsTool.analysis();
            }
        }, this);
    }

    public boolean checkWCSSeriesPath() {
        if (checkWCSSeriesPath_()) {
            analyzePath();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (getRSSeriesPathList() == null) {
            return "WCS路徑串不合法!";
        }
        StringBuilder builder = new StringBuilder();
        for (SeriesPath sp : getRSSeriesPathList()) {
            builder.append(sp.toString());
            builder.append("\r\n");
        }
        return builder.toString();
    }

    /**
     * @return the agvID
     */
    public long getAgvID() {
        return agvID;
    }

    /**
     * @param agvID the agvID to set
     */
    public void setAgvID(long agvID) {
        this.agvID = agvID;
    }

    /**
     * @return the podAddressCodeID
     */
    public long getUpPodAddressCodeID() {
        return upPodAddressCodeID;
    }

    /**
     * @param upPodAddressCodeID the podAddressCodeID to set
     */
    public void setUpPodAddressCodeID(long upPodAddressCodeID) {
        this.upPodAddressCodeID = upPodAddressCodeID;
    }

    /**
     * @return the addresCodeIDList
     */
    public LinkedList<Long> getAddressCodeIDList() {
        return addressCodeIDList;
    }

    /**
     * @param addressCodeIDList the addresCodeIDList to set
     */
    public void setAddressCodeIDList(LinkedList<Long> addressCodeIDList) {
        this.addressCodeIDList = addressCodeIDList;
    }

    /**
     * @return the cellNodeList
     */
    public LinkedList<CellNode> getCellNodeList() {
        return cellNodeList;
    }

    /**
     * @return the downPodAddressCodeID
     */
    public long getDownPodAddressCodeID() {
        return downPodAddressCodeID;
    }

    /**
     * @return the isRotate
     */
    public boolean isIsRotate() {
        return isRotate;
    }

    /**
     * @param isRotate the isRotate to set
     */
    public void setIsRotate(boolean isRotate) {
        this.isRotate = isRotate;
    }

    /**
     * @return the rotateTheta
     */
    public int getRotateTheta() {
        return rotateTheta;
    }

    /**
     * @param rotateTheta the rotateTheta to set
     */
    public void setRotateTheta(int rotateTheta) {
        this.rotateTheta = rotateTheta;
    }

    /**
     * @return the enterAddressCodeID
     */
    public long getEnterAddressCodeID() {
        return enterAddressCodeID;
    }

    /**
     * @param enterAddressCodeID the enterAddressCodeID to set
     */
    public void setEnterAddressCodeID(long enterAddressCodeID) {
        this.enterAddressCodeID = enterAddressCodeID;
    }

    /**
     * @return the exitAddressCodeID
     */
    public long getExitAddressCodeID() {
        return exitAddressCodeID;
    }

    /**
     * @param exitAddressCodeID the exitAddressCodeID to set
     */
    public void setExitAddressCodeID(long exitAddressCodeID) {
        this.exitAddressCodeID = exitAddressCodeID;
    }

    /**
     * @param cellNodeList the cellNodeList to set
     */
    public void setCellNodeList(LinkedList<CellNode> cellNodeList) {
        this.cellNodeList = cellNodeList;
    }

    /**
     * @return the rotateAreaCellNodeList
     */
    public LinkedList<SeriesPath> getRotateAreaSeriesPathList() {
        return rotateAreaSeriesPathList;
    }

    /**
     * @param rotateSeriesPathList the rotateAreaCellNodeList to set
     */
    public void setRotateAreaSeriesPathList(LinkedList<SeriesPath> rotateSeriesPathList) {
        this.rotateAreaSeriesPathList = rotateSeriesPathList;
    }

    /**
     * @return the enterCellNode
     */
    public CellNode getEnterCellNode() {
        return enterCellNode;
    }

    /**
     * @param enterCellNode the enterCellNode to set
     */
    public void setEnterCellNode(CellNode enterCellNode) {
        this.enterCellNode = enterCellNode;
        this.enterCellNode.setRotateAreaEnter();
    }

    /**
     * @return the exitCellNode
     */
    public CellNode getExitCellNode() {
        return exitCellNode;
    }

    /**
     * @param exitCellNode the exitCellNode to set
     */
    public void setExitCellNode(CellNode exitCellNode) {
        this.exitCellNode = exitCellNode;
        this.exitCellNode.setRotateAreaExit();
    }

    /**
     * @return the isRotateArea
     */
    public boolean isIsRotateArea() {
        return isRotateArea;
    }

    /**
     * @param isRotateArea the isRotateArea to set
     */
    public void setIsRotateArea(boolean isRotateArea) {
        this.isRotateArea = isRotateArea;
    }

    /**
     * @return the rsSeriesPathList
     */
    public LinkedList<SeriesPath> getRSSeriesPathList() {
        return rsSeriesPathList;
    }

    /**
     * @param rsSeriesPathList the rsSeriesPathList to set
     */
    public void setRSSeriesPathList(LinkedList<SeriesPath> rsSeriesPathList) {
        this.rsSeriesPathList = rsSeriesPathList;
    }

    /**
     * @return the previousEnterCellNode
     */
    public CellNode getPreviousEnterCellNode() {
        return previousEnterCellNode;
    }

    /**
     * @param previousEnterCellNode the previousEnterCellNode to set
     */
    public void setPreviousEnterCellNode(CellNode previousEnterCellNode) {
        this.previousEnterCellNode = previousEnterCellNode;
    }

    /**
     * @return the nextExitCellNode
     */
    public CellNode getNextExitCellNode() {
        return nextExitCellNode;
    }

    /**
     * @param nextExitCellNode the nextExitCellNode to set
     */
    public void setNextExitCellNode(CellNode nextExitCellNode) {
        this.nextExitCellNode = nextExitCellNode;
    }

    /**
     * @return the wcsSPID
     */
    public String getWcsSPID() {
        return wcsSPID;
    }

    /**
     * @param wcsSPID the wcsSPID to set
     */
    public void setWcsSPID(String wcsSPID) {
        this.wcsSPID = wcsSPID;
    }

    public int getPodCodeID() {
        return podCodeID;
    }

    public void setPodCodeID(int podCodeID) {
        this.podCodeID = podCodeID;
    }
}
