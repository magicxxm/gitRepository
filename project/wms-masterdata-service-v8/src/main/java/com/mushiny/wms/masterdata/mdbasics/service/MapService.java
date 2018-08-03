package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;

import java.util.List;

public interface MapService extends BaseService<MapDTO> {

    List<MapDTO> getAll();

//    List<MapDTO> getBySectionId(String sectionId);

}
