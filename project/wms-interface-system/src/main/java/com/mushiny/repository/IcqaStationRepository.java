package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.IcqaStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/27.
 */
public interface IcqaStationRepository extends BaseRepository<IcqaStation,String> {

    @Query("select i from IcqaStation i where i.name = :name")
    IcqaStation getByName(@Param("name")String name);
}
