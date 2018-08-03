package com.mushiny.wms.masterdata.general.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.general.crud.dto.ClientDTO;
import com.mushiny.wms.masterdata.general.domain.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends BaseRepository<Client, String> {

    Client findByClientNo(String clientNo);

    @Query(" select c.id from Client c " +
            " where  c.name = :name and c.entityLock=:entityLock")
    String findIdByName(@Param("name") String name, @Param("entityLock") Integer entityLock);

    @Query(" select c from Client c " +
            " where c.entityLock=:entityLock")
    List<Client> findIdByEntityLock(@Param("entityLock") Integer entityLock);


    List<Client> findByEntityLockOrderByName(Integer entityLock);

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
