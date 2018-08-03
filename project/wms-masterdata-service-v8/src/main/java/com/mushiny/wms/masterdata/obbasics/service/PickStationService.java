package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationDTO;

import java.util.List;

public interface PickStationService extends BaseService<PickStationDTO> {

    void createMore(PickStationDTO dto);

    List<PickStationDTO> getAll();
}
