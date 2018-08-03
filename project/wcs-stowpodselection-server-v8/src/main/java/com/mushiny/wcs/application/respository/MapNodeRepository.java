package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MapNodeRepository extends JpaRepository<MapNode, String> {


    @Query(" select n from MapNode n " +
            " where n.blocked = false and n.type=1 and n.classGroup is not null and n.classValue=1 " +
            " and n.mapId = :mapId order by n.addressCodeId")
    List<MapNode> getInsideStorageNode(@Param("mapId") String mapId);

}
