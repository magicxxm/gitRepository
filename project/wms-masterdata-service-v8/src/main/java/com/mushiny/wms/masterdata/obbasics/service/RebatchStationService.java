package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationDTO;

import java.util.List;

public interface RebatchStationService extends BaseService<RebatchStationDTO> {

    void createMore(RebatchStationDTO dto);

    List<RebatchStationDTO> getAll();
}
