package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationTypeDTO;

import java.util.List;

public interface RebatchStationTypeService extends BaseService<RebatchStationTypeDTO> {

    List<RebatchStationTypeDTO> getAll();

}
