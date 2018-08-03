package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWall;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OBPWallRepository extends BaseRepository<OBPWall, String> {

    @Query("select r from OBPWall r where r.warehouseId = :warehouse and r.name = :name")
    OBPWall getByName(@Param("warehouse") String warehouse, @Param("name") String name);

    @Query("select r from OBPWall r where r.obpWallType.id = :reBinWallTypeId")
    List<OBPWall> getByOBPWallTypeId(@Param("reBinWallTypeId") String reBinWallTypeId);
}
