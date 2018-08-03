package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellTypeDTO;

import java.util.List;

public interface ReBinCellTypeService extends BaseService<ReBinCellTypeDTO> {

    List<ReBinCellTypeDTO> getAll();
}
