package com.mushiny.wcs.application.respository;

import com.mushiny.wcs.application.domain.MapNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MapNodeRepository extends JpaRepository<MapNode, String> {


    @Query(" select n from MapNode n " +
            " where n.blocked = false " +
            " and n.mapId = :mapId order by n.addressCodeId")
    List<MapNode> getEmptyNode(@Param("mapId") String mapId);

    @Query(" select n from MapNode n " +
            " where n.blocked = false " +
            " and n.mapId = :mapId order by n.addressCodeId")
    List<MapNode> getHeavyNode(@Param("mapId") String mapId);

    @Query(" select n from MapNode n " +
            " where n.mapId = :mapId " +
            " and n.addressCodeId = :addressCodeId")
    MapNode getByMapIdAndAddressCodeId(@Param("mapId") String mapId,
                                       @Param("addressCodeId") int addressCodeId);
}
