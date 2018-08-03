package com.mushiny.wms.tot.general.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.general.crud.dto.WarehouseDTO;

import java.util.List;

public interface WarehouseService extends BaseService<WarehouseDTO> {

    List<WarehouseDTO> getByCurrentUserId();
    List<WarehouseDTO> getCurrentWarehouse();
}
