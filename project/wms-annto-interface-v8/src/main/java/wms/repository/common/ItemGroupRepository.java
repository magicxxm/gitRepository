package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.ItemGroup;

import java.util.List;

public interface ItemGroupRepository extends BaseRepository<ItemGroup, String> {

    @Query(" select i from ItemGroup i where i.name =:name")
    ItemGroup getByName(@Param("name") String name);

    @Query(" select i from ItemGroup i where i.entityLock=:entityLock")
    List<ItemGroup> getAllItemGroup(@Param("entityLock") int entityLock);

}
