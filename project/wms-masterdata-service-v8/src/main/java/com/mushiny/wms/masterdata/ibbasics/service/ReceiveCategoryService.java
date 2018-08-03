package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryDTO;

import java.util.List;

public interface ReceiveCategoryService extends BaseService<ReceiveCategoryDTO> {

    List<ReceiveCategoryDTO> getByClientId(String clientId);
}
