/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.global.PathConfig;
import com.mushiny.rcs.global.RotateAreaConfig;
import com.mushiny.rcs.kiva.bus.action.AGVActionContainer;
import com.mushiny.rcs.kiva.bus.action.Charge30Action;
import com.mushiny.rcs.kiva.bus.action.StopFFAction;
import com.mushiny.rcs.kiva.bus.action.StraightLine02Action;
import com.mushiny.rcs.kiva.bus.action.StraightLine02Divide2Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class PathManager extends AGVActionContainer {

    protected static PathManager instance;
    static Logger LOG = LoggerFactory.getLogger(PathManager.class.getName());

    protected PathManager() {
        super();
    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new PathManager();
        }
    }

    public static PathManager getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static PathManager getInstance() {
        if (instance == null) {
            instance = new PathManager();
        }
        return instance;
    }
*/

    /*
     把路径串分割成可供AGV运行的路径列表，每一个都是可供AGV运行的路径
     */
    public LinkedList<SeriesPath> getSplitedSeriesPathList(SeriesPath seriesPath) {
        LinkedList<SeriesPath> seriesPathsList = new LinkedList();
        SeriesPath tmpSeriesPath = new SeriesPath();
        for (CellNode cellNode : seriesPath.getPathList()) {
            tmpSeriesPath.addPathCell(cellNode);
        }
        LinkedList<CellNode> tmpLinkedList = new LinkedList();
        while (tmpSeriesPath.getPathList().size() > 0) {
            CellNode cellNode = tmpSeriesPath.getPathList().removeFirst();
            if (cellNode.getAddressCodeID() != RotateAreaConfig.TURN_ADDRESS_CODE_ID) {
                tmpLinkedList.addLast(cellNode);
            } else {
                SeriesPath sp = new SeriesPath();
                sp.setCellPathList(tmpLinkedList);
                //设置路径的优先级
                for (CellNode cn : tmpLinkedList) {
                    if (RotateAreaManager.getInstance().getRotateAreaByCellNode(cn) != null) {
                        sp.setPriority(PathConfig.PATH_PRIORITY_ROTATE_AREA_EXIT);
                        break;
                    }
                }
                seriesPathsList.addLast(sp);
                tmpLinkedList.clear();
            }
        }
        if (tmpLinkedList.size() > 0) {
            SeriesPath sp = new SeriesPath();
            sp.setCellPathList(tmpLinkedList);
            //设置路径的优先级
            for (CellNode cn : tmpLinkedList) {
                if (RotateAreaManager.getInstance().getRotateAreaByCellNode(cn) != null) {
                    sp.setPriority(PathConfig.PATH_PRIORITY_ROTATE_AREA_EXIT);
                    break;
                }
            }
            seriesPathsList.addLast(sp);
        }
        return seriesPathsList;
    }

    /*
     对路径串进行初始化，包括设置CELL的左右邻居，动作类型,路径分割标志
     */
//    public void initSeriesPath(List<CellNode> cellNodeList) {
//        setSeriesPathNeighbour(cellNodeList);
//        setCellNodeAction(cellNodeList);
//        setSeriesPathSplitFlag(cellNodeList);
//    }

    /*
     对旋转处设置路径分割标志
     */
    public void setSeriesPathSplitFlag(List<CellNode> pathCellNodeList) {
        CellNode currentCellNode;
        LinkedList<CellNode> rsList = new LinkedList();
        for (int i = 0; i < pathCellNodeList.size(); i++) {
            currentCellNode = pathCellNodeList.get(i);
            if (!(currentCellNode.getRobotAction() instanceof StraightLine02Action)
                    && !(currentCellNode.getRobotAction() instanceof StraightLine02Divide2Action)
                    && !(currentCellNode.getRobotAction() instanceof Charge30Action)
                    && !(currentCellNode.getRobotAction() instanceof StopFFAction)
                    && (i!=pathCellNodeList.size()-1)) {
                rsList.add(RotateAreaManager.getInstance().getInventedTurnCellNode());
            }
            rsList.add(currentCellNode);
        }
        pathCellNodeList.clear();
        pathCellNodeList.addAll(rsList);
    }


    /*
     对指定的路径串进行前后左右邻居设定
    @return 返回具有邻居关系的路径
     */
    public List<CellNode> setSeriesPathNeighbour(List<CellNode> pathCellNodeList) {
        //计算邻居关系
        CellNode currentNode;
        CellNode nextNode;
        for (int i = 0; i < pathCellNodeList.size(); i++) {
            currentNode = pathCellNodeList.get(i);
            if ((i + 1) < pathCellNodeList.size()) {
                nextNode = pathCellNodeList.get(i + 1);
                currentNode.setNeighhour(nextNode);
            }
        }
        return pathCellNodeList;
    }

    /**
     * 根据起点beginCellNode和终点endCellNode获取指定路径,如果list是非路径，则返回原来的list
     *
     * @param list
     * @param beginCellNode
     * @param endCellNode
     * @return
     */
    public List<CellNode> getSortCellNodeListByBeginEnd(List<CellNode> list, CellNode beginCellNode, CellNode endCellNode) {

        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return null;
        }
        if (!isSeriesPathList(list)) {
            return null;
        }
        List<CellNode> resList = new LinkedList<CellNode>();

        List<CellNode> tmpList = new LinkedList<CellNode>();
        for (CellNode node : list) {
            if (node.getPoint() != null) {
                tmpList.add(node);
            }
        }

