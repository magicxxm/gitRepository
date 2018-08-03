package com.mushiny.wms.masterdata.ibbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBPStationTypeRepository extends BaseRepository<IBPStationType, String> {

    @Query("select p from IBPStationType p where p.warehouseId = :warehouse and p.name = :name")
    IBPStationType getByName(@Param("warehouse") String warehouse, @Param("name") String name);

}
