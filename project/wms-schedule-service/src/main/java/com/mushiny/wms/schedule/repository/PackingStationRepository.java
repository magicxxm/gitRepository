package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.PackingStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackingStationRepository extends BaseRepository<PackingStation,String> {
    @Query("select p from PackingStation p where p.name=:name")
    PackingStation getByName(@Param("name") String name);

}
