package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryRuleDTO;

import java.util.List;

public interface ReceiveCategoryRuleService extends BaseService<ReceiveCategoryRuleDTO> {

    List<ReceiveCategoryRuleDTO> getAll();
}
