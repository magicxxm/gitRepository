package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends BaseRepository<Client, String> {

    Client findByClientNo(String clientNo);

    Client findByName(String name);

    List<Client> findByEntityLockOrderByName(Integer entityLock);

    @Query("select c.id from Client c where  c.entityLock = :entityLock and c.name=:name")
    String findIdByName(@Param("entityLock") Integer entityLock,@Param("name")String name);

    @Query(" select c from Client c " +
            " where c.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from Warehouse w " +
            " left join w.clients wc " +
            " where wc.id = c.id " +
            " and w.id = :warehouseId) " +
            " order by c.name ")
    List<Client> getUnassignedWarehouseClients(@Param("warehouseId") String warehouseId,
                                               @Param("entityLock") Integer entityLock);
}
