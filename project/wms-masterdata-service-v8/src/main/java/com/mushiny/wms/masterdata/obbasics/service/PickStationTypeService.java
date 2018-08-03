package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationTypeDTO;

import java.util.List;

public interface PickStationTypeService extends BaseService<PickStationTypeDTO> {

    List<PickStationTypeDTO> getAll();

}
