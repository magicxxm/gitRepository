package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessPathRepository extends BaseRepository<ProcessPath, String> {

    @Query("select p from ProcessPath p where p.name = :name and p.warehouseId = :warehouse")
    ProcessPath getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    List<ProcessPath> findByEntityLock(Integer entityLock);
    @Query(" select p from ProcessPath p " +
            " where p.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from p.users u " +
            " where u.warehouse.id= :warehouseId " +
            " and u.id = :userId )" +
            " order by p.name")
    List<ProcessPath> findByUserId(@Param("entityLock") Integer entityLock,
                                   @Param("warehouseId") String warehouseId,
                                   @Param("userId") String userId);
}
