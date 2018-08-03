package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DigitalLabelDTO;

import java.util.List;

public interface DigitalLabelService extends BaseService<DigitalLabelDTO> {

    List<DigitalLabelDTO> getAll();

    List<DigitalLabelDTO> getByLabel(List<String> ids);

}
