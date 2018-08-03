package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.HardWare;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HardWareRepository extends BaseRepository<HardWare, String> {

    @Query(" select a from HardWare a " +
            " where a.warehouseId = :warehouse and a.name = :name")
    HardWare getByName(@Param("warehouse") String warehouse,
                   @Param("name") String name);

    @Query(" select w from HardWare w " +
            " where w.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from WorkStation c " +
            " left join c.hardWares cw " +
            " where cw.id = w.id " +
            " and c.id = :userId) " +
            " order by w.name")
    List<HardWare> getUnassignedClientWarehouses(@Param("userId") String userId,
                                                 @Param("entityLock") Integer entityLock);
}
