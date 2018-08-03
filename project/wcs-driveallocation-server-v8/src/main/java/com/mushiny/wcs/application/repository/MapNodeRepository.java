package com.mushiny.wcs.application.repository;

import com.mushiny.wcs.application.domain.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapNodeRepository extends JpaRepository<MapNode, String> {

    @Query(" select n from MapNode n " +
            " where n.mapId = :mapId " +
            " and n.addressCodeId = :addressCodeId")
    MapNode getByAddressCodeId(@Param("mapId") String mapId,
                               @Param("addressCodeId") int addressCodeId);

    @Query(" select n1 from MapNode n1 " +
            "where n1.mapId=:mapId and n1.classGroup=:classGroupId  and n1.classValue=2 and n1.id <>:mapNodeId"
    )
    List<MapNode> getOutMapNodeByMapId(@Param("mapId") String mapId, @Param("classGroupId") String classGroupId, @Param("mapNodeId") String mapNodeId);

    @Query(" select n1 from MapNode n1 " +
            "where n1.mapId=:mapId and n1.classGroup=:classGroupId and n1.classValue=1 and n1.id <>:mapNodeId"
    )
    List<MapNode> getInMapNodeByMapId(@Param("mapId") String mapId, @Param("classGroupId") String classGroupId, @Param("mapNodeId") String mapNodeId);

    @Query(" select n from MapNode n " +
            " where n.blocked = false " +
            " and n.mapId = :mapId " +
            " and n.type = 7 and n.addressCodeId not in (select r.addressCodeId from  WCSRobot r)"  )
    List<MapNode> getByMapId(@Param("mapId") String mapId);
}
