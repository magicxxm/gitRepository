package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ZoneDTO;

import java.util.List;

public interface ZoneService extends BaseService<ZoneDTO> {

    List<ZoneDTO> getByClientId(String clientId);

    List<ZoneDTO> getByClientIdAndSectionId(String clientId,String sectionId);
}
