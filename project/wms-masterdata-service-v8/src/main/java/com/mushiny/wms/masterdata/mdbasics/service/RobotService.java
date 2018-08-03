package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotLaveBatteryDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TripDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RobotService extends BaseService<RobotDTO> {

    void enter(String id, String password);

    List<RobotLaveBatteryDTO> getLaveBattery(String robotId);

    Page<TripDTO> getPageRcsTrip(String startTime, String endTime, List<String> type, boolean isFinish, String seek, boolean isExport, Pageable pageable);
}
