package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface WarehouseRepository extends BaseRepository<Warehouse,String> {

    @Query("select w from Warehouse w where w.warehouseNo = :warehouseNo")
    Warehouse getByWarehouseNo(@Param("warehouseNo")String warehouseNo);
}
