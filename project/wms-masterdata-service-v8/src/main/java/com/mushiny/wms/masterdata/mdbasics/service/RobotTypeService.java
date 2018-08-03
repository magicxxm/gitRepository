package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotTypeDTO;

import java.util.List;

public interface RobotTypeService extends BaseService<RobotTypeDTO> {

    List<RobotTypeDTO> getAll();

}
