package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.dto.GoodsOutDoorDTO;

import java.util.List;

public interface GoodsOutDoorService extends BaseService<GoodsOutDoorDTO> {

    List<GoodsOutDoorDTO> getAll();
}
