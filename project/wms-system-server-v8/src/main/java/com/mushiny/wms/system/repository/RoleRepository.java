package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, String> {

    Role findByName(String name);

    List<Role> findAllByOrderByName();

    @Query(" select r from Role r " +
            " where not exists (" +
            " select 1 from Module m " +
            " left join m.roles mr " +
            " where mr.id = r.id " +
            " and m.id = :moduleId)" +
            " order by r.name ")
    List<Role> getUnassignedModuleRoles(@Param("moduleId") String moduleId);

    @Query(" select r from Role r " +
            " where exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.roleId = r.id " +
            " and uwr.warehouseId = :warehouseId " +
            " and uwr.userId = :userId)" +
            " and r.name <> 'SuperAdmin'" +
            " order by r.name")
    List<Role> getByWarehouseIdAndUserId(@Param("warehouseId") String warehouseId,
                                         @Param("userId") String userId);

    @Query(" select r from Role r " +
            " where not exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.roleId = r.id " +
            " and uwr.warehouseId = :warehouseId " +
            " and uwr.userId = :userId)" +
            " and r.name <> 'SuperAdmin'" +
            " order by r.name")
    List<Role> getUnassignedUserWarehouseRoles(@Param("warehouseId") String warehouseId,
                                               @Param("userId") String userId);

    @Query(" select r from Role r " +
            " where exists (" +
            " select 1 from UserWarehouseRole cuwr " +
            " where cuwr.roleId = r.id " +
            " and cuwr.userId = :currentUserId " +
            " and cuwr.warehouseId = :warehouseId" +
            " and not exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.roleId = cuwr.roleId " +
            " and uwr.warehouseId = cuwr.warehouseId " +
            " and uwr.userId = :userId)" +
            " )" +
            " order by r.name ")
    List<Role> getUnassignedCurrentUserWarehouseRoles(@Param("currentUserId") String currentUserId,
                                                      @Param("warehouseId") String warehouseId,
                                                      @Param("userId") String userId);
}