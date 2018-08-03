package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SizeFilterRuleDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
public interface SizeFilterRuleService extends BaseService<SizeFilterRuleDTO> {
    List<SizeFilterRuleDTO> getAll();
}
