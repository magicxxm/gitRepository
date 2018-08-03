package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RfMenuDTO;
import com.mushiny.wms.system.crud.dto.RfMenuItemDTO;

import java.util.List;

public interface RfMenuService {

    void createAll(String parentId, List<RfMenuDTO> rfMenuDTOS);

    void updateMore(List<RfMenuDTO> rfMenuDTOS);

    List<RfMenuDTO> getByModuleId(String moduleId);

    List<RfMenuItemDTO> getMenuItem();

    List<ModuleDTO> getAssignedModuleByRoot();

    List<ModuleDTO> getAssignedModuleByParentId(String parentId);

    List<ModuleDTO> getUnassignedModuleByParentId(String parentId);
}
