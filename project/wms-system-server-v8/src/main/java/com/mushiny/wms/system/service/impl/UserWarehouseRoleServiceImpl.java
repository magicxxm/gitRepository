package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.RoleDTO;
import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.crud.mapper.RoleMapper;
import com.mushiny.wms.system.crud.mapper.UserMapper;
import com.mushiny.wms.system.domain.Role;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.domain.UserWarehouseRole;
import com.mushiny.wms.system.domain.Warehouse;
import com.mushiny.wms.system.repository.RoleRepository;
import com.mushiny.wms.system.repository.UserRepository;
import com.mushiny.wms.system.repository.UserWarehouseRoleRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import com.mushiny.wms.system.service.UserWarehouseRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserWarehouseRoleServiceImpl implements UserWarehouseRoleService {

    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionsContext permissionsContext;
    private final ApplicationContext applicationContext;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Autowired
    public UserWarehouseRoleServiceImpl(UserWarehouseRoleRepository userWarehouseRoleRepository,
                                        WarehouseRepository warehouseRepository,
                                        UserRepository userRepository,
                                        PermissionsContext permissionsContext,
                                        ApplicationContext applicationContext,
                                        RoleRepository roleRepository,
                                        UserMapper userMapper,
                                        RoleMapper roleMapper) {
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.warehouseRepository = warehouseRepository;
        this.userRepository = userRepository;
        this.permissionsContext = permissionsContext;
        this.applicationContext = applicationContext;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public void createWarehouseUserRoles(String warehouseId, String userId, List<String> roleIds) {
        userWarehouseRoleRepository.deleteByWarehouseIdAndUserId(warehouseId, userId);
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        User user = userRepository.retrieve(userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserWarehouseRole> userWarehouseRoles = new ArrayList<>();
            for (String roleId : roleIds) {
                Role role = roleRepository.retrieve(roleId);
                UserWarehouseRole userWarehouseRole = new UserWarehouseRole();
                userWarehouseRole.setRoleId(role.getId());
                userWarehouseRole.setUserId(user.getId());
                userWarehouseRole.setWarehouseId(warehouse.getId());
                userWarehouseRoles.add(userWarehouseRole);
            }
            userWarehouseRoleRepository.save(userWarehouseRoles);
        }
    }

    @Override
    public void createWarehousesRoleUsers(String warehouseId, String roleId, List<String> userIds) {
        userWarehouseRoleRepository.deleteByWarehouseIdAndRoleId(warehouseId, roleId);
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        Role role = roleRepository.retrieve(roleId);
        if (userIds != null && !userIds.isEmpty()) {
            List<UserWarehouseRole> userWarehouseRoles = new ArrayList<>();
            for (String userId : userIds) {
                User user = userRepository.retrieve(userId);
                UserWarehouseRole userWarehouseRole = new UserWarehouseRole();
                userWarehouseRole.setRoleId(role.getId());
                userWarehouseRole.setUserId(user.getId());
                userWarehouseRole.setWarehouseId(warehouse.getId());
                userWarehouseRoles.add(userWarehouseRole);
            }
            userWarehouseRoleRepository.save(userWarehouseRoles);
        }
    }

    @Override
    public List<UserDTO> getWarehouseUserList(String warehouseId) {
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        List<User> entities = new ArrayList<>();
        entities.addAll(warehouse.getUsers());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<RoleDTO> getWarehouseRoleList(String warehouseId) {
        List<Role> entities = roleRepository.getByWarehouseIdAndUserId(
                warehouseId, applicationContext.getCurrentUser());
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<RoleDTO> getAssignedRoleByWarehouseIdAndUserId(String warehouseId, String userId) {
        List<Role> entities = roleRepository.getByWarehouseIdAndUserId(warehouseId, userId);
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<RoleDTO> getUnassignedRoleByWarehouseIdAndUserId(String warehouseId, String userId) {
        List<Role> entities;
        if (permissionsContext.isWarehouseSuperRole()) {
            entities = roleRepository.getUnassignedUserWarehouseRoles(warehouseId, userId);
        } else {
            entities = roleRepository.getUnassignedCurrentUserWarehouseRoles(
                    permissionsContext.getCurrentUser().getId(), warehouseId, userId);
        }
        return roleMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getAssignedUserByWarehouseIdAndRoleId(String warehouseId, String roleId) {
        List<User> entities = userRepository.getByWarehouseIdAndRoleId(warehouseId, roleId);
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getUnassignedUserByWarehouseIdAndRoleId(String warehouseId, String roleId) {
        List<User> entities = userRepository.getUnassignedWarehouseRoleUsers(
                warehouseId, roleId, Constant.NOT_LOCKED);
        return userMapper.toDTOList(entities);
    }
}
