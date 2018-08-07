package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.ItemGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface ItemGroupRepository extends BaseRepository<ItemGroup,String> {

    @Query("select i from ItemGroup i where i.name = :name")
    ItemGroup getByName(@Param("name") String groupName);
}
