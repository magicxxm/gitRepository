package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallDTO;

import java.util.List;

public interface ReBinWallService extends BaseService<ReBinWallDTO> {

    void createMore(ReBinWallDTO dto);

    List<ReBinWallDTO> getAll();
}
