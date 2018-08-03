package com.mingchun.mu.mushiny.kiva.individual;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;

import java.util.LinkedList;

/**
 *  管理一组孤立点
 *
 */
public class IndividualCellNodeManager {

    private static IndividualCellNodeManager instance = null;
    private IndividualCellNodeManager() {
    }
    private synchronized static void initInstance(){
        if(instance == null){
            instance = new IndividualCellNodeManager();
        }
    }
    public synchronized static IndividualCellNodeManager getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }


    LinkedList<IndividualCellNode> individualCellNodeList = new LinkedList<>();

    public void addIndividualCellNode(IndividualCellNode individualCellNode){
        if(individualCellNode != null
                && !individualCellNodeList.contains(individualCellNode)){
            individualCellNodeList.add(individualCellNode);
        }
    }


    public void removeIndividualCellNode(IndividualCellNode individualCellNode){
        if(individualCellNode != null
                && individualCellNodeList.contains(individualCellNode)){
            individualCellNodeList.remove(individualCellNode);
        }
    }

    /**
     * 是否需要解锁入口点  -- 当前点为孤立点， 不需要解锁； 否则都需要。
     * @param curNode 当前点
     * @return 需要解锁入口点返回true
     */
    public boolean isNeedUnlockEnterNode(CellNode curNode){
        if(curNode == null){
            return true;
        }
        for(IndividualCellNode node: individualCellNodeList){
            if(node.getIndividualNode().equals(curNode)){
                return false;
            }
        }
        return true;
    }


    public IndividualCellNode getIndividualCellNodeByIndividualCellNode(CellNode individualCellNode){
        if(individualCellNode == null){
            return null;
        }
        for(IndividualCellNode node: individualCellNodeList){
            if(node.getIndividualNode().equals(individualCellNode)){
                return node;
            }
        }
        return null;
    }

    public IndividualCellNode getIndividualCellNodeByCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(IndividualCellNode node: individualCellNodeList){
            if(node.getIndividualNode().equals(cellNode)
                    || node.getEnterNode().equals(cellNode)){
                return node;
            }
        }
        return null;
    }
    public IndividualCellNode getIndividualCellNodeByIndividualCellNode(long individualAddressCodeID){
        if(individualAddressCodeID <= 0){
            return null;
        }
        if(individualAddressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            return null;
        }
        CellNode individualCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(individualAddressCodeID);
        return getIndividualCellNodeByIndividualCellNode(individualCellNode);
    }





}
