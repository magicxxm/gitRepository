package com.mushiny.wms.system.service;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RoleDTO;

import java.util.List;

public interface RoleModuleService {

    void createModuleRoles(String moduleId, List<String> roleIds);

    void createRoleModules(String roleId, List<String> moduleIds);

    List<RoleDTO> getRoleList();

    List<ModuleDTO> getModuleList();

    List<RoleDTO> getAssignedRoleByModuleId(String moduleId);

    List<RoleDTO> getUnassignedRoleByModuleId(String moduleId);

    List<ModuleDTO> getAssignedModuleByRoleId(String roleId);

    List<ModuleDTO> getUnassignedModuleByRoleId(String roleId);

}
