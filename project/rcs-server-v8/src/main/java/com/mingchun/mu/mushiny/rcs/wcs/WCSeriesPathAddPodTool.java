package com.mingchun.mu.mushiny.rcs.wcs;

import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNode;
import com.mingchun.mu.mushiny.kiva.individual.IndividualCellNodeManager;
import com.mingchun.mu.mushiny.kiva.path.*;
import com.mushiny.kiva.map.Cell;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.RotateAreaManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.*;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import com.mushiny.rcs.wcs.WCSeriesPathTool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *   WCS下发路径转换成AGV可执行的路径串
 *
 *  mingchun.mu@mushiny.com  路径中增加上升或下降的pod
 */
public class WCSeriesPathAddPodTool extends WCSeriesPathTool {
    private KivaMap kivaMap;
    public WCSeriesPathAddPodTool(WCSSeriesPath wcsp) {
        super(wcsp);
    }
    @Override
    public void analysis() {
        super.analysis();
        if(kivaMap == null){
            kivaMap = MapManager.getInstance().getMap();
        }
        int podID = wcsp.getPodCodeID();
        CellNode upCellNode = kivaMap.getMapCellByAddressCodeID(wcsp.getUpPodAddressCodeID());
        CellNode downCellNode = kivaMap.getMapCellByAddressCodeID(wcsp.getDownPodAddressCodeID());
        if(upCellNode != null || downCellNode != null){
            LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
            CellNode endCellNode = resPath.getLast().getPathList().getLast();
            for(SeriesPath seriesPath: resPath){
                for(CellNode cellNode: seriesPath.getPathList()){
                    if(cellNode.equals(upCellNode)){ // 上升点
                        LinkedList<RobotAction> robotActionList = cellNode.getRobotActionList(seriesPath);
                        RobotAction tempTobotAction = null;
                        for(RobotAction action : robotActionList){
                            if(action instanceof Up20Action
                                    || action instanceof Rotate12Action){
                                tempTobotAction = action;
                                break;
                            }
                        }
                        if(tempTobotAction != null){
                            robotActionList.remove(tempTobotAction);
                            if(tempTobotAction instanceof Up20Action){ //上升 --> 加上podID参数
                                RobotAction up20PodIDAction = new Up20PodIDAction(podID);
                                robotActionList.addFirst(up20PodIDAction);
                                seriesPath.setUpCellNode(upCellNode);
                                seriesPath.setUpPodID(podID);
                            }
                            if(tempTobotAction instanceof Rotate12Action){// 旋转上升 --> 上升 + 旋转
                                Rotate12Action rotate12Action = (Rotate12Action)tempTobotAction;
                                robotActionList.addFirst(new Rotate10Action(rotate12Action.getActionParameter()));
                                robotActionList.addFirst(new Up20PodIDAction(podID));
                                seriesPath.setUpCellNode(upCellNode);
                                seriesPath.setUpPodID(podID);
                            }
                        }
                    }
                    if(cellNode.equals(downCellNode)){ // 下降点
                        LinkedList<RobotAction> robotActionList = cellNode.getRobotActionList(seriesPath);
                        RobotAction tempTobotAction = null;
                        for(RobotAction action : robotActionList){
                            if(action instanceof Down21Action
                                    || action instanceof Rotate13Action){
                                tempTobotAction = action;
                                break;
                            }
                        }
                        if(tempTobotAction != null){
                            robotActionList.remove(tempTobotAction);
                            if(tempTobotAction instanceof Down21Action){// 下降 --> 加上podID参数
                                RobotAction down21PodIDAction = new Down21PodIDAction(podID);
                                robotActionList.addFirst(down21PodIDAction);
                                seriesPath.setDownCellNode(downCellNode);
                                seriesPath.setUpPodID(podID);
                            }
                            if(tempTobotAction instanceof Rotate13Action){// 旋转下降 --> 下降 + 旋转
                                Rotate13Action rotate13Action = (Rotate13Action)tempTobotAction;
                                robotActionList.addFirst(new Rotate10Action(rotate13Action.getActionParameter()));
                                robotActionList.addFirst(new Down21PodIDAction(podID));
                                seriesPath.setDownCellNode(downCellNode);
                                seriesPath.setUpPodID(podID);
                            }
                        }
                    }
                }
            }
        }

        // 加上三角旋转区的旋转角度
//        addTriRotateAreaAngle();

        // 加上新三角旋转区的旋转角度
        addTriRotateAreaNewAngle();

        // 添加旋转区的旋转角度
        addRotationAreaRotationAngle();

        // 加上孤立点的旋转角度
        addIndividualCellNodeAngle();



    }

