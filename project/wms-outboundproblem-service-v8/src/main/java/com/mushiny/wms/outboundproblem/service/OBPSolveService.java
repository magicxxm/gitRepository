package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;

import java.util.List;
import java.util.Map;

public interface OBPSolveService extends BaseService<OBPSolveDTO> {

    List<OBPCheckStateDTO> getSolveBySeek(String state, String userName, String seek,
                                          String startDate, String endDate);

    List<OBPSolveDTO> getAll();

    //扫描订单得到订单下的问题列表
    List<OBPSolveDTO> getProblemsByShipmentNo(String obpStationId, String obpWallId, String shipmentNo, String state);

    //点击问题列表，得到商品详情
    List<SolveShipmentPositionDTO> getSolveShipmentPositionByShipmentNo(String shipmentNo);

    //扫描商品详情中的所有商品
    SolveShipmentPositionDTO scanItemByShipmentAndItem(String toCellName, String shipmentNo, String itemNo);

    //扫描SN
    void scanSerialNo(String toCellName, String shipmentNo, String itemNo, String serialNo);

    //扫描容器
    StorageLocationDTO scanProcessContainer(String containerName);

    //扫描hot pick拣货回来的车，显示cell格的状态
    OBPCellDTO getCellByContainer(String containerName, String opbWallId);

    //hot pick拣货回来后，扫描车内商品
    OBPCellDTO getCellByContainerAndItemNo(String containerName, String obpWallId, String itemNo);

    //hot pick拣货回来后，扫描商品序列号
    OBPCellDTO scanHotPickSn(String containerName, String obpWallId, String itemNo,String serialNo);

    //点击问题货格，查看详情
    List<OBPSolveDTO> getOBProblemByCellName(String obpWallId, String cellName);

    //退出OB问题处理工作站
    void signOutOBPStation(String obpStationId);

    //送去包装(送去包装的同时,需要释放问题处理格;此处送去包装功能同释放问题格功能一致)
    //void gotoPacking(String shipmentNo, String obpStationId, String cellName);

    void assignLocation(String name,String solveid,String obpLocationId,String obpWallId);

    PrintShipmentPositionDTO printOrderByShipmentNo (String shipmentNo,String solveKey);

    //全部送去包装
    void batchToPacking(String dtos);

    boolean verifySignOutOBPStation(String obpStationId);

    List<Map> getLocations (List<String> cellNames,String workStationId);

    SolveShipmentPositionDTO scanPickingGoods(String location,String itemNo);

    void scanGoodsSn (String serialNo,String location,String itemNo);
}
