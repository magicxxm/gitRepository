package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationDTO;

import java.util.List;

public interface StockTakingStationService extends BaseService<StockTakingStationDTO> {

    void createMore(StockTakingStationDTO dto);

    List<StockTakingStationDTO> getAll();
}
