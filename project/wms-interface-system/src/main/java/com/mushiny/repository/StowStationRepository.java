package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.StowStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/29.
 */
public interface StowStationRepository extends BaseRepository<StowStation,String> {

    @Query("select s from StowStation s where s.name = :name")
    StowStation getByNameAndWarehouseId(@Param("name")String name);
}
