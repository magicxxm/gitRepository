package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellDTO;

import java.util.List;

public interface OBPCellService extends BaseService<OBPCellDTO> {

    List<OBPCellDTO> getAll();

}
