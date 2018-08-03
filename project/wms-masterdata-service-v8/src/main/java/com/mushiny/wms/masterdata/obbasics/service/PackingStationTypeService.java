package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationTypeDTO;

import java.util.List;

public interface PackingStationTypeService extends BaseService<PackingStationTypeDTO> {

    List<PackingStationTypeDTO> getAll();

}
