package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemUnit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface ItemUnitRepository extends BaseRepository<ItemUnit,String> {

    @Query("select i from ItemUnit i where i.name = :name")
    ItemUnit getByName(@Param("name") String unitName);
}
