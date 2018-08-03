package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.ResourceDTO;

import java.util.Map;

public interface ResourceService extends BaseService<ResourceDTO> {

    Map<String, String> getByLocale(String locale);
}
