package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarrierRepository extends BaseRepository<Carrier, String> {

    @Query("select b from Carrier b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    Carrier getByName(@Param("warehouse") String warehouse,
                      @Param("name") String name);

}
