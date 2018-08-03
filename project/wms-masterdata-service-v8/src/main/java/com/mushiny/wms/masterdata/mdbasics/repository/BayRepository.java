package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Bay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BayRepository extends BaseRepository<Bay, String> {

    @Modifying
    @Query(" update Bay set bayIndex = bayIndex + :addIndex " +
            " where bayIndex >= :bayIndex and warehouseId = :warehouseId ")
    void updateCreateBayIndex(@Param("addIndex") int addIndex,
                              @Param("bayIndex") int bayIndex,
                              @Param("warehouseId") String warehouseId);
    @Modifying
    @Query(" update Bay set bayIndex = bayIndex - 1 " +
            " where bayIndex > :bayIndex and warehouseId = :warehouseId ")
    void updateDeleteBayIndex(@Param("bayIndex") int bayIndex,
                              @Param("warehouseId") String warehouseId);

    @Query(" select b from Bay b " +
            " where b.warehouseId = :warehouseId and b.name = :name")
    Bay getByName(@Param("warehouseId") String warehouseId,
                  @Param("name") String name);

    @Query(" select b from Bay b " +
            " where b.warehouseId = :warehouseId and b.bayIndex = (" +
            " select coalesce( max (mb.bayIndex), 0) from Bay mb where mb.warehouseId = b.warehouseId)")
    Bay getMaxIndexBay(@Param("warehouseId") String warehouseId);
}
