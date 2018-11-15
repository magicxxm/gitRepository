package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.PickingOrder;
import com.mushiny.wms.schedule.domin.PickingUnitLoad;
import com.mushiny.wms.schedule.domin.UnitLoad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickUnitLoadRepository extends BaseRepository<PickingUnitLoad,String> {

    @Query("select pu from PickingUnitLoad pu where pu.unitLoad=:unitLoad and pu.state<:state")
    PickingUnitLoad getByUnitLoadAndState(@Param("unitLoad") UnitLoad unitLoad,
                                          @Param("state") int state);
    //根据pickOrder获取pickingUnitLoad
    @Query("select pu from PickingUnitLoad pu where pu.pickingOrder=:pickingOrder ")
    List<PickingUnitLoad> getByPickingOrder(@Param("pickingOrder") PickingOrder pickingOrder);

}
