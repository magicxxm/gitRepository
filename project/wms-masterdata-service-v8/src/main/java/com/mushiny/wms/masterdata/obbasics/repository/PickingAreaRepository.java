package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickingAreaRepository extends BaseRepository<PickingArea, String> {

    @Query("select p from PickingArea p " +
            " where p.warehouseId = :warehouse and p.clientId = :client and p.name = :name")
    PickingArea getByName(@Param("warehouse") String warehouse,
                          @Param("client") String client,
                          @Param("name") String name);

    @Query("select p from PickingArea p where p.warehouseId = :warehouse ")
    List<PickingArea> getByWarehouse(@Param("warehouse") Warehouse warehouse);


    @Query("select p from PickingArea p " +
            "where p.clientId = :clientId " +
            "and p.entityLock=:entityLock ")
    List<PickingArea> getPickingArea(@Param("clientId") String clientId,
                                     @Param("entityLock") Integer entityLock);

    @Query(" select p from PickingArea p " +
            " where p.entityLock = :entityLock " +
            " and p.clientId=:clientId" +
            " and not exists (" +
            " select 1 from p.users u " +
            " where u.warehouse.id= :warehouseId " +
            " and u.id = :userId )" +
            " order by p.name")
    List<PickingArea> getUnassignedUsersPickingArea(@Param("clientId") String clientId,
                                                    @Param("warehouseId") String warehouseId,
                                                    @Param("userId") String userId,
                                                    @Param("entityLock") Integer entityLock);
}
