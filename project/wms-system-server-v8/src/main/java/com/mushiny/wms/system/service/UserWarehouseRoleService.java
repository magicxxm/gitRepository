package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.crud.dto.UserDTO;

import java.util.List;

public interface UserWarehouseRoleService {

    void createWarehouseUserRoles(String warehouseId, String userId, List<String> roleIds);

    void createWarehousesRoleUsers(String warehouseId, String roleId, List<String> userIds);

    List<UserDTO> getWarehouseUserList(String warehouseId);

    List<RoleDTO> getWarehouseRoleList(String warehouseId);

    List<UserDTO> getAssignedUserByWarehouseIdAndRoleId(String warehouseId, String roleId);

    List<UserDTO> getUnassignedUserByWarehouseIdAndRoleId(String warehouseId, String roleId);

    List<RoleDTO> getAssignedRoleByWarehouseIdAndUserId(String warehouseId, String userId);

    List<RoleDTO> getUnassignedRoleByWarehouseIdAndUserId(String warehouseId, String userId);
}
