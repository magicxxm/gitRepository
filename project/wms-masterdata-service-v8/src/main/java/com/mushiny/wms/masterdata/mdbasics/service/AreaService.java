package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;

import java.util.List;

public interface AreaService extends BaseService<AreaDTO> {

    List<AreaDTO> getByClientId(String clientId);

    List<AreaDTO> getAll();
}
