package com.mushiny.wms.schedule.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.PickStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PickStationRepository extends BaseRepository<PickStation,String> {

  @Query("select p from PickStation p where p.name=:name")
  PickStation getByName(@Param("name") String name);
}
