package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.HardWareDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;

import java.util.List;

public interface HardWareWorkStationService {

    void createWorkStationHardWares(String workStationId, List<String> hardwares);

    List<WorkStationDTO> getWorkStationList();

    List<HardWareDTO> getAssignedByWorkStation(String userId);

    List<HardWareDTO> getUnassignedByWorkStation(String userId);

}
