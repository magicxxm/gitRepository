package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationTypeDTO;

import java.util.List;

public interface WorkStationTypeService extends BaseService<WorkStationTypeDTO> {

    List<WorkStationTypeDTO> getAll();
}
