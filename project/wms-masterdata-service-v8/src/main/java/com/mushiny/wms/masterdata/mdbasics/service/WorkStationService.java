package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;

import java.util.List;

public interface WorkStationService extends BaseService<WorkStationDTO> {

    List<WorkStationDTO> getAll();

    List<NodeDTO> getBySectionId(String sectionId);

    void exitWorkStation(String stationId);
}
