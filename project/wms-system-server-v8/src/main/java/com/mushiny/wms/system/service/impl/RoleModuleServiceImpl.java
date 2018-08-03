package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.crud.mapper.ModuleMapper;
import com.mushiny.wms.system.crud.mapper.RoleMapper;
import com.mushiny.wms.system.domain.Module;
import com.mushiny.wms.system.domain.Role;
import com.mushiny.wms.system.repository.ModuleRepository;
import com.mushiny.wms.system.repository.RoleRepository;
import com.mushiny.wms.system.service.RoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RoleModuleServiceImpl implements RoleModuleService {

    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final RoleMapper roleMapper;
    private final ModuleMapper moduleMapper;

    @Autowired
    public RoleModuleServiceImpl(ModuleRepository moduleRepository,
                                 RoleRepository roleRepository,
                                 RoleMapper roleMapper,
                                 ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public void createModuleRoles(String moduleId, List<String> roleIds) {
        Module module = moduleRepository.retrieve(moduleId);
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleId : roleIds) {
                Role role = roleRepository.retrieve(roleId);
                roles.add(role);
            }
            module.setRoles(roles);
        } else {
            module.setRoles(null);
        }
        moduleRepository.save(module);
    }

    @Override
    public void createRoleModules(String roleId, List<String> moduleIds) {
        Role role = roleRepository.retrieve(roleId);
        if (moduleIds != null && !moduleIds.isEmpty()) {
            Set<Module> modules = new HashSet<>();
            for (String moduleId : moduleIds) {
                Module module = moduleRepository.retrieve(moduleId);
                modules.add(module);
            }
            role.setModules(modules);
        } else {
            role.setModules(null);
        }
        roleRepository.save(role);
    }

    @Override
    public List<RoleDTO> getRoleList() {
        List<Role> entities = roleRepository.findAllByOrderByName();
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<ModuleDTO> getModuleList() {
        List<Module> entities = moduleRepository.findAllByOrderByName();
        return moduleMapper.toDTOList(entities);
    }

    @Override
    public List<RoleDTO> getAssignedRoleByModuleId(String moduleId) {
        Module module = moduleRepository.retrieve(moduleId);
        List<Role> entities = new ArrayList<>();
        entities.addAll(module.getRoles());
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<RoleDTO> getUnassignedRoleByModuleId(String moduleId) {
        List<Role> entities = roleRepository.getUnassignedModuleRoles(moduleId);
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<ModuleDTO> getAssignedModuleByRoleId(String roleId) {
        Role role = roleRepository.retrieve(roleId);
        List<Module> entities = new ArrayList<>();
        entities.addAll(role.getModules());
        return moduleMapper.toDTOList(entities);
    }

    @Override
    public List<ModuleDTO> getUnassignedModuleByRoleId(String roleId) {
        List<Module> entities = moduleRepository.getUnassignedRoleModules(roleId);
        return moduleMapper.toDTOList(entities);
    }
}
