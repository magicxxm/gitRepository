package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;

import java.util.List;

public interface ItemGroupService extends BaseService<ItemGroupDTO> {

    List<ItemGroupDTO> getAll();
}
