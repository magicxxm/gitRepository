package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.SolveDamagedDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;

import java.util.List;
import java.util.Map;

public interface SolveDamagedService extends BaseService<SolveDamagedDTO> {

//    void damagedToNormal(String stationName, String shipmentNo, String itemDateNo, String solveKey, String location);

    //残品转正品
    void damagedToNormal(OBPSolvePositionDTO obpSolvePositionDTO);

    //确认残品
    void confirmDamaged(OBPSolvePositionDTO obpSolvePositionDTO);

    //生成HotPick
    void generateHotPick(OBPSolvePositionDTO obpSolvePositionDTO);

    //可以分配的货位
    List<Map> searchStorageLocationByItemNo(String itemNo,String sectionId);

    //分配货位
    OBPSolvePositionDTO allocationStorageLocation(OBPSolvePositionDTO obpSolvePositionDTO);

//    //检查有效期，并重设有效期(暂可不用该接口)
//    void checkItemLot(String fromContainer, String toContainer, String shipmentNo, String itemNO, LocalDate useNotAfter, BigDecimal amount);

    //扫描残品到容器中
    void putDamagedToContainer(String containerName, String shipmentNo, String itemNo, String useNotAfter,String serialNo);

    //确认删除订单时
    void deleteShipment(OBPSolvePositionDTO obpSolvePositionDTO);

    //确认删除订单后，扫描商品到容器中
    void deleteShipmentScanGoods(DeleteShipmentDTO deleteShipmentDTO);

    //拆单发货
    void dismantleShipment(String shipmentNo, String solveKey);

    List<Map> getPodFace(List<String> names,String obpStationId);

}
