package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationTypeDTO;

import java.util.List;

public interface StockTakingStationTypeService extends BaseService<StockTakingStationTypeDTO> {

    List<StockTakingStationTypeDTO> getAll();

}
