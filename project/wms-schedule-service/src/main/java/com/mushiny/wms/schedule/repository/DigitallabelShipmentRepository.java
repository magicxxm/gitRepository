package com.mushiny.wms.schedule.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.schedule.domin.DigitallabelShipment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DigitallabelShipmentRepository extends BaseRepository<DigitallabelShipment,String> {

    @Query("select d from DigitallabelShipment d where d.state=0")
    List<DigitallabelShipment> getByState();
}
