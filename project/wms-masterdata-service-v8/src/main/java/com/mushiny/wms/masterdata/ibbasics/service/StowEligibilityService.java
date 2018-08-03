package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;

import java.util.List;

public interface StowEligibilityService {

    void createClientWarehouses(String userId, List<String> thresholdIds);

    List<UserDTO> getUserList();

    List<StowThresholdDTO> getAssignedWarehouseByUserId(String userId);

    List<StowThresholdDTO> getUnassignedWarehouseByUserId(String userId);

}