//起终点是否在所有能连成路径的起点中
        List<CellNode> starts = new LinkedList<CellNode>();
        for (CellNode node : tmpList) {
            List<CellNode> tlist = new LinkedList<CellNode>(tmpList);
            if (isSeriesPathList(tlist, node)) {
                starts.add(node);
            }
        }
        if (!starts.contains(beginCellNode) && !starts.contains(endCellNode)) {
            return null;
        }

        bunchList(tmpList, beginCellNode, resList);

//终点是否为指定的终点
        CellNode tmpEndCellNode = resList.get(resList.size() - 1);
        if (tmpEndCellNode != endCellNode) {
            resList = new LinkedList<CellNode>();
            tmpList = new LinkedList<CellNode>();
            for (CellNode node : list) {
                if (node.getPoint() != null) {
                    tmpList.add(node);
                }
            }
            bunchList(tmpList, endCellNode, resList);
            tmpEndCellNode = resList.get(resList.size() - 1);
            if (tmpEndCellNode != beginCellNode) {
                return null;
            }
            Collections.reverse(resList);
        }

        return resList;
    }

    /**
     * 获取某个串排序后的点集
     *
     * @param list 需要排序的点集
     * @return
     */
    public LinkedList<CellNode> getSortSeriesPathList(LinkedList<CellNode> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return list;
        }
        if (list.size() == 1) {
            return list;
        }
        if (!PathManager.this.isSeriesPathList(list)) {
            return list;
        }
        LOG.info("路径排序之前:");
        for (int i = 0; i < list.size(); i++) {
            LOG.info("cellNode " + (i + 1) + list.get(i).getPoint());
        }

        LinkedList<CellNode> resList = new LinkedList();
        LinkedList<CellNode> tmpList = new LinkedList();
        for (CellNode node : list) {
            if (node.getPoint() != null) {
                tmpList.add(node);
            }
        }
        CellNode startNode = null;
        for (CellNode node : tmpList) {
            List<CellNode> tlist = new LinkedList(tmpList);
            if (isSeriesPathList(tlist, node)) {
                startNode = node;
                break;
            }
        }
        bunchList(tmpList, startNode, resList);
