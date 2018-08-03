package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.MapNeighbor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MapNeighborRepository extends JpaRepository<MapNeighbor, String> {

    @Query(" select n from MapNeighbor n " +
            " left join n.inNode ind " +
            " left join n.outNode ond" +
            " where n.blocked = false " +
            " and n.mapId = :mapId" +
            " and ind.blocked = false " +
            " and ond.blocked = false ")
    List<MapNeighbor> getByMapId(@Param("mapId") String mapId);
}
