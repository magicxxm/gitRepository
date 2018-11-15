package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.RcsTrip;
import com.mushiny.wms.schedule.domin.StowStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StowStationRepository extends BaseRepository<StowStation,String> {

    @Query("select s from StowStation s where s.name=:name")
    StowStation getByName(@Param("name") String name);

    @Query(" select s from RcsTrip s where s.workStationId.id =:workStationId")
    List<RcsTrip> getRcsTrip(@Param("workStationId") String workStationId);
}
