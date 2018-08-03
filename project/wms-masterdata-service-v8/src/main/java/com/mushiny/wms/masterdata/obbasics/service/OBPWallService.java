package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPWallDTO;

import java.util.List;

public interface OBPWallService extends BaseService<OBPWallDTO> {

    void createMore(OBPWallDTO dto);

    List<OBPWallDTO> getAll();
}
