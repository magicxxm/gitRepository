package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.StowThreshold;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StowThresholdRepository extends BaseRepository<StowThreshold, String> {

    @Query(" select a from StowThreshold a " +
            " where a.warehouseId = :warehouse and a.clientId = :client and a.name = :name")
    StowThreshold getByName(@Param("warehouse") String warehouse,
                               @Param("client") String client,
                               @Param("name") String name);

    @Query(" select w from StowThreshold w " +
            " where w.entityLock = :entityLock " +
            " and not exists (" +
            " select 1 from User c " +
            " left join c.stowThresholds cw " +
            " where cw.id = w.id " +
            " and c.id = :userId) " +
            " order by w.name")
    List<StowThreshold> getUnassignedClientWarehouses(@Param("userId") String userId,
                                                         @Param("entityLock") Integer entityLock);
}
