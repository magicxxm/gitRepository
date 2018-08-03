package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPCellRepository extends BaseRepository<OBPCell, String> {

    @Query("select b from OBPCell b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    OBPCell getByName(@Param("warehouse") String warehouse,
                      @Param("name") String name);

}
