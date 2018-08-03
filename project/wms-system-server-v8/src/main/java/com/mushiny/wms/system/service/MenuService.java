package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.MenuDTO;
import com.mushiny.wms.system.crud.dto.MenuItemDTO;
import com.mushiny.wms.system.crud.dto.ModuleDTO;

import java.util.List;

public interface MenuService {

    void createAll(String parentId, List<MenuDTO> menuDTOs);

    void updateMore(List<MenuDTO> menuDTOs);

    List<MenuItemDTO> getMenuItem();

    List<ModuleDTO> getAssignedModuleByRoot();

    List<ModuleDTO> getAssignedModuleByParentId(String parentId);

    List<ModuleDTO> getUnassignedModuleByParentId(String parentId);
}