package com.mushiny.wms.tot.general.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.general.crud.dto.ClientDTO;

import java.util.List;

public interface ClientService extends BaseService<ClientDTO> {

    List<ClientDTO> getByCurrentWarehouseId();
    List<ClientDTO> getByWarehouseId(String warehouseId);
    List<ClientDTO> getClientByCurrentWarehouse();
}
