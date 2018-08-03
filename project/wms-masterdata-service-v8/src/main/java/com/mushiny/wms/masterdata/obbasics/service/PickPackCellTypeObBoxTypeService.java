package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellTypeDTO;

import java.util.List;

public interface PickPackCellTypeObBoxTypeService {

    void createPickPackCellTypeObBoxType(String pickingAreaId, List<String> userIds);

    List<PickPackCellTypeDTO> getList(String clientId);

    List<BoxTypeDTO> getAssignedUserByReBinCellTypeId(String pickingAreaId,String clientId);

    List<BoxTypeDTO> getUnassignedUserByReBinCellTypeId(String pickingAreaId,String clientId);

}
