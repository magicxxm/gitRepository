package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationTypeDTO;

import java.util.List;

public interface StorageLocationTypeService extends BaseService<StorageLocationTypeDTO> {

    List<StorageLocationTypeDTO> getAll();
}
