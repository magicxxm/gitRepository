package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.business.dto.PodStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodDTO;

import java.util.List;

public interface PodService extends BaseService<PodDTO> {

    void createMore(PodStorageLocationsDTO dto);

    List<PodDTO> getByClientId(String clientId);
    List<NodeDTO> getPlaceMark(String id);
}
