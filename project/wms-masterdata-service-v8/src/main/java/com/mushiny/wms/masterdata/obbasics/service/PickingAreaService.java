package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;

import java.util.List;

public interface PickingAreaService extends BaseService<PickingAreaDTO> {

    List<PickingAreaDTO> getByClientId(String clientId);

}
