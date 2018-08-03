package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPWallTypeDTO;

import java.util.List;

public interface OBPWallTypeService extends BaseService<OBPWallTypeDTO> {

    List<OBPWallTypeDTO> getAll();
}
