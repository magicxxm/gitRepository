package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWall;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReBinWallRepository extends BaseRepository<ReBinWall, String> {

    @Query("select r from ReBinWall r where r.warehouseId = :warehouse and r.name = :name")
    ReBinWall getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select r from ReBinWall r where r.reBinWallType.id = :reBinWallTypeId")
    List<ReBinWall> getByReBinWallTypeId(@Param("reBinWallTypeId") String reBinWallTypeId);
}
