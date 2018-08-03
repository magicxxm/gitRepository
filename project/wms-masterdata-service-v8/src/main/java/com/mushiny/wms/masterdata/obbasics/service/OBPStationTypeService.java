package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypeDTO;

import java.util.List;

public interface OBPStationTypeService extends BaseService<OBPStationTypeDTO> {

    List<OBPStationTypeDTO> getAll();

}
