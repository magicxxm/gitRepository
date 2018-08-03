package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OrderStrategyDTO;

import java.util.List;

public interface OrderStrategyService extends BaseService<OrderStrategyDTO> {

    List<OrderStrategyDTO> getByClientId(String clientId);
}
