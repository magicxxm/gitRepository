package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Laptop-6 on 2017/9/29.
 */
public class RotationAreaManager {

    private RotationAreaManager() {
    }
    private static RotationAreaManager instance = null;
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RotationAreaManager();
        }
    }
    public static synchronized RotationAreaManager getInstance(){
        if(instance == null){
            initInstance();
        }
        return instance;
    }


    private final List<IRotationArea> rotationAreaList = new CopyOnWriteArrayList<>();

    // 添加旋转区
    public void addRotationArea(IRotationArea rotationArea){
        if(rotationArea != null && !rotationAreaList.contains(rotationArea)){
            rotationAreaList.add(rotationArea);
        }
    }

    // 移除旋转区
    public void removeRotationArea(IRotationArea rotationArea){
        if(rotationArea != null && rotationAreaList.contains(rotationArea)){
            rotationAreaList.remove(rotationArea);
        }
    }

    // 通过旋转区的旋转点获取旋转区
    public IRotationArea getRotationAreaByRotationCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(IRotationArea rotationArea: rotationAreaList){
            if(rotationArea.getRotationCellNode().getAddressCodeID() == cellNode.getAddressCodeID()){
                return rotationArea;
            }
        }
        return null;
    }
    // 通过旋转区的点获取旋转区
    public IRotationArea getRotationArea(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(IRotationArea rotationArea: rotationAreaList){
            if(rotationArea.isCellNodeInRotationAreaCellNodes(cellNode)){
                return rotationArea;
            }
        }
        return null;
    }
    // 通过旋转区的点获取旋转区
    public IRotationArea getRotationArea(long addressCodeID){
        if(addressCodeID <= 0){
            return null;
        }
        if(addressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            return null;
        }
        CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCodeID);
        return getRotationArea(cellNode);
    }
    // 通过旋转区的出口点获取旋转区
    public IRotationArea getRotationAreaByOutCellNode(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        for(IRotationArea rotationArea: rotationAreaList){
            if(rotationArea.isCellNodeInRotationAreaOutCellNodes(cellNode)){
                return rotationArea;
            }
        }
        return null;
    }
    // 通过旋转区的点获取旋转区
    public IRotationArea getRotationAreaByOutCellNode(long addressCodeID){
        if(addressCodeID <= 0){
            return null;
        }
        if(addressCodeID > MapManager.getInstance().getMap().getMaxAddressCodeID()){
            return null;
        }
        CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCodeID);
        return getRotationAreaByOutCellNode(cellNode);
    }

    public boolean isInRotationArea(CellNode cellNode){
        if(cellNode == null){
            return false;
        }
        if(getRotationArea(cellNode) != null){
            return true;
        }
        return false;
    }

    public boolean isInRotationArea(long addressCodeId){
        if(getRotationArea(addressCodeId) != null){
            return true;
        }
        return false;
    }


    public List<IRotationArea> getRotationAreaList() {
        return rotationAreaList;
    }
}
