package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import com.mushiny.wms.system.crud.mapper.UserMapper;
import com.mushiny.wms.system.crud.mapper.WarehouseMapper;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.domain.Warehouse;
import com.mushiny.wms.system.repository.UserRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import com.mushiny.wms.system.service.UserWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserWarehouseServiceImpl implements UserWarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WarehouseMapper warehouseMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserWarehouseServiceImpl(UserRepository userRepository,
                                    WarehouseRepository warehouseRepository,
                                    WarehouseMapper warehouseMapper,
                                    UserMapper userMapper) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.userMapper = userMapper;
    }


    @Override
    public void createUserWarehouses(String userId, List<String> warehouseIds) {
        User user = userRepository.retrieve(userId);
        if (warehouseIds != null && !warehouseIds.isEmpty()) {
            Set<Warehouse> warehouses = new HashSet<>();
            for (String warehouseId : warehouseIds) {
                Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
                warehouses.add(warehouse);
            }
            user.setWarehouses(warehouses);
        } else {
            user.setWarehouses(null);
        }
        userRepository.save(user);
    }

    @Override
    public void createWarehouseUsers(String warehouseId, List<String> userIds) {
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        if (userIds != null && !userIds.isEmpty()) {
            Set<User> users = new HashSet<>();
            for (String userId : userIds) {
                User user = userRepository.retrieve(userId);
                users.add(user);
            }
            warehouse.setUsers(users);
        } else {
            warehouse.setUsers(null);
        }
        warehouseRepository.save(warehouse);
    }

    @Override
    public List<WarehouseDTO> getWarehouseList() {
        List<Warehouse> entities = warehouseRepository.findByEntityLockOrderByName(Constant.NOT_LOCKED);
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getUserList() {
        List<User> entities = userRepository.findByEntityLockOrderByUsername(Constant.NOT_LOCKED);
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<WarehouseDTO> getAssignedWarehouseByUserId(String userId) {
        User user = userRepository.retrieve(userId);
        List<Warehouse> entities = new ArrayList<>();
        entities.addAll(user.getWarehouses());
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<WarehouseDTO> getUnassignedWarehouseByUserId(String userId) {
        List<Warehouse> entities = warehouseRepository.getUnassignedUserWarehouses(
                userId, Constant.NOT_LOCKED);
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getAssignedUserByWarehouseId(String warehouseId) {
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        List<User> entities = new ArrayList<>();
        entities.addAll(warehouse.getUsers());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getUnassignedUserByWarehouseId(String warehouseId) {
        List<User> entities = userRepository.getUnassignedWarehouseUsers(
                warehouseId, Constant.NOT_LOCKED);
        return userMapper.toDTOList(entities);
    }
}
