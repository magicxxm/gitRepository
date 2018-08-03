package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends BaseRepository<Node, String> {

    @Query(" select n from Node n " +
            " where n.warehouseId = :warehouse and n.map.id=:id order by n.addressCodeId")
    List<Node> getByMapId(@Param("warehouse") String warehouse, @Param("id") String id);

    @Query(" select n from Node n " +
            " where n.warehouseId = :warehouse and n.addressCodeId=:addressCodeId and n.map.id=:id")
    Node getAddressCodeId(@Param("warehouse") String warehouse, @Param("addressCodeId") int addressCodeId,@Param("id") String id);

}
