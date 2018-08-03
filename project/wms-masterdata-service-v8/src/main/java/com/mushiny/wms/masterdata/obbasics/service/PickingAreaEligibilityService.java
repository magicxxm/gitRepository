package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;

import java.util.List;

public interface PickingAreaEligibilityService {

    void createPickingAreaEligibility(String pickingAreaId, List<String> userIds);
    void saveAreaToUser(String userId, List<String> pickingArea);
    List<UserDTO> getAssignedUserByPickingAreaId(String pickingAreaId);

    List<UserDTO> getUnassignedUserByPickingAreaId(String pickingAreaId);
    List<PickingAreaDTO> getAssignedAreaByUserId(String useId,String clientId);
    List<PickingAreaDTO> getUnassignedAreaByUserId(String useId,String clientId);

}
