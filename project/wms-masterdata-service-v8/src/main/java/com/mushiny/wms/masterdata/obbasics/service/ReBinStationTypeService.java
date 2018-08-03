package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationTypeDTO;

import java.util.List;

public interface ReBinStationTypeService extends BaseService<ReBinStationTypeDTO> {

    List<ReBinStationTypeDTO> getAll();
}
