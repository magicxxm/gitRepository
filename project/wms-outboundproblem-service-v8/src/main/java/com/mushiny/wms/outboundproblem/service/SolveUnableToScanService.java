package com.mushiny.wms.outboundproblem.service;


import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.domain.common.ItemDataGlobal;

import java.util.List;

public interface SolveUnableToScanService {

    //打印商品条码
   ItemDataGlobal printSKUNo(OBPSolvePositionDTO obpSolvePositionDTO);

    //序列号无法扫描，转为待调查
    void pendingInvestigation(OBPSolvePositionDTO obpSolvePositionDTO);

}
