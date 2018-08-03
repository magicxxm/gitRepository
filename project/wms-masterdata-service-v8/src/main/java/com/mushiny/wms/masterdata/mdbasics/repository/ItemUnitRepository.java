package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemUnitRepository extends BaseRepository<ItemUnit, String> {

    @Query(" select i from ItemUnit i where i.name =:name")
    ItemUnit getByName(@Param("name") String name);

    @Query(" select i.id from ItemUnit i where i.name =:name")
    String getIdByName(@Param("name") String name);
}
