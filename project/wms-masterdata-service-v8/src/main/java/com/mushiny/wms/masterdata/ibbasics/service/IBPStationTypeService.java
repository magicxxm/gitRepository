package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationTypeDTO;

import java.util.List;

public interface IBPStationTypeService extends BaseService<IBPStationTypeDTO> {

    List<IBPStationTypeDTO> getAll();

}
