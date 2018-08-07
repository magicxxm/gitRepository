package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ChangePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by 123 on 2018/3/1.
 */
public interface ChangePositionRepository extends BaseRepository<ChangePosition,String> {

    @Query("select c from ChangePosition c where c.changeOrder = :orderNo and c.zitem = :lineNo")
    ChangePosition getByChangeOrderaAndAndZitem(@Param("orderNo")String order,@Param("lineNo")String lineNo);
}