//         LOG.info("路径排序之后:");
//        for(int i = 0;i < resList.size();i++) {
//            LOG.info("cellNode "+(i+1)+list.get(i).getPoint());
//        }
        return resList;
    }

    /**
     * 判断点集是否能连成串
     *
     * @param list 点集
     * @return boolean true能 false不能
     */
    public boolean isSeriesPathList(List<CellNode> list) {
        if (list == null) {
            return false;
        }
        if (list.isEmpty()) {
            return false;
        }
        List<CellNode> tmpList = new LinkedList();
        for (CellNode node : list) {
            if (node.getPoint() != null) {
                tmpList.add(node);
            }
        }
        Boolean resBool = false;
        for (CellNode node : tmpList) {
            List<CellNode> tlist = new LinkedList(tmpList);
            if (isSeriesPathList(tlist, node)) {
                resBool = true;
                break;
            }
        }
        return resBool;
    }

    /**
     * 判断两点是否正交相邻
     *
     * @param curCellNode 当前节点
     * @param nextCellNode 相邻节点
     * @return boolean true相邻， false不相邻
     */
    private boolean isNextPoint(CellNode curCellNode, CellNode nextCellNode) {
        if (curCellNode.getPoint().getX() == nextCellNode.getPoint().getX() && (curCellNode.getPoint().getY() + 1) == nextCellNode.getPoint().getY()) {
            return true;
        }
        if (curCellNode.getPoint().getX() == nextCellNode.getPoint().getX() && (curCellNode.getPoint().getY() - 1) == nextCellNode.getPoint().getY()) {
            return true;
        }
        if ((curCellNode.getPoint().getX() + 1) == nextCellNode.getPoint().getX() && curCellNode.getPoint().getY() == nextCellNode.getPoint().getY()) {
            return true;
        }
        if ((curCellNode.getPoint().getX() - 1) == nextCellNode.getPoint().getX() && curCellNode.getPoint().getY() == nextCellNode.getPoint().getY()) {
            return true;
        }
        return false;
    }

    /**
     * 搜索list中下一个串联点
     *
     * @param curCellNode 当前节点
     * @param list list节点容器
     * @return Point 返回获取到的Point
     */
    private CellNode findNextPoint(CellNode curCellNode, List<CellNode> list) {
        if (curCellNode == null) {
            return null;
        }
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        for (CellNode cellNode : list) {
            if (isNextPoint(curCellNode, cellNode)) {
                return cellNode;
            }
        }
        return null;
    }

    /**
     * 判断list中是否有下一个串联点
     *
     * @param curCellNode 当前节点
     * @param list list节点容器
     * @return Point 返回获取到的Point
     */
    private boolean findNextPointBool(CellNode curCellNode, List<CellNode> list) {
        CellNode cellNode = findNextPoint(curCellNode, list);
        return cellNode != null;
    }

    /**
     * 判断点集是否能连成串
     *
     * @param list 点集
     * @param startCellNode 开始连接的点
     * @return boolean true能 false不能
     */
    private boolean isSeriesPathList(List<CellNode> list, CellNode startCellNode) {
        if (list.size() == 1) {
            return true;
        }
        if (!findNextPointBool(startCellNode, list)) {
            return false;
        }
        list.remove(startCellNode);
        List<CellNode> choiceList = getPointChoice(startCellNode, list);
        int len = choiceList.size();
        if (len > 1) {
            boolean tmpBool = false;
            for (int i = 0; i < len; i++) {
                List<CellNode> leftList = new LinkedList<CellNode>();
                leftList.addAll(list);
                if (isSeriesPathList(leftList, choiceList.get(i))) {
                    tmpBool = true;
                    break;
                }
            }
            return tmpBool;
        } else {
            startCellNode = findNextPoint(startCellNode, list);
        }
        return isSeriesPathList(list, startCellNode);
    }

    /**
     * 获取当前点有几条选择
     *
     * @param curCellNode 当前串联点
     * @param leftList 剩余要串联的点集
     * @return List<Point> 返回可以串联上的点集
     */
    private List<CellNode> getPointChoice(CellNode curCellNode, List<CellNode> leftList) {
        List<CellNode> choiceList = new ArrayList<CellNode>();
        for (CellNode node : leftList) {
            if (isNextPoint(curCellNode, node)) {
                choiceList.add(node);
            }
        }
        return choiceList;
    }

    /**
     * 递归获取获取排序点集
     *
     * @param list 需要排序的点集
     * @param curCellNode 关联点
     * @param resList 结果点集
     */
    private void bunchList(List<CellNode> list, CellNode curCellNode, List<CellNode> resList) {
        resList.add(curCellNode);
        list.remove(curCellNode);
        if (list.size() == 1) {
            resList.add(list.get(0));
        } else {
            List<CellNode> choiceList = getPointChoice(curCellNode, list);
            if (choiceList.size() > 1) {
                for (CellNode node : choiceList) {
                    List<CellNode> tmpList = new LinkedList<CellNode>();
                    tmpList.addAll(list);
                    if (isSeriesPathList(tmpList, node)) {
                        curCellNode = node;
                        break;
                    }
                }
            } else {
                curCellNode = findNextPoint(curCellNode, list);
            }
            bunchList(list, curCellNode, resList);
        }
    }

}
