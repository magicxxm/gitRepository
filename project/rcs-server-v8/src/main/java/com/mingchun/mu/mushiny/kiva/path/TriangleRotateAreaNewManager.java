package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;

import java.util.LinkedList;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class TriangleRotateAreaNewManager {

    private TriangleRotateAreaNewManager() {
        mapManager = MapManager.getInstance();
    }
    private static TriangleRotateAreaNewManager instance = null;
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new TriangleRotateAreaNewManager();
        }
    }
    public static synchronized TriangleRotateAreaNewManager getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }


    private MapManager mapManager;
    private final LinkedList<TriangleRotateAreaNew> rotateAreaLinkedList = new LinkedList();

    // 添加三点旋转区
    public void addTriangleRotateAreaNew(TriangleRotateAreaNew triangleRotateAreaNew){
        if(triangleRotateAreaNew != null && !rotateAreaLinkedList.contains(triangleRotateAreaNew)){
            ((MapCellNode)(triangleRotateAreaNew.getEnterCellNode())).fireOnCellCommonUpdate();
            ((MapCellNode)(triangleRotateAreaNew.getRotationExitCellNode())).fireOnCellCommonUpdate();
            rotateAreaLinkedList.add(triangleRotateAreaNew);
        }
    }

    // 移除三点旋转区
    public void removeTriangleRotateAreaNew(TriangleRotateAreaNew triangleRotateAreaNew){
        if(triangleRotateAreaNew != null && rotateAreaLinkedList.contains(triangleRotateAreaNew)){
            rotateAreaLinkedList.remove(triangleRotateAreaNew);
        }
    }

    // 通过旋转区的点获取旋转区
    public TriangleRotateAreaNew getTriangleRotateAreaNewByCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(TriangleRotateAreaNew triangleRotateAreaNew: rotateAreaLinkedList){
            if(triangleRotateAreaNew.getEnterCellNode().equals(cellNode)
                    || triangleRotateAreaNew.getRotationExitCellNode().equals(cellNode)){
                return triangleRotateAreaNew;
            }
        }
        return null;
    }
    // 通过旋转点获取旋转区
    public TriangleRotateAreaNew getTriangleRotateAreaNewByRotateCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(TriangleRotateAreaNew triangleRotateAreaNew: rotateAreaLinkedList){
            if(triangleRotateAreaNew.getRotationExitCellNode().equals(cellNode)){
                return triangleRotateAreaNew;
            }
        }
        return null;
    }
    // 通过旋转区的点获取旋转区
    public TriangleRotateAreaNew getTriangleRotateAreaNewByCellNode(long addressCodeID){
        if(addressCodeID <= 0){
            return null;
        }
        if(addressCodeID > mapManager.getMap().getMaxAddressCodeID()){
            return null;
        }
        CellNode cellNode = mapManager.getMap().getMapCellByAddressCodeID(addressCodeID);
        return getTriangleRotateAreaNewByCellNode(cellNode);
    }

    public boolean isInTriangleRotateAreaNew(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(getTriangleRotateAreaNewByCellNode(cellNode) != null){
            return true;
        }
        return false;
    }
    public boolean isInTriangleRotateAreaNew(long addressCodeId){
        if(addressCodeId <= 0){
            return false;
        }
        if(addressCodeId > mapManager.getMap().getMaxAddressCodeID()){
            return false;
        }
        return isInTriangleRotateAreaNew(mapManager.getMap().getMapCellByAddressCodeID(addressCodeId));
    }




}
