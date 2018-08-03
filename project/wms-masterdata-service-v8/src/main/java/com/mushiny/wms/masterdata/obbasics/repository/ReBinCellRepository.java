package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCell;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReBinCellRepository extends BaseRepository<ReBinCell, String> {

    @Query("select r from ReBinCell r where r.warehouseId = :warehouse and r.name = :name")
    ReBinCell getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select b from ReBinCell b " +
            " where b.reBinWall.reBinWallType.id = :reBinWall order by b.name")
    List<ReBinCell> getByWallType(@Param("reBinWall") String reBinWall);
}
