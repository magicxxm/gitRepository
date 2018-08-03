/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.wcs;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.RotateAreaActionManager;
import com.mushiny.kiva.path.RotatePathManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 *WCS下发路径转换成AGV可执行的路径串
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class WCSeriesPathTool_bak {
    private static Logger LOG = LoggerFactory.getLogger(WCSeriesPathTool_bak.class.getName());
    private final RotateAreaActionManager rotateAreaActionManager;
    private final WCSSeriesPath wcsp;
    private final LinkedList<CellNode> firstCellNodeList = new LinkedList();
    private final LinkedList<CellNode> secondCellNodeList = new LinkedList();
    private LinkedList<SeriesPath> firstSeriesPathList = new LinkedList();
    private LinkedList<SeriesPath> secondSeriesPathList = new LinkedList();

    public WCSeriesPathTool_bak(WCSSeriesPath wcsp) {
        this.wcsp = wcsp;
        rotateAreaActionManager = RotateAreaActionManager.getInstance();
    }

    public void analysis() {
        if (wcsp.isIsRotateArea()) {
            rotateAreaAnalysis();
        } else {
            noRotateAreaAnalysis();
        }
    }

    //1.不经过旋转区的处理方法
    public void noRotateAreaAnalysis() {
        LinkedList<SeriesPath> rsList = commonCellNodeListAnalysis(wcsp.getCellNodeList());
        wcsp.setRSSeriesPathList(rsList);
    }
    //2.经过旋转区的处理方法
    public void rotateAreaAnalysis() {
        CellNode enterCellNode = wcsp.getEnterCellNode();
        CellNode exitCellNode = wcsp.getExitCellNode();
        int enterNum = wcsp.getCellNodeList().indexOf(enterCellNode);
        int exitNum = wcsp.getCellNodeList().indexOf(exitCellNode);
        firstCellNodeList.clear();
        secondCellNodeList.clear();
        for (int i = 0; i <= enterNum; i++) {
            firstCellNodeList.addLast(wcsp.getCellNodeList().get(i));
        }
        for (int i = exitNum; i < wcsp.getCellNodeList().size(); i++) {
            secondCellNodeList.addLast(wcsp.getCellNodeList().get(i));
        }
        firstSeriesPathList = commonCellNodeListAnalysis(firstCellNodeList);
//        firstSeriesPathList.getLast().getPathList().getLast().setRobotAction(firstSeriesPathList.getLast(), null);
        secondSeriesPathList = commonCellNodeListAnalysis(secondCellNodeList);
//        secondSeriesPathList.getFirst().getPathList().getFirst().setRobotAction(secondSeriesPathList.getFirst(), null);

        //------旋转区路径串
        LinkedList<SeriesPath> rotateAreaSeriesPathList;
        rotateAreaSeriesPathList = rotateAreaActionManager.getRotateAreaEnterSeriesPath(wcsp.getPreviousEnterCellNode(), wcsp.getEnterCellNode(), wcsp.getExitCellNode(), wcsp.getNextExitCellNode(), wcsp.getRotateTheta());
        SeriesPath enterSeriesPath_A = rotateAreaSeriesPathList.get(0);
        SeriesPath enterSeriesPath_B = rotateAreaSeriesPathList.get(1);
        SeriesPath exitSeriesPath_A = rotateAreaSeriesPathList.get(2);
        SeriesPath exitSeriesPath_B = rotateAreaSeriesPathList.get(3);
        LinkedList<SeriesPath> rsLinkedList = new LinkedList();
        //修订第一段路径串中,最后一个SeriesPath
        SeriesPath firstLastSeriesPath = firstSeriesPathList.getLast();
        firstLastSeriesPath.getPathList().remove(firstLastSeriesPath.getCellListSize() - 1);
        for (CellNode cn : enterSeriesPath_A.getPathList()) {
            if(cn.getRobotActionList(firstLastSeriesPath) != null){
                cn.getRobotActionList(firstLastSeriesPath).clear();
            }
            cn.setRobotAction(firstLastSeriesPath, cn.getRobotAction());
            firstLastSeriesPath.addPathCell(cn);
        }
        for(CellNode node: enterSeriesPath_B.getPathList()){
            node.setRobotAction(enterSeriesPath_B, node.getRobotAction());
        }
        firstSeriesPathList.addLast(enterSeriesPath_B);
        for(CellNode node: exitSeriesPath_A.getPathList()){
            node.setRobotAction(exitSeriesPath_A, node.getRobotAction());
        }
        secondSeriesPathList.addFirst(exitSeriesPath_A);
        //修订第二段路径串中,第一个SeriesPath
        SeriesPath secondFirstSeriesPath = secondSeriesPathList.get(1);
        secondFirstSeriesPath.getPathList().remove(0);
        for (int i = 0; i < exitSeriesPath_B.getCellListSize(); i++) {

            CellNode tmpNode = exitSeriesPath_B.getCellNodeByIndex(i);

            if(tmpNode.getRobotActionList(secondFirstSeriesPath) != null){
                tmpNode.getRobotActionList(secondFirstSeriesPath).clear();
            }

            tmpNode.setRobotAction(secondFirstSeriesPath, tmpNode.getRobotAction());

            secondFirstSeriesPath.getPathList().add(i, tmpNode);
        }
        //分析结果赋予WCSP
        rsLinkedList.addAll(firstSeriesPathList);
        rsLinkedList.addAll(secondSeriesPathList);
        wcsp.setRotateAreaSeriesPathList(rotateAreaSeriesPathList);

        StringBuilder builder1 = new StringBuilder();
        builder1.append(wcsp.getAgvID()+"号车 经过旋转区分割出来的路径串:\n");
        for(SeriesPath path : rsLinkedList){
            builder1.append(path);
            builder1.append("\r\n");
        }
        LOG.debug(builder1.toString());

        wcsp.setRSSeriesPathList(rsLinkedList);


    }

    /**
     * 通过node获取到所在的路径串
     * @param seriesPaths
     * @param node
     * @param i
     * @return
     */
    private SeriesPath getNodePath(LinkedList<SeriesPath> seriesPaths, CellNode node, int i){
        int count = 0;
        for(SeriesPath seriesPath: seriesPaths){
            if(seriesPath.getPathList().contains(node)){
                if ((seriesPath.getPathList().indexOf(node) + count) == i){
                    return seriesPath;
                }
            }
            count += seriesPath.getPathList().size();
        }
        return null;
    }

    /**
     * 截取neighbourCells从start（包含）开始到end（排斥）
     * @param neighbourCells
     * @param start
     * @param end
     * @return
     */
    private LinkedList<CellNode> getSplitLinkedList(LinkedList<CellNode> neighbourCells, int start, int end){
        LinkedList<CellNode> linkedList = new LinkedList<CellNode>();
        for(int i = start; i < end; i++){
            linkedList.add(neighbourCells.get(i));
        }
        return linkedList;
    }
    /**
     * 将具有邻里关系点划分成直线串
     * @param neighbourCells 邻里关系点
     * @return 直线串集合
     */
    private LinkedList<SeriesPath> wcsCells2SeriesPath(LinkedList<CellNode> neighbourCells, CellNode upNode, CellNode downNode){
        // 截取直线路径串开始------------------
        LinkedList<SeriesPath> seriesPaths = new LinkedList<SeriesPath>();
        int index = 0;
        for(int i = 1, len = neighbourCells.size(); i < len-1; i++){
            CellNode curNode = neighbourCells.get(i);
            CellNode preNode = neighbourCells.get(i-1);
            CellNode nextNode = neighbourCells.get(i+1);
            if(curNode.equals(upNode) //上升点分割
                    || curNode.equals(downNode) //下降点分割
                    ){
                SeriesPath seriesPath = new SeriesPath();
                seriesPath.setCellPathList(getSplitLinkedList(neighbourCells, index, (i+1)));
                index = i+1;
                seriesPaths.add(seriesPath);
            }else {//所有转弯处分割
                if(preNode.isUpNode(curNode) || preNode.isDownNode(curNode)){
                    if(curNode.isRightNode(nextNode) || curNode.isLeftNode(nextNode) || preNode == nextNode){
                        SeriesPath seriesPath = new SeriesPath();
                        seriesPath.setCellPathList(getSplitLinkedList(neighbourCells, index, (i+1)));
                        index = i+1;
                        seriesPaths.add(seriesPath);
                    }
                }
                if(preNode.isRightNode(curNode) || preNode.isLeftNode(curNode)){
                    if(curNode.isUpNode(nextNode) || curNode.isDownNode(nextNode) || preNode == nextNode){
                        SeriesPath seriesPath = new SeriesPath();
                        seriesPath.setCellPathList(getSplitLinkedList(neighbourCells, index, (i+1)));
                        index = i+1;
                        seriesPaths.add(seriesPath);
                    }
                }
            }
        }
        SeriesPath lastSeriesPath = new SeriesPath();
        lastSeriesPath.setCellPathList(getSplitLinkedList(neighbourCells, index, neighbourCells.size()));
        seriesPaths.add(lastSeriesPath);
        // 截取直线路径串结束------------------
        LOG.debug(wcsp.getAgvID()+"号车 无动作分割出来的路径串:");
        StringBuilder builder1 = new StringBuilder();
        for(SeriesPath path : seriesPaths){
            for(CellNode node: path.getPathList()){
                builder1.append(node.getAddressCodeID()+",");
            }
            builder1.append("\r\n");
        }
        LOG.debug(builder1.toString());


        // 给点添加动作开始------------------------------
        for(int i = 0, len = neighbourCells.size(); i < len; i++){
            CellNode curNode = neighbourCells.get(i);
            SeriesPath nodeInSeriesPath = getNodePath(seriesPaths, curNode, i);
            if(i == 0){
                CellNode nextNode = neighbourCells.get(1);
                RobotAction rotateAction = null;
                int rotateAngel = 0;
                if(curNode.isUpNode(nextNode)){
                    rotateAngel = 0;
                }else if(curNode.isRightNode(nextNode)){
                    rotateAngel = 90;
                }else if(curNode.isDownNode(nextNode)){
                    rotateAngel = 180;
                }else if(curNode.isLeftNode(nextNode)){
                    rotateAngel = 270;
                }
                if(curNode == upNode){
                    rotateAction = new Rotate12Action(rotateAngel);
                }else if(curNode == downNode){
                    rotateAction = new Rotate13Action(rotateAngel);
                }else{
                    rotateAction = new Rotate10Action(rotateAngel);
                }
                curNode.setRobotAction(nodeInSeriesPath, rotateAction);
                RobotAction straightLine02Divide1Action = new StraightLine02Divide1Action();
                curNode.setRobotAction(nodeInSeriesPath, straightLine02Divide1Action);

            }else if(i == len-1){
                RobotAction robotAction;
                if(curNode.equals(upNode)){
                    robotAction = new Up20Action();
                }else if(curNode.equals(downNode)){
                    robotAction = new Down21Action();
                }else{
                    robotAction = new StopFFAction();
                }
                curNode.setRobotAction(nodeInSeriesPath, robotAction);
            }else if(i < len-1){
                CellNode preNode = neighbourCells.get(i-1);
                CellNode nextNode = neighbourCells.get(i+1);
                //当前点在前一个点上下方
                if(preNode.isUpNode(curNode) || preNode.isDownNode(curNode)){
                    if(curNode.isRightNode(nextNode)){
                        if(curNode.equals(upNode)){//旋转上升
                            RobotAction rotate12Action = new Rotate12Action(90);
                            curNode.setRobotAction(nodeInSeriesPath, rotate12Action);
                        }else if(curNode.equals(downNode)){//旋转下降
                            RobotAction rotate13Action = new Rotate13Action(90);
                            curNode.setRobotAction(nodeInSeriesPath, rotate13Action);
                        }else{//旋转
                            RobotAction rotate10Action = new Rotate10Action(90);
                            curNode.setRobotAction(nodeInSeriesPath, rotate10Action);
                        }
                    }else if(curNode.isLeftNode(nextNode)){
                        if(curNode.equals(upNode)){//旋转上升
                            RobotAction rotate12Action = new Rotate12Action(270);
                            curNode.setRobotAction(nodeInSeriesPath, rotate12Action);
                        }else if(curNode.equals(downNode)){//旋转下降
                            RobotAction rotate13Action = new Rotate13Action(270);
                            curNode.setRobotAction(nodeInSeriesPath, rotate13Action);
                        }else{//旋转
                            RobotAction rotate10Action = new Rotate10Action(270);
                            curNode.setRobotAction(nodeInSeriesPath, rotate10Action);
                        }
                    }else{
                        if(curNode.equals(upNode)){//顶升
                            RobotAction up20Action = new Up20Action();
                            curNode.setRobotAction(nodeInSeriesPath, up20Action);
                        }else if(curNode.equals(downNode)){//下降
                            RobotAction down21Action = new Down21Action();
                            curNode.setRobotAction(nodeInSeriesPath, down21Action);
                        }else{//直行
                            RobotAction straightLine02Divide1Action = new StraightLine02Divide1Action();
                            if(curNode.isFollowCellNode()){
                                straightLine02Divide1Action = new StraightLineFollow03Action();
                            }
                            curNode.setRobotAction(nodeInSeriesPath, straightLine02Divide1Action);
                        }
                    }
                }
                //当前点在前一个点的右左方
                if(preNode.isRightNode(curNode) || preNode.isLeftNode(curNode)){
                    if(curNode.isUpNode(nextNode)){
                        if(curNode.equals(upNode)){//旋转上升
                            RobotAction rotate12Action = new Rotate12Action(0);
                            curNode.setRobotAction(nodeInSeriesPath, rotate12Action);
                        }else if(curNode.equals(downNode)){//旋转下降
                            RobotAction rotate13Action = new Rotate13Action(0);
                            curNode.setRobotAction(nodeInSeriesPath, rotate13Action);
                        }else{//旋转
                            RobotAction rotate10Action = new Rotate10Action(0);
                            curNode.setRobotAction(nodeInSeriesPath, rotate10Action);
                        }
                    }else if(curNode.isDownNode(nextNode)){
                        if(curNode.equals(upNode)){//旋转上升
                            RobotAction rotate12Action = new Rotate12Action(180);
                            curNode.setRobotAction(nodeInSeriesPath, rotate12Action);
                        }else if(curNode.equals(downNode)){//旋转下降
                            RobotAction rotate13Action = new Rotate13Action(180);
                            curNode.setRobotAction(nodeInSeriesPath, rotate13Action);
                        }else{//旋转
                            RobotAction rotate10Action = new Rotate10Action(180);
                            curNode.setRobotAction(nodeInSeriesPath, rotate10Action);
                        }
                    }else{
                        if(curNode.equals(upNode)){//顶升
                            RobotAction up20Action = new Up20Action();
                            curNode.setRobotAction(nodeInSeriesPath, up20Action);
                        }else if(curNode.equals(downNode)){//下降
                            RobotAction down21Action = new Down21Action();
                            curNode.setRobotAction(nodeInSeriesPath, down21Action);
                        }else{//直行
                            RobotAction straightLine02Divide1Action = new StraightLine02Divide1Action();
                            if(curNode.isFollowCellNode()){
                                straightLine02Divide1Action = new StraightLineFollow03Action();
                            }
                            curNode.setRobotAction(nodeInSeriesPath, straightLine02Divide1Action);
                        }
                    }
                }
            }
        }
        // 给点添加动作结束------------------------------
        builder1 = new StringBuilder();
        builder1.append(wcsp.getAgvID()+"号车 动作路径串:\n");
        for(SeriesPath path : seriesPaths){
            builder1.append(path);
            builder1.append("\r\n");
        }
        LOG.debug(builder1.toString());




        //给串添加开始运动点  开始--------------------------------------------
        for(int i = 1, len = seriesPaths.size(); i < len; i++){
            SeriesPath preSeriesPath = seriesPaths.get(i-1);
            SeriesPath curSeriesPath = seriesPaths.get(i);
            CellNode curSeriesPathFirstNode = preSeriesPath.getPathList().getLast();
            CellNode curSeriesPathSecondNode = curSeriesPath.getPathList().getFirst();
            int rotateAngle = -1;
            if(curSeriesPathFirstNode.isUpNode(curSeriesPathSecondNode)){
                rotateAngle = 0;
            }else if(curSeriesPathFirstNode.isDownNode(curSeriesPathSecondNode)){
                rotateAngle = 180;
            }else if(curSeriesPathFirstNode.isRightNode(curSeriesPathSecondNode)){
                rotateAngle = 90;
            }else if(curSeriesPathFirstNode.isLeftNode(curSeriesPathSecondNode)){
                rotateAngle = 270;
            }
            RobotAction rotate10Action = new Rotate10Action(rotateAngle);
            curSeriesPathFirstNode.setRobotAction(curSeriesPath, rotate10Action);
            RobotAction straightLine02Divide1Action = new StraightLine02Divide1Action();
            curSeriesPathFirstNode.setRobotAction(curSeriesPath, straightLine02Divide1Action);
            curSeriesPath.getPathList().addFirst(curSeriesPathFirstNode);

            //如果上一条路径为直行改为停止
            if(curSeriesPathFirstNode.getRobotActionList(preSeriesPath) != null
                    && !curSeriesPathFirstNode.getRobotActionList(preSeriesPath).isEmpty()){
                RobotAction tmpAction = curSeriesPathFirstNode.getRobotActionList(preSeriesPath).get(0);
                if(tmpAction instanceof StraightLine02Divide1Action){
                    curSeriesPathFirstNode.getRobotActionList(preSeriesPath).clear();
                    curSeriesPathFirstNode.setRobotAction(preSeriesPath, new StopFFAction());
                }
            }
        }

        // 给串添加开始运动点 结束------------------------------
        builder1 = new StringBuilder();
        builder1.append(wcsp.getAgvID()+"号车 给串添加开始运动点:\n");
        for(SeriesPath path : seriesPaths){
            builder1.append(path);
            builder1.append("\r\n");
        }
        LOG.debug(builder1.toString());

        return seriesPaths;
    }




    /*
     对路径串的CELL进行分析,返回合法的路径串列表
    @param analysisCellNodeList 包含CELL的列表
    @return 合法的路径串列表
     */
    public LinkedList<SeriesPath> commonCellNodeListAnalysis(LinkedList<CellNode> analysisCellNodeList) {
        LinkedList<SeriesPath> rsSeriesPathList = null;
        //设置路径串CELL的领里关系
        RotatePathManager.getInstance().setSeriesPathNeighbour(analysisCellNodeList);

        rsSeriesPathList = wcsCells2SeriesPath(analysisCellNodeList,
                MapManager.getInstance().getMap().getMapCellByAddressCodeID(wcsp.getUpPodAddressCodeID()),
                MapManager.getInstance().getMap().getMapCellByAddressCodeID(wcsp.getDownPodAddressCodeID()));

        /*
         处理分割后的路径串,刨去CELL数量为0的路径串.
         */
        LinkedList<SeriesPath> tmpList = new LinkedList();
        for (SeriesPath tmpSP : rsSeriesPathList) {
            if (tmpSP.getCellListSize() == 0) {
                tmpList.addLast(tmpSP);
            }
        }
        for (SeriesPath tmpSP : tmpList) {
            rsSeriesPathList.remove(tmpSP);
        }
        return rsSeriesPathList;
    }

}
