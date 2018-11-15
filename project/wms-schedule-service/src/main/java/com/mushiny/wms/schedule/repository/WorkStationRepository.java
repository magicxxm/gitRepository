package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.WorkStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkStationRepository extends BaseRepository<WorkStation,String> {

    @Query("select w from WorkStation w where w.name=:name")
    WorkStation getByName(@Param("name") String name);
}
