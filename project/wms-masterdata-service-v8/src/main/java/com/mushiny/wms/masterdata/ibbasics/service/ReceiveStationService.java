package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;

import java.util.List;

public interface ReceiveStationService extends BaseService<ReceiveStationDTO> {

    void createMore(ReceiveStationDTO dto);

    List<ReceiveStationDTO> getAll();
}
