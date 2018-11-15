package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.PackingStation;

/**
 * Created by 123 on 2017/5/16.
 */
public interface PackingStationRepository extends BaseRepository<PackingStation,String> {

    @Query("select ps from PackingStation ps where ps.name = :stationName and ps.warehouseId = :warehouseId")
    PackingStation getByName(@Param("stationName") String stationName, @Param("warehouseId") String warehouseId);
}
