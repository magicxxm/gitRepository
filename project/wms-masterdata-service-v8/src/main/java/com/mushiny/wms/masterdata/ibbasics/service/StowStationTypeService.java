package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypeDTO;

import java.util.List;

public interface StowStationTypeService extends BaseService<StowStationTypeDTO> {

    List<StowStationTypeDTO> getAll();
}
