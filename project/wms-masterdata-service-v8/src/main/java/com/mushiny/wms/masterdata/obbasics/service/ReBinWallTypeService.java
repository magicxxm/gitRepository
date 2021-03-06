package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypeDTO;

import java.util.List;

public interface ReBinWallTypeService extends BaseService<ReBinWallTypeDTO> {

    List<ReBinWallTypeDTO> getAll();
}
