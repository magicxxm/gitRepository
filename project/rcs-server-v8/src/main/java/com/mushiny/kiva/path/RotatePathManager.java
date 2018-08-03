/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 具有旋转功能的工具的路径管理
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RotatePathManager extends PathManager {

    static Logger LOG = LoggerFactory.getLogger(RotatePathManager.class.getName());
    protected static RotatePathManager instance;

    private RotatePathManager() {
        super();

    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RotatePathManager();
        }
    }

    public static RotatePathManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static RotatePathManager getInstance() {
        if (instance == null) {
            instance = new RotatePathManager();
        }
        return instance;
    }
*/

    /*
     对路径串进行初始化，包括设置CELL的左右邻居，动作类型
     */
    public void initSeriesPathForRun(List<CellNode> cellNodeList, long podUpAddressCodeID, long podDownAddressCodeID, boolean isRotate, int rotateTheta, long enterAddressCodeID, long exitAddressCodeID) {
        setSeriesPathNeighbour(cellNodeList);
        setCellNodeAction(cellNodeList, podUpAddressCodeID, podDownAddressCodeID);
        setSeriesPathSplitFlag(cellNodeList);
    }

    /*
     对路径进行直行、AGV旋转等进行自动设定
     */
    public List<CellNode> setCellNodeAction(List<CellNode> cellNodeList, long podUpAddressCodeID, long podDownAddressCodeID) {
        CellNode currentCellNode;
        CellNode nextCellNode;
        //设置起点路径，起点的动作码必须是旋转
        currentCellNode = cellNodeList.get(0);
        nextCellNode = cellNodeList.get(1);
        if (currentCellNode.isUpNode(nextCellNode)) {
            if (podUpAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转顶升
                currentCellNode.setRobotAction(rotate12Action_0);
            }
            if (podDownAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转降落
                currentCellNode.setRobotAction(rotate13Action_0);
            }
            if (podDownAddressCodeID != currentCellNode.getAddressCodeID()
                    && podUpAddressCodeID != currentCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                currentCellNode.setRobotAction(rotate10Action_0);
            }
        }
        if (currentCellNode.isRightNode(nextCellNode)) {
            if (podUpAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转顶升
                currentCellNode.setRobotAction(rotate12Action_90);
            }
            if (podDownAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转降落
                currentCellNode.setRobotAction(rotate13Action_90);
            }
            if (podDownAddressCodeID != currentCellNode.getAddressCodeID()
                    && podUpAddressCodeID != currentCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                currentCellNode.setRobotAction(rotate10Action_90);
            }
        }
        if (currentCellNode.isDownNode(nextCellNode)) {
            if (podUpAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转顶升
                currentCellNode.setRobotAction(rotate12Action_180);
            }
            if (podDownAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转降落
                currentCellNode.setRobotAction(rotate13Action_180);
            }
            if (podDownAddressCodeID != currentCellNode.getAddressCodeID()
                    && podUpAddressCodeID != currentCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                currentCellNode.setRobotAction(rotate10Action_180);
            }
        }
        if (currentCellNode.isLeftNode(nextCellNode)) {
            if (podUpAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转顶升
                currentCellNode.setRobotAction(rotate12Action_270);
            }
            if (podDownAddressCodeID == currentCellNode.getAddressCodeID()) {//旋转降落
                currentCellNode.setRobotAction(rotate13Action_270);
            }
            if (podDownAddressCodeID != currentCellNode.getAddressCodeID()
                    && podUpAddressCodeID != currentCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                currentCellNode.setRobotAction(rotate10Action_270);
            }
        }
        //如果路径中只有2个CELL
        if (cellNodeList.size() == 2) {
            if (podUpAddressCodeID == nextCellNode.getAddressCodeID()) {//旋转顶升
                nextCellNode.setRobotAction(rotate12Action_270);
            }
            if (podDownAddressCodeID == nextCellNode.getAddressCodeID()) {//旋转降落
                nextCellNode.setRobotAction(rotate13Action_270);
            }
            if (podDownAddressCodeID != nextCellNode.getAddressCodeID()
                    && podUpAddressCodeID != nextCellNode.getAddressCodeID()) {//停止
                nextCellNode.setRobotAction(stopAction);
            }
            return cellNodeList;
        }
        //路径中大于等于3个CELL
        //处理路径中的中间数据
        CellNode previousCellNode;
        for (int i = 1; i < cellNodeList.size(); i++) {
            currentCellNode = cellNodeList.get(i);
            if ((i + 1) < cellNodeList.size()) {
                previousCellNode = cellNodeList.get(i - 1);
                nextCellNode = cellNodeList.get(i + 1);
                setCellNodeActionCode(previousCellNode, currentCellNode, nextCellNode, podUpAddressCodeID, podDownAddressCodeID);
            } else {
                nextCellNode = null;//说明currentCellNode是末CELL
                if(podUpAddressCodeID==currentCellNode.getAddressCodeID()) {
                    currentCellNode.setRobotAction(up20Action);
                }
                if(podDownAddressCodeID==currentCellNode.getAddressCodeID()) {
                    currentCellNode.setRobotAction(down21Action);
                }
                if(podUpAddressCodeID!=currentCellNode.getAddressCodeID()
                        &&podDownAddressCodeID!=currentCellNode.getAddressCodeID()) {
                currentCellNode.setRobotAction(stopAction);
                }
                break;
            }
        }
        return cellNodeList;
    }

    /*
     三个相邻的CELL,通过判断后，设置中间CELL的ActionCode
    @param firstCellNode
    @param secondeCellNode
    @param thirdCellNode
    
     */
    public void setCellNodeActionCode(CellNode firstCellNode, CellNode secondeCellNode, CellNode thirdCellNode, long podUpAddressCodeID, long podDownAddressCodeID) {
        //在前一个的右边
        if (firstCellNode.isRightNode(secondeCellNode)) {
            if (secondeCellNode.isRightNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//顶升
                    secondeCellNode.setRobotAction(up20Action);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//降落
                    secondeCellNode.setRobotAction(down21Action);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//直行
                    secondeCellNode.setRobotAction(lineAction);
                }
                //-- secondeCellNode.setRobotAction(lineAction);
            }
            if (secondeCellNode.isUpNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_0);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_0);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_0);
                }
                //--  secondeCellNode.setRobotAction(rotateAction_0);
            }
            if (secondeCellNode.isDownNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_180);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_180);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_180);
                }
                //-- secondeCellNode.setRobotAction(rotateAction_180);
            }
            return;
        }
        //在前一个的下面
        if (firstCellNode.isDownNode(secondeCellNode)) {
            if (secondeCellNode.isRightNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_90);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_90);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_90);
                }
                //-- secondeCellNode.setRobotAction(rotateAction_90);
            }
            if (secondeCellNode.isLeftNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_270);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_270);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_270);
                }
                //-- secondeCellNode.setRobotAction(rotateAction_270);
            }
            if (secondeCellNode.isDownNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//顶升
                    secondeCellNode.setRobotAction(up20Action);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//降落
                    secondeCellNode.setRobotAction(down21Action);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//直行
                    secondeCellNode.setRobotAction(lineAction);
                }
                //-- secondeCellNode.setRobotAction(lineAction);
            }
            return;
        }
        //在前一个的左边
        if (firstCellNode.isLeftNode(secondeCellNode)) {
            if (secondeCellNode.isUpNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_0);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_0);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_0);
                }
                //-- secondeCellNode.setRobotAction(rotateAction_0);
            }
            if (secondeCellNode.isLeftNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//顶升
                    secondeCellNode.setRobotAction(up20Action);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//降落
                    secondeCellNode.setRobotAction(down21Action);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//直行
                    secondeCellNode.setRobotAction(lineAction);
                }
                //-- secondeCellNode.setRobotAction(lineAction);
            }
            if (secondeCellNode.isDownNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_180);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_180);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_180);
                }
                //--secondeCellNode.setRobotAction(rotateAction_180);
            }
            return;
        }
        //在前一个的上面
        if (firstCellNode.isUpNode(secondeCellNode)) {
            if (secondeCellNode.isUpNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//顶升
                    secondeCellNode.setRobotAction(up20Action);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//降落
                    secondeCellNode.setRobotAction(down21Action);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//直行
                    secondeCellNode.setRobotAction(lineAction);
                }
                //-- secondeCellNode.setRobotAction(lineAction);
            }
            if (secondeCellNode.isLeftNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_270);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_270);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_270);
                }
                //--secondeCellNode.setRobotAction(rotateAction_270);
            }
            if (secondeCellNode.isRightNode(thirdCellNode)) {
                if (podUpAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转顶升
                    secondeCellNode.setRobotAction(rotate12Action_90);
                }
                if (podDownAddressCodeID == secondeCellNode.getAddressCodeID()) {//旋转降落
                    secondeCellNode.setRobotAction(rotate13Action_90);
                }
                if (podDownAddressCodeID != secondeCellNode.getAddressCodeID()
                        && podUpAddressCodeID != secondeCellNode.getAddressCodeID()) {//车辆普通旋转,托盘固定
                    secondeCellNode.setRobotAction(rotate10Action_90);
                }
                //--secondeCellNode.setRobotAction(rotateAction_90);
            }
            return;
        }
    }

    /**
     * 根据起点beginCellNode、终点endCellNode、旋转区rotateArea获取指定路径
     * @param list 待排序的点集
     * @param beginCellNode 开始点
     * @param endCellNode 结束点
     * @param rotateArea 旋转区
     * @return List<CellNode>
     */
    public List<CellNode> getSortBunchListByListBeginEndRotateArea(List<CellNode> list, CellNode beginCellNode, CellNode endCellNode, RotateArea rotateArea){
        if(list == null){
            return null;
        }
        if(list.size() == 0){
            return null;
        }
        if(list.size() == 1){
            return null;
        }
        if(beginCellNode == null){
            return null;
        }
        if(endCellNode == null){
            return null;
        }
        //起点终点不在指定点集里
        if(!list.contains(beginCellNode) || !list.contains(endCellNode)){
            return null;
        }
        //不经过旋转区
        if(rotateArea == null){
            return getSortCellNodeListByBeginEnd(list, beginCellNode, endCellNode);
        }
        //经过旋转区但不满足旋转区要求
        if(!isInOutCellNode(list, rotateArea)){
            return null;
        }
        List<CellNode> resList = null;
        if(isSeriesPathList(list)){//经过旋转区 进出口点相邻
            resList = getSortCellNodeListByBeginEnd(list, beginCellNode, endCellNode);
        }else{//经过旋转区 进出口点对角
            CellNode rotateCellNode = null;
            if (!list.contains(rotateArea.getLeftUpCellNode())){
                list.add(rotateArea.getLeftUpCellNode());
                rotateCellNode = rotateArea.getLeftUpCellNode();
            }else if(!list.contains(rotateArea.getRightUpCellNode())){
                list.add(rotateArea.getRightUpCellNode());
                rotateCellNode = rotateArea.getRightUpCellNode();
            }else if(!list.contains(rotateArea.getRightDownCellNode())){
                list.add(rotateArea.getRightDownCellNode());
                rotateCellNode = rotateArea.getRightDownCellNode();
            }else if(!list.contains(rotateArea.getLeftDownCellNode())){
                list.add(rotateArea.getLeftDownCellNode());
                rotateCellNode = rotateArea.getLeftDownCellNode();
            }
            if(!isSeriesPathList(list)){
                return null;
            }
            resList = getSortCellNodeListByBeginEnd(list, beginCellNode, endCellNode);
            resList.remove(rotateCellNode);
        }

        CellNode outCellNode = getOutCellNode(resList, rotateArea);
        if(outCellNode == rotateArea.getLeftUpCellNode()){//当出口点为左上角的点时
            if(outCellNode.getUpNode() == null && outCellNode.getLeftNode() == null){//没有出口时
                return null;
            }
        }else if(outCellNode == rotateArea.getRightUpCellNode()){//右上角
            if(outCellNode.getUpNode() == null && outCellNode.getRightNode() == null){//没有出口时
                return null;
            }
        }else if(outCellNode == rotateArea.getRightDownCellNode()){//右下角
            if(outCellNode.getDownNode() == null && outCellNode.getRightNode() == null){//没有出口时
                return null;
            }
        }else if(outCellNode == rotateArea.getLeftDownCellNode()){//左下角
            if(outCellNode.getDownNode() == null && outCellNode.getLeftNode() == null){//没有出口时
                return null;
            }
        }
        return resList;
    }

    /**
     * 判断点是否经过旋转区，而且有且有一个入口点和一个出口点
     * @param list
     * @param rotateArea
     * @return
     */
    private boolean isInOutCellNode(List<CellNode> list, RotateArea rotateArea){
        if(list == null){
            return false;
        }
        if(list.size() ==0 || list.size() == 1){
            return false;
        }
        int count = 0;
        if (list.contains(rotateArea.getLeftUpCellNode()) && rotateArea.getLeftUpCellNode() != null){
            count ++;
        }
        if(list.contains(rotateArea.getRightUpCellNode()) && rotateArea.getRightUpCellNode() != null){
            count ++;
        }
        if(list.contains(rotateArea.getRightDownCellNode()) && rotateArea.getRightDownCellNode() != null){
            count ++;
        }
        if(list.contains(rotateArea.getLeftDownCellNode()) && rotateArea.getLeftDownCellNode() != null){
            count ++;
        }
        if(count == 2){
            return true;
        }
        return false;
    }
    /**
     * 获取旋转区出口点
     * @param list 排序后的集合
     * @param rotateArea 旋转区
     * @return CellNode 旋转区出口点
     */
    private CellNode getOutCellNode(List<CellNode> list, RotateArea rotateArea){
        if(list == null){
            return null;
        }
        if(list.size() ==0 || list.size() == 1){
            return null;
        }
        int index = -1;
        if (list.contains(rotateArea.getLeftUpCellNode()) && rotateArea.getLeftUpCellNode() != null){
            index = list.indexOf(rotateArea.getLeftUpCellNode());
        }
        if(list.contains(rotateArea.getRightUpCellNode()) && rotateArea.getRightUpCellNode() != null){
            int tmp = list.indexOf(rotateArea.getRightUpCellNode());
            if(tmp > index){
                index = tmp;
            }
        }
        if(list.contains(rotateArea.getRightDownCellNode()) && rotateArea.getRightDownCellNode() != null){
            int tmp = list.indexOf(rotateArea.getRightDownCellNode());
            if(tmp > index){
                index = tmp;
            }
        }
        if(list.contains(rotateArea.getLeftDownCellNode()) && rotateArea.getLeftDownCellNode() != null){
            int tmp = list.indexOf(rotateArea.getLeftDownCellNode());
            if(tmp > index){
                index = tmp;
            }
        }
        return list.get(index);
    }
}
