package com.mushiny.wms.outboundproblem.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolveCheckDTO;


public interface OBPSolveCheckService extends BaseService<OBPSolveCheckDTO> {

    StorageLocationDTO getStorageLocationIdByName(String storageLocationName);

}
