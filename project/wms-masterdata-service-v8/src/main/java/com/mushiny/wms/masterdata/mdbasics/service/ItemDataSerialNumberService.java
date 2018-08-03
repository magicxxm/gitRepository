package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataSerialNumberDTO;

import java.util.List;

public interface ItemDataSerialNumberService extends BaseService<ItemDataSerialNumberDTO> {

    List<ItemDataSerialNumberDTO> getByClientId(String clientId);
}
