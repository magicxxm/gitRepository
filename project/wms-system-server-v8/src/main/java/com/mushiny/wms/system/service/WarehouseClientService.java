package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.ClientDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;

import java.util.List;

public interface WarehouseClientService {

    void createWarehouseClients(String warehouseId, List<String> clientIds);

    void createClientWarehouses(String clientId, List<String> warehouseIds);

    List<WarehouseDTO> getWarehouseList();

    List<ClientDTO> getClientList();

    List<WarehouseDTO> getAssignedWarehouseByClientId(String clientId);

    List<WarehouseDTO> getUnassignedWarehouseByClientId(String clientId);

    List<ClientDTO> getAssignedClientByWarehouseId(String warehouseId);

    List<ClientDTO> getUnassignedClientByWarehouseId(String warehouseId);
}
