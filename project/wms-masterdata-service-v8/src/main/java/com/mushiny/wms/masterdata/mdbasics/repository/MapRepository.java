package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapRepository extends BaseRepository<Map, String> {

    @Modifying
    @Query(" update Map set active = false " +
            " where sectionId = :sectionId and warehouseId = :warehouseId")
    void updateActiveBySection(@Param("sectionId") String sectionId,
                               @Param("warehouseId") String warehouseId);

    @Query(" select a from Map a " +
            " where a.warehouseId = :warehouse and a.sectionId = :sectionId")
    List<Map> getBySectionId(@Param("warehouse") String warehouse,
                             @Param("sectionId") String sectionId);

    @Query(" select a from Map a " +
            " where a.warehouseId = :warehouse and a.sectionId = :sectionId and a.active=:active")
    List<Map> getMapBySectionId(@Param("warehouse") String warehouse,
                             @Param("sectionId") String sectionId,@Param("active") boolean active);

    @Query(" select a from Map a " +
            " where a.warehouseId = :warehouse  and a.name = :name")
    Map getByName(@Param("name") String name, @Param("warehouse") String warehouse);

}
