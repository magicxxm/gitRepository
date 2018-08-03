package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaDTO;

import java.util.List;

public interface TurnAreaService extends BaseService<TurnAreaDTO> {

    List<TurnAreaDTO> getAll();
}
