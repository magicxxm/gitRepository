package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;

import java.util.List;

public interface SortingStationService extends BaseService<SortingStationDTO> {

    void createMore(SortingStationDTO dto);

    List<SortingStationDTO> getAll();
}
