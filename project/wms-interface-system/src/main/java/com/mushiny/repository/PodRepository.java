package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.Pod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/27.
 */
public interface PodRepository extends BaseRepository<Pod,String> {

    @Query("select p from Pod p where p.name = :name")
    Pod getByName(@Param("name")String name);
}
