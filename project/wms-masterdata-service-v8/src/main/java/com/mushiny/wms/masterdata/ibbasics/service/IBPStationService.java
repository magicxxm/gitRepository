package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationDTO;

import java.util.List;

public interface IBPStationService extends BaseService<IBPStationDTO> {

    void createMore(IBPStationDTO dto);

    List<IBPStationDTO> getAll();
}
