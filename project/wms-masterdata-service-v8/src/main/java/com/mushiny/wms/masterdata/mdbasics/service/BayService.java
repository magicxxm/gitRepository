package com.mushiny.wms.masterdata.mdbasics.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.business.dto.BayStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BayDTO;

import java.util.List;

public interface BayService extends BaseService<BayDTO> {

    void createMore(BayStorageLocationsDTO dto);

    List<BayDTO> getByClientId(String clientId);
}
