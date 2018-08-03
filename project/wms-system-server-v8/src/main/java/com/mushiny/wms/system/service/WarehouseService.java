package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;

import java.util.List;

public interface WarehouseService extends BaseService<WarehouseDTO> {

    List<WarehouseDTO> getByCurrentUserId();
}
