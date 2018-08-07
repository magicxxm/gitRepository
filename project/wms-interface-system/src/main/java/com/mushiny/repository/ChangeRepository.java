package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.Change;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/1.
 */
public interface ChangeRepository extends BaseRepository<Change,String> {

    @Query("select c from Change c where c.changeOrder = :changeNo")
    Change getByChangeOrder(@Param("changeNo")String changeNo);
}
