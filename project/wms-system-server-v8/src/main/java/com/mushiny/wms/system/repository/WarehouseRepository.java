package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends BaseRepository<Warehouse, String> {

    Warehouse findByName(String name);

    Warehouse findByWarehouseNo(String warehouseNo);

    List<Warehouse> findByEntityLockOrderByName(Integer entityLock);

    @Query("select w.id from Warehouse w where  w.entityLock = :entityLock and w.name=:name")
    String findIdByName(@Param("entityLock") Integer entityLock,@Param("name")String name);

    @Query(" select w from Warehouse w " +
            " where w.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from User u " +
            " left join u.warehouses uw " +
            " where uw.id = w.id " +
            " and u.id = :userId) " +
            " order by w.name")
    List<Warehouse> getUnassignedUserWarehouses(@Param("userId") String userId,
                                                @Param("entityLock") Integer entityLock);

    @Query(" select w from Warehouse w " +
            " where w.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from Client c " +
            " left join c.warehouses cw " +
            " where cw.id = w.id " +
            " and c.id = :clientId) " +
            " order by w.name")
    List<Warehouse> getUnassignedClientWarehouses(@Param("clientId") String clientId,
                                                  @Param("entityLock") Integer entityLock);
}
