package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.UserWarehouseRole;
import com.mushiny.wms.system.domain.UserWarehouseRolePK;

public interface UserWarehouseRoleRepository extends BaseRepository<UserWarehouseRole, UserWarehouseRolePK> {

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

    void deleteByWarehouseId(String warehouseId);

    void deleteByWarehouseIdAndRoleId(String warehouseId, String roleId);

    void deleteByWarehouseIdAndUserId(String warehouseId, String userId);
}
