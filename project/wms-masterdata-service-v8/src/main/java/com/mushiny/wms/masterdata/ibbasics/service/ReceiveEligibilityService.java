package com.mushiny.wms.masterdata.ibbasics.service;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;

import java.util.List;

public interface ReceiveEligibilityService{

    void createClientWarehouses(String userId, List<String> thresholdIds);

    List<UserDTO> getUserList();

    List<ReceiveThresholdDTO> getAssignedWarehouseByUserId(String userId);

    List<ReceiveThresholdDTO> getUnassignedWarehouseByUserId(String userId);

}
