package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaQueueDTO;

import java.util.List;

public interface TurnAreaQueueService extends BaseService<TurnAreaQueueDTO> {

    List<TurnAreaQueueDTO> getAll();
}
