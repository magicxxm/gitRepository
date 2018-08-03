package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;

import java.util.List;

public interface ProcessPathService extends BaseService<ProcessPathDTO> {

    List<ProcessPathDTO> getAll();
}
