package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.PickStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/27.
 */
public interface PickStationRepository extends BaseRepository<PickStation,String> {

    @Query("select p from PickStation p where p.name = :name")
    PickStation getByName(@Param("name")String name);
}
