package com.mushiny.wms.outboundproblem.repository.common;


import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.outboundproblem.domain.common.PickingUnitLoad;
import com.mushiny.wms.outboundproblem.domain.common.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickingUnitLoadRepository extends BaseRepository<PickingUnitLoad,String>{

    @Query("select p from PickingUnitLoad p where p.unitLoad=:unitLoad and p.warehouseId=:warehouseId")
    List<PickingUnitLoad> getByUnitLoad(@Param("warehouseId") String warehouseId, @Param("unitLoad") UnitLoad unitLoad);
}
