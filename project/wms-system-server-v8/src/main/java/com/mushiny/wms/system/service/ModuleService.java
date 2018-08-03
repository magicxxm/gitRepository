package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.ModuleDTO;

import java.util.List;


public interface ModuleService extends BaseService<ModuleDTO> {

    List<ModuleDTO> getByCurrentWarehouseIdAndUserId();
}
