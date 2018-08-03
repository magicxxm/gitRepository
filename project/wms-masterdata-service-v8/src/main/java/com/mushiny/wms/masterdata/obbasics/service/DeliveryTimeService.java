package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.LabelControllerDTO;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
public interface DeliveryTimeService extends BaseService<DeliveryTimeDTO>{
    List<DeliveryTimeDTO> getAll();
}
