package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.ForceDeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DeleteShipmentService {

    //查询已删除订单
    List<ForceDeleteShipmentDTO> queryForceDeleteShipment(String startDate, String endDate, String shipmentNo, String state, String obpStationId, String obpWallId);

    //添加删除订单原因
    void addDeleteReason(String shipmentNo, String deleteReason);

    //删订单时，将商品放到处理车牌中
    void putForceDeleteGoodsToContainer(DeleteShipmentDTO deleteShipmentDTO);

}