    /**
     * 加上三角旋转区的旋转角度
     */
    private void addTriRotateAreaAngle(){
        LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
        TriangleRotateArea triangleRotateArea = null;
        A:for(SeriesPath seriesPath: resPath){
            for(CellNode cellNode: seriesPath.getPathList()){
                triangleRotateArea = TriangleRotateAreaManager.getInstance().getTriangleRotateAreaByCellNode(cellNode);
                if(triangleRotateArea != null){
                    RobotAction rotate11Action = new Rotate11Action(wcsp.getRotateTheta());
                    triangleRotateArea.getRotationCellNode().getRobotActionList(seriesPath).addFirst(rotate11Action);
//                    triangleRotateArea.getRotationCellNode().setRobotAction(seriesPath, rotate11Action);
                    break A; // 只需一条路径添加旋转角度即可
                }
            }
        }
    }

    private void addRotationAreaRotationAngle(){
        LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
        IRotationArea rotationArea = null;
        CellNode tempCellNode = null;
        for(SeriesPath seriesPath: resPath){
            rotationArea = null;
            tempCellNode = seriesPath.getPathList().getFirst();
            IRotationArea tempRotationArea = RotationAreaManager.getInstance().getRotationAreaByRotationCellNode(tempCellNode);
            if(tempRotationArea == null){
                int len = seriesPath.getPathList().size() - 1;
                for(int i = 0; i < len; i++){
                    tempRotationArea = RotationAreaManager.getInstance().getRotationAreaByRotationCellNode(seriesPath.getPathList().get(i));
                    if(tempRotationArea != null){
                        rotationArea = tempRotationArea;
                        break;
                    }
                }
            }else {
                rotationArea = tempRotationArea;
            }
            if(rotationArea != null){
                if(rotationArea.getRotationCellNode().getRobotActionList(seriesPath) != null){
                    RobotAction rotate11Action = new Rotate11Action(wcsp.getRotateTheta());
                    LinkedList<RobotAction> actionLinkedList = rotationArea.getRotationCellNode().getRobotActionList(seriesPath);
                    boolean isUp = false;
                    for(RobotAction action: actionLinkedList){
                        if(action instanceof Up20PodIDAction){
                            isUp = true;
                        }
                    }
                    if(isUp){
                        actionLinkedList.add(2,rotate11Action);
                    }else{
                        actionLinkedList.add(1,rotate11Action);
                    }
                }
            }
        }
    }


    /**
     * 加上新三角旋转区的旋转角度
     */
    private void addTriRotateAreaNewAngle(){
        LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
        TriangleRotateAreaNew triangleRotateAreaNew = null;
        CellNode tempCellNode = null;
        for(SeriesPath seriesPath: resPath){
            triangleRotateAreaNew = null;
            tempCellNode = seriesPath.getPathList().getFirst();
            TriangleRotateAreaNew tempTriangleRotateAreaNew = TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByRotateCellNode(tempCellNode);
            if(tempTriangleRotateAreaNew == null){
                int len = seriesPath.getPathList().size() - 1;
                for(int i = 0; i < len; i++){
                    tempTriangleRotateAreaNew = TriangleRotateAreaNewManager.getInstance().getTriangleRotateAreaNewByRotateCellNode(seriesPath.getPathList().get(i));
                    if(tempTriangleRotateAreaNew != null){
                        tempCellNode = seriesPath.getPathList().get(i);
                        triangleRotateAreaNew = tempTriangleRotateAreaNew;
                    }
                }
            }else {
                triangleRotateAreaNew = tempTriangleRotateAreaNew;
            }
            if(triangleRotateAreaNew != null){
                if(triangleRotateAreaNew.getRotationExitCellNode().getRobotActionList(seriesPath) != null){
                    RobotAction rotate11Action = new Rotate11Action(wcsp.getRotateTheta());
                    LinkedList<RobotAction> actionLinkedList = triangleRotateAreaNew.getRotationExitCellNode().getRobotActionList(seriesPath);
                    boolean isUp = false;
                    for(RobotAction action: actionLinkedList){
                        if(action instanceof Up20PodIDAction){
                            isUp = true;
                        }
                    }
                    if(isUp){
                        actionLinkedList.add(2,rotate11Action);
                    }else{
                        actionLinkedList.add(1,rotate11Action);
                    }
                }
            }
        }
    }

    /**
     * 加上孤立点的旋转角度
     */
    private void addIndividualCellNodeAngle(){
        LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
        IndividualCellNode individualCellNode = null;
        for(SeriesPath seriesPath: resPath){
            int first = -1;
            for(CellNode cellNode: seriesPath.getPathList()){
                first ++ ;
                if(first != 0){
                    individualCellNode = IndividualCellNodeManager.getInstance().getIndividualCellNodeByIndividualCellNode(cellNode);
                    if(individualCellNode != null){
                        RobotAction rotate11Action = new Rotate11Action(wcsp.getRotateTheta());
                        individualCellNode.getIndividualNode().getRobotActionList(seriesPath).addFirst(rotate11Action);
//                    individualCellNode.getIndividualNode().setRobotAction(seriesPath, rotate11Action);
                    }
                }
            }
        }
    }



}
