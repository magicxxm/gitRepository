package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.LabelControllerDTO;

import java.util.List;

public interface LabelControllerService extends BaseService<LabelControllerDTO> {

    void createMore(LabelControllerDTO dto);

    List<LabelControllerDTO> getAll();

}
