package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, String> {

    @Query(" select m from Map m " +
            " where m.entityLock = 0 " +
            " and m.active = true " +
            " and m.sectionId = :sectionId ")
    Map getBySectionId(@Param("sectionId") String sectionId);

    @Query(" select m from Map m " +
            " where m.entityLock = 0 " +
            " and m.active = true ")
    List<Map> findAllMap();

    @Query(" select m from Map m " +
            " where m.entityLock = 0 " +
            " and m.active = true " +
            " and m.sectionId = :sectionId" +
            " and m.warehouseId = :warehouseId")
    Map getBySectionIdAndWarehouseId(@Param("sectionId") String sectionId,
                                     @Param("warehouseId") String warehouseId);

    @Query(" select m from Map m " +
            " where m.entityLock = 0 " +
            " and m.active = true ")
    List<Map> getAllActiveMap();

}
