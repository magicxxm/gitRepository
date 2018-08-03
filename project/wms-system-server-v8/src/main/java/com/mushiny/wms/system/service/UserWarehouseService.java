package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;

import java.util.List;

public interface UserWarehouseService {

    void createWarehouseUsers(String warehouseId, List<String> userIds);

    void createUserWarehouses(String userId, List<String> warehouseIds);

    List<WarehouseDTO> getWarehouseList();

    List<UserDTO> getUserList();

    List<WarehouseDTO> getAssignedWarehouseByUserId(String userId);

    List<WarehouseDTO> getUnassignedWarehouseByUserId(String userId);

    List<UserDTO> getAssignedUserByWarehouseId(String warehouseId);

    List<UserDTO> getUnassignedUserByWarehouseId(String warehouseId);
}
