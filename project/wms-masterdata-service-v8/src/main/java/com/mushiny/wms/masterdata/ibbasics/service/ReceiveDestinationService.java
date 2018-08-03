package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;

import java.util.List;

public interface ReceiveDestinationService extends BaseService<ReceiveDestinationDTO> {

    List<ReceiveDestinationDTO> getAll();
}
