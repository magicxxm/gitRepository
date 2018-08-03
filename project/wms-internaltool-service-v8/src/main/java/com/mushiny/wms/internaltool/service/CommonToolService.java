package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.RcsTrip;
import com.mushiny.wms.internaltool.common.domain.Robot;
import com.mushiny.wms.internaltool.web.dto.RobotDTO;
import com.mushiny.wms.internaltool.web.dto.TripDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommonToolService {

    ItemData getByItemNo(String itemNo, String clientId, String warehouseId);

    List<TripDTO> getRcsTrip(String seek);

    List<RobotDTO> getLaveBattery(String robotId);

}
