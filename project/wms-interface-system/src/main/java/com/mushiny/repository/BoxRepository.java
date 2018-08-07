package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.BoxType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface BoxRepository extends BaseRepository<BoxType,String> {

    @Query("select b from BoxType b where b.name = :name")
    BoxType getByName(@Param("name") String name);
}
