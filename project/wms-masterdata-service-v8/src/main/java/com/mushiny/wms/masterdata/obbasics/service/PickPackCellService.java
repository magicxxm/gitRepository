package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;

public interface PickPackCellService extends BaseService<PickPackCellDTO> {

    void updateMore(PickPackWallDTO dto);
}
