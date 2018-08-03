package com.mushiny.wms.masterdata.mdbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemGroupRepository extends BaseRepository<ItemGroup, String> {

    @Query(" select i from ItemGroup i where i.name =:name")
    ItemGroup getByName(@Param("name") String name);

    @Query(" select i from ItemGroup i where i.entityLock=:entityLock")
    List<ItemGroup> getAllItemGroup(@Param("entityLock") int entityLock);

    @Query(" select i from ItemGroup i " +
            " where not exists ( " +
            " select 1 from Zone z " +
            " left join z.itemGroups zi where z.id = :zoneId and zi.id = i.id)" +
            " order by i.name ")
    List<ItemGroup> getUnassignedZoneItemGroups(@Param("zoneId") String zoneId);

    @Query(" select i.id from ItemGroup i where i.name =:name")
    String getIdByName(@Param("name") String name);
}
