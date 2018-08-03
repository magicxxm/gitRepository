package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;

import java.util.List;

public interface ZoneItemGroupService {

    void createZoneItemGroups(String zoneId, List<String> itemGroupIds);

    List<ItemGroupDTO> getAssignedItemGroupByZoneId(String zoneId);

    List<ItemGroupDTO> getUnassignedItemGroupByZoneId(String zoneId);
}
