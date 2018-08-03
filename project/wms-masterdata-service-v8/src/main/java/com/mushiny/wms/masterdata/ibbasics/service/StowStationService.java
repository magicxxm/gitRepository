package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationDTO;
import com.mushiny.wms.masterdata.mdbasics.business.dto.PodStorageLocationsDTO;

import java.util.List;

public interface StowStationService extends BaseService<StowStationDTO> {

    void createMore(StowStationDTO dto);

    List<StowStationDTO> getAll();
}
