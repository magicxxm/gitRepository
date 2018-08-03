package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;

import java.util.List;

public interface PickPackWallService extends BaseService<PickPackWallDTO> {

    void createMore(PickPackWallDTO dto);

    List<PickPackWallDTO> getAll();

}
