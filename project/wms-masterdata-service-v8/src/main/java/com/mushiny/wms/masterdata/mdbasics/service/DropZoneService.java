package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.DropZoneDTO;

import java.util.List;

public interface DropZoneService extends BaseService<DropZoneDTO> {

    List<DropZoneDTO> getAll();
}
