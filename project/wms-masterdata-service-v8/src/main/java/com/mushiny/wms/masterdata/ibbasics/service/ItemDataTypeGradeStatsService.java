package com.mushiny.wms.masterdata.ibbasics.service;


import com.mushiny.wms.masterdata.ibbasics.crud.dto.ItemDataTypeGradeStatsDTO;
import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.TimeConfigDTO;

import java.util.List;

public interface ItemDataTypeGradeStatsService extends BaseService<ItemDataTypeGradeStatsDTO> {

    List<ItemDataTypeGradeStatsDTO> getByClientId(String clientId);

    int getDayNumber();

    TimeConfigDTO getRefreshTime();

    TimeConfigDTO saveRefreshTime(String time);

    TimeConfigDTO saveDayNumber(int day);

}
