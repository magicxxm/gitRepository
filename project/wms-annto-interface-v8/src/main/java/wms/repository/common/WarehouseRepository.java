package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.Warehouse;

public interface WarehouseRepository extends BaseRepository<Warehouse, String> {

    @Query(" select s from Warehouse s " +
            " where s.warehouseNo = :warehouseNo")
    Warehouse getByWarehouseNo(@Param("warehouseNo") String warehouseNo);

}
