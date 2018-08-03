package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemUnitDTO;

import java.util.List;

public interface ItemUnitService extends BaseService<ItemUnitDTO> {

    List<ItemUnitDTO> getAll();
}
