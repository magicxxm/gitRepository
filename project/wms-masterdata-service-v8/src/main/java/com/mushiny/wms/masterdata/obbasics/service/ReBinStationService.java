package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationDTO;

import java.util.List;

public interface ReBinStationService extends BaseService<ReBinStationDTO> {

    List<ReBinStationDTO> getAll();
}
