package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, String> {

    @Query(" select m from Map m " +
            " where m.entityLock = 0 " +
            " and m.active = true " +
            " and m.sectionId = :sectionId" +
            " and m.warehouseId = :warehouseId")
    Map getBySectionIdAndWarehouseId(@Param("sectionId") String sectionId,
                                     @Param("warehouseId") String warehouseId);


}
