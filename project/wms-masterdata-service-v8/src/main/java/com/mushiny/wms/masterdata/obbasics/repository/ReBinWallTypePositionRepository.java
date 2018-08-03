package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallTypePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReBinWallTypePositionRepository extends BaseRepository<ReBinWallTypePosition, String> {


    @Query("select r from ReBinWallTypePosition r " +
            " where r.reBinWallType = :reBinWallType order by r.orderIndex")
    List<ReBinWallTypePosition> getByType(@Param("reBinWallType") ReBinWallType reBinWallType);

    @Query("select distinct r.reBinCellType from ReBinWallTypePosition r " +
            " where r.reBinWallType.id = :reBinWallType ")
    List<ReBinCellType> getByTypeId(@Param("reBinWallType") String reBinWallType);
}
