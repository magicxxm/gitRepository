package com.mushiny.wms.masterdata.obbasics.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryPointDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliverySortCodeDTO;

import java.util.List;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
public interface DeliverySortCodeService extends BaseService<DeliverySortCodeDTO> {
    List<DeliverySortCodeDTO> getAll();
}
