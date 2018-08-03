package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.BatterConfig;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BatterConfigRepository extends BaseRepository<BatterConfig, String> {

    @Query(" select a from BatterConfig a " +
            " where a.name = :name")
    BatterConfig getByName(@Param("name") String name);
}
