package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;

import java.util.List;

public interface StowThresholdService extends BaseService<StowThresholdDTO> {

    List<StowThresholdDTO> getByClientId(String clientId);
}
