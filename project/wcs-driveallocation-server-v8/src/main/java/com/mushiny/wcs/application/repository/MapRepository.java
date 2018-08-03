package com.mushiny.wcs.application.repository;

import com.mushiny.wcs.application.domain.Map;
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

}
