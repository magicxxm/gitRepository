package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.PickPackWall;

/**
 * Created by 123 on 2017/4/28.
 */
public interface PickPackWallRepository extends BaseRepository<PickPackWall,String> {

    @Query("select pw from PickPackWall pw where pw.warehouseId=:warehouseId and pw.name=:name")
    PickPackWall getByName(@Param("warehouseId") String warehouseId, @Param("name") String name);
}
