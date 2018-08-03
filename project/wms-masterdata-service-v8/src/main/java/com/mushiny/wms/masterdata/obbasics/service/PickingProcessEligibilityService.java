package com.mushiny.wms.masterdata.obbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.general.crud.dto.UserGroupDTO;
import com.mushiny.wms.masterdata.general.domain.UserGroup;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;

import java.util.List;

public interface PickingProcessEligibilityService {

    void createPickingAreaEligibility(String processPathId, List<String> userIds);
    void savePPToUser(String userId, List<String> processPathIds);
    List<UserDTO> getAssignedUserByProcessPathId(String processPathId);

    List<UserDTO> getUnassignedUserByProcessPathId(String processPathId);
    List<ProcessPathDTO> getAssignedPPByUserId(String userId);

    List<ProcessPathDTO> getUnassignedPPByUserId(String userId);

    List<UserGroupDTO> getUserGroup();
    List<UserDTO> getUserByUserGroupId(String userGroupId);
}
