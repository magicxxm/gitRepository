package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.ItemUnit;

public interface ItemUnitRepository extends BaseRepository<ItemUnit, String> {

    @Query(" select i from ItemUnit i where i.name =:name")
    ItemUnit getByName(@Param("name") String name);
}
