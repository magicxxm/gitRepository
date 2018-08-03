package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryPointDTO;

import java.util.List;

public interface CarrierService extends BaseService<CarrierDTO> {
    List<CarrierDTO> getAll();
}
