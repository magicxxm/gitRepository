package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;

import java.util.List;

public interface NormalToProblemService {

    //确认将正常订单转为问题订单处理
    List<SolveShipmentPositionDTO> normalToProblem(String obpStationId, String obpWallId, String shipmentNo);

}
