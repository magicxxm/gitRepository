package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCellType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OBPCellTypeRepository extends BaseRepository<OBPCellType, String> {

    @Query("select b from OBPCellType b " +
            " where b.warehouseId = :warehouse and b.name = :name")
    OBPCellType getByName(@Param("warehouse") String warehouse,
                      @Param("name") String name);

}
