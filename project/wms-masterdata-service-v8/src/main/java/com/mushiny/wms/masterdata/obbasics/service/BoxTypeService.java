package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;

import java.util.List;

public interface BoxTypeService extends BaseService<BoxTypeDTO> {

    List<BoxTypeDTO> getByClientId(String clientId);
}
