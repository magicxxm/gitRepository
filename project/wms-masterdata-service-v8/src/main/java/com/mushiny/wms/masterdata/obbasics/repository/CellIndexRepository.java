package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.CellIndex;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CellIndexRepository extends BaseRepository<CellIndex, String> {

    @Query("select b from CellIndex b " +
            " where b.warehouseId = :warehouse and b.pickPackWallType1.id = :pickPackWallTypeId")
    List<CellIndex> getById(@Param("warehouse") String warehouse,
                            @Param("pickPackWallTypeId") String pickPackWallTypeId);

}
