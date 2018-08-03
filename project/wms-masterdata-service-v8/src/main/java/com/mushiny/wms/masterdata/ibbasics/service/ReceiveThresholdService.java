package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;

import java.util.List;

public interface ReceiveThresholdService extends BaseService<ReceiveThresholdDTO> {

    List<ReceiveThresholdDTO> getByClientId(String clientId);
}
