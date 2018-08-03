package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReplenishStrategyDTO;

import java.util.List;

public interface ReplenishStrategyService extends BaseService<ReplenishStrategyDTO> {

    List<ReplenishStrategyDTO> getByClientId(String clientId);
}
