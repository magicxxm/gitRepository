package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationDTO;

import java.util.List;

public interface OBPStationService extends BaseService<OBPStationDTO> {

    void createMore(OBPStationDTO dto);

    List<OBPStationDTO> getAll();
}
