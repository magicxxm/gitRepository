package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.CustomerShipmentPosition;
import com.mushiny.wms.schedule.domin.PickStation;
import com.mushiny.wms.schedule.domin.PickingOrderPosition;
import com.mushiny.wms.schedule.domin.Pod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickingOrderPositionRepository extends BaseRepository<PickingOrderPosition,String> {

    @Query("select po from PickingOrderPosition po where po.pod=:pod " +
            "and po.pickStation=:pickStation " +
            "and po.state<600 and po.pickFromLocationName like :podname")
    List<PickingOrderPosition> getFirstByPodAndPickStation(@Param("pod") Pod pod, @Param("pickStation") PickStation pickStation, @Param("podname") String podName);

    @Query("select po from PickingOrderPosition po where po.customerShipmentPosition=:customerShipmentPosition")
    List<PickingOrderPosition> getAllByCustomerShipmentPosition(@Param("customerShipmentPosition") CustomerShipmentPosition customerShipmentPosition);

}
