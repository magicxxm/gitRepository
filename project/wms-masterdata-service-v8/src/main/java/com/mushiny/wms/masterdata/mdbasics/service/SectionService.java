package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SectionDTO;

import java.util.List;

public interface SectionService extends BaseService<SectionDTO> {

    List<SectionDTO> getAll();

}
