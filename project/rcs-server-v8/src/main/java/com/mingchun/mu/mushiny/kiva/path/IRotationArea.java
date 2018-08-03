package com.mingchun.mu.mushiny.kiva.path;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.rcs.server.KivaAGV;

import java.util.List;

/**
 * Created by Administrator on 2018/4/27 0027.
 */
public interface IRotationArea {
    boolean isCellNodeInRotationAreaCellNodes(CellNode cellNode);

    boolean isCellNodeInRotationAreaOutCellNodes(CellNode cellNode);

    boolean lockRotationArea(KivaAGV agv);

    boolean unlockRotationArea(KivaAGV agv);

    int getWorkStationAngle();

    long getRotationAddressCodeID();

    CellNode getRotationCellNode();

    List<CellNode> getRotationAreaCellNodes();

    List<CellNode> getRotationAreaOutCellNodes();

    int getId();





}