package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathTypeDTO;

import java.util.List;

public interface ProcessPathTypeService extends BaseService<ProcessPathTypeDTO> {

    List<ProcessPathTypeDTO> getAll();
}
