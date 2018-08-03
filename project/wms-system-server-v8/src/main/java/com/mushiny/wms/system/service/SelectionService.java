package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.SelectionDTO;

import java.util.List;

public interface SelectionService extends BaseService<SelectionDTO> {

    List<SelectionDTO> getBySelectionKey(String selectionKey);
}
