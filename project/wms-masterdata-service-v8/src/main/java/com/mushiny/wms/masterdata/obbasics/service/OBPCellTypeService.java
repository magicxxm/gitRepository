package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellTypeDTO;

import java.util.List;

public interface OBPCellTypeService extends BaseService<OBPCellTypeDTO> {

    List<OBPCellTypeDTO> getAll();

}
