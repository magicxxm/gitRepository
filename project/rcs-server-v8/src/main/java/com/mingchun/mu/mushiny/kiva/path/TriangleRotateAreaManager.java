package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;

import java.util.LinkedList;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class TriangleRotateAreaManager {

    private TriangleRotateAreaManager() {
    }
    private static TriangleRotateAreaManager instance = null;
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new TriangleRotateAreaManager();
        }
    }
    public static synchronized TriangleRotateAreaManager getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }


    private final LinkedList<TriangleRotateArea> rotateAreaLinkedList = new LinkedList();

    // 添加三点旋转区
    public void addTriangleRotateArea(TriangleRotateArea triangleRotateArea){
        if(triangleRotateArea != null && !rotateAreaLinkedList.contains(triangleRotateArea)){
            ((MapCellNode)(triangleRotateArea.getEnterCellNode())).fireOnCellCommonUpdate();
            ((MapCellNode)(triangleRotateArea.getRotationCellNode())).fireOnCellCommonUpdate();
            ((MapCellNode)(triangleRotateArea.getExitCellNode())).fireOnCellCommonUpdate();
            rotateAreaLinkedList.add(triangleRotateArea);
        }
    }

    // 移除三点旋转区
    public void removeTriangleRotateArea(TriangleRotateArea triangleRotateArea){
        if(triangleRotateArea != null && rotateAreaLinkedList.contains(triangleRotateArea)){
            rotateAreaLinkedList.remove(triangleRotateArea);
        }
    }

    // 通过旋转区的点获取旋转区
    public TriangleRotateArea getTriangleRotateAreaByCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(TriangleRotateArea triangleRotateArea: rotateAreaLinkedList){
            if(triangleRotateArea.getEnterCellNode().equals(cellNode)
                    || triangleRotateArea.getRotationCellNode().equals(cellNode)
                    || triangleRotateArea.getExitCellNode().equals(cellNode)){
                return triangleRotateArea;
            }
        }
        return null;
    }
    // 通过旋转区的点获取旋转区
    public TriangleRotateArea getTriangleRotateAreaByCellNode(long addressCodeID){
        if(addressCodeID <= 0){
            return null;
        }
        if(addressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            return null;
        }
        CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCodeID);
        return getTriangleRotateAreaByCellNode(cellNode);
    }

    public boolean isInTriangleRotateArea(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(getTriangleRotateAreaByCellNode(cellNode) != null){
            return true;
        }
        return false;
    }




}
