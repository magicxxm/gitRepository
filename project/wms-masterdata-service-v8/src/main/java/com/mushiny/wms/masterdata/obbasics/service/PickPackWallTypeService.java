package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallTypeDTO;

import java.util.List;

public interface PickPackWallTypeService extends BaseService<PickPackWallTypeDTO> {

    List<PickPackWallTypeDTO> getAll();
}
