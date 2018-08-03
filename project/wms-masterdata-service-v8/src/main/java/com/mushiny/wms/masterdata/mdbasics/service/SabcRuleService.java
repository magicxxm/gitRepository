package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SabcRuleDTO;

import java.util.List;

public interface SabcRuleService extends BaseService<SabcRuleDTO> {

    List<SabcRuleDTO> getByClientId(String clientId);

    List<SabcRuleDTO> getAll();
}
