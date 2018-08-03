package com.mushiny.wms.masterdata.general.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.general.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);
    @Query(" select u from User u " +
            " where u.warehouse.id=:warehouseId " +
            " and u.entityLock=:entityLock order by u.username")
    List<User> findByEntityLockOrderByUsername(@Param("entityLock") Integer entityLock,
                                               @Param("warehouseId") String warehouseId);

    @Query(" select u from User u " +
            " where u.userGroup.id=:userGroupId " +
            " and u.entityLock=:entityLock and " +
            "u.warehouse.id=:warehouseId order by u.username")
    List<User> findByUserGroupId(@Param("userGroupId") String userGroupId,
                                 @Param("entityLock") Integer entityLock,
                                 @Param("warehouseId") String warehouseId);
    @Query(" select u.username from User u " +
            " where u.id=:id")
   String findUseName(@Param("id") String id);

    @Query(" select u from User u " +
            " where u.entityLock = :entityLock " +
            " and exists (" +
            " select 1 from Warehouse w " +
            " left join w.users wu " +
            " where wu.id = u.id " +
            " and w.id = :warehouseId " +
            " and not exists (" +
            " select 1 from PickingArea p " +
            " left join p.users pu" +
            " where pu.id = wu.id " +
            " and p.id = :pickingAreaId )" +
            " )" +
            " order by u.username ")
    List<User> getUnassignedPickingAreaUsers(@Param("warehouseId") String warehouseId,
                                             @Param("pickingAreaId") String pickingAreaId,
                                             @Param("entityLock") Integer entityLock);

    @Query(" select u from User u " +
            " where u.entityLock = :entityLock " +
            " and exists (" +
            " select 1 from Warehouse w " +
            " left join w.users wu " +
            " where wu.id = u.id " +
            " and w.id = :warehouseId " +
            " and not exists (" +
            " select 1 from ProcessPath p " +
            " left join p.users pu" +
            " where pu.id = wu.id " +
            " and p.id = :processPathId )" +
            " )" +
            " order by u.username ")
    List<User> getUnassignedProcessPathUsers(@Param("warehouseId") String warehouseId,
                                             @Param("processPathId") String processPathId,
                                             @Param("entityLock") Integer entityLock);

}
