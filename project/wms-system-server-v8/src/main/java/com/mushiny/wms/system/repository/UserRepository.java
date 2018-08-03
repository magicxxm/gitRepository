package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.domain.UserGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);

    List<User> findByUserGroup(UserGroup userGroup);

    List<User> findByEntityLockOrderByUsername(Integer entityLock);

    @Query(" select u from User u " +
            " where not exists (" +
            " select 1 from Warehouse w " +
            " left join w.users wu " +
            " where wu.id = u.id " +
            " and w.id = :warehouseId) " +
            " and u.entityLock = :entityLock " +
            " order by u.username ")
    List<User> getUnassignedWarehouseUsers(@Param("warehouseId") String warehouseId,
                                           @Param("entityLock") Integer entityLock);

    @Query(" select u from User u " +
            " where exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.userId = u.id " +
            " and uwr.warehouseId = :warehouseId " +
            " and uwr.roleId = :roleId) " +
            " order by u.username ")
    List<User> getByWarehouseIdAndRoleId(@Param("warehouseId") String warehouseId,
                                         @Param("roleId") String roleId);

    @Query(" select u from User u " +
            " where u.entityLock = :entityLock " +
            " and exists (" +
            " select 1 from Warehouse w " +
            " left join w.users wu " +
            " where wu.id = u.id " +
            " and w.id = :warehouseId " +
            " and not exists (" +
            " select 1 from UserWarehouseRole uwr " +
            " where uwr.userId = wu.id " +
            " and uwr.warehouseId = w.id " +
            " and uwr.roleId = :roleId)" +
            " )" +
            " order by u.username ")
    List<User> getUnassignedWarehouseRoleUsers(@Param("warehouseId") String warehouseId,
                                               @Param("roleId") String roleId,
                                               @Param("entityLock") Integer entityLock);
}
