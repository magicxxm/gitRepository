package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypeDTO;

import java.util.List;

public interface ReceiveStationTypeService extends BaseService<ReceiveStationTypeDTO> {

    List<ReceiveStationTypeDTO> getAll();
}
