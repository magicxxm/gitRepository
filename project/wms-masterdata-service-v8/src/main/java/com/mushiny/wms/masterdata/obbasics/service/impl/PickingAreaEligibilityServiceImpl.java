package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickingAreaMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;
import com.mushiny.wms.masterdata.obbasics.repository.PickingAreaRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickingAreaEligibilityService;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PickingAreaEligibilityServiceImpl implements PickingAreaEligibilityService {

    private final UserRepository userRepository;
    private final PickingAreaRepository pickingAreaRepository;
    private final PickingAreaMapper pickingAreaMapper;
    private final UserMapper userMapper;

    @Autowired
    public PickingAreaEligibilityServiceImpl(UserRepository userRepository,
                                             PickingAreaRepository pickingAreaRepository,
                                             PickingAreaMapper pickingAreaMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.pickingAreaRepository = pickingAreaRepository;
        this.pickingAreaMapper = pickingAreaMapper;
        this.userMapper = userMapper;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void createPickingAreaEligibility(String pickingAreaId, List<String> userIds) {
        PickingArea pickingArea = pickingAreaRepository.retrieve(pickingAreaId);
        List<User> users = new ArrayList<>();
        if (userIds != null && !userIds.isEmpty()) {
            for (String userId : userIds) {
                User user = userRepository.retrieve(userId);
                users.add(user);
            }
            pickingArea.setUsers(users);
        } else {
            pickingArea.setUsers(null);
        }
        pickingAreaRepository.save(pickingArea);
    }
    @SuppressWarnings("Duplicates")
    @Override
    public void saveAreaToUser(String userId, List<String> pickingAreaIds) {
        User user=userRepository.retrieve(userId);
        List<PickingArea> pickingAreaList = new ArrayList<>();
        if (pickingAreaIds != null && !pickingAreaIds.isEmpty()) {
            for (String pickingAreaId : pickingAreaIds) {
                PickingArea pickingArea = pickingAreaRepository.retrieve(pickingAreaId);
                pickingAreaList.add(pickingArea);
            }
            user.setPickingArea(pickingAreaList);
        } else {
            user.setPickingArea(null);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAssignedUserByPickingAreaId(String pickingAreaId) {
        PickingArea pickingArea = pickingAreaRepository.retrieve(pickingAreaId);
        List<User> entities = new ArrayList<>();
        entities.addAll(pickingArea.getUsers());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getUnassignedUserByPickingAreaId(String pickingAreaId) {
        PickingArea pickingArea = pickingAreaRepository.retrieve(pickingAreaId);
        List<User> entities = userRepository.getUnassignedPickingAreaUsers(
                pickingArea.getWarehouseId(), pickingArea.getId(), Constant.NOT_LOCKED);
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<PickingAreaDTO> getAssignedAreaByUserId(String useId, String clientId) {
        User userEntities=userRepository.retrieve(useId);
        List<PickingArea> pickingAreaList = pickingAreaRepository.getPickingArea(clientId,Constant.NOT_LOCKED);
        List<PickingArea> pickingAreaList1 = new ArrayList<>();
        for (PickingArea pickingArea : pickingAreaList) {
            List<User> entities = pickingArea.getUsers();
            for (User user : entities) {
                if (user.getId().equals(useId)) {
                    pickingAreaList1.add(pickingArea);
                }
            }
        }
        return pickingAreaMapper.toDTOList(pickingAreaList1);
    }

    @Override
    public List<PickingAreaDTO> getUnassignedAreaByUserId(String useId, String clientId) {
        User user=userRepository.retrieve(useId);
        List<PickingArea> pickingAreaList =pickingAreaRepository.getUnassignedUsersPickingArea(clientId,user.getWarehouse().getId(),useId,Constant.NOT_LOCKED);
        return pickingAreaMapper.toDTOList(pickingAreaList);
    }
}
