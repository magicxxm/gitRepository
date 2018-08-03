package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationTypeDTO;

import java.util.List;

public interface SortingStationTypeService extends BaseService<SortingStationTypeDTO> {

    List<SortingStationTypeDTO> getAll();

}
