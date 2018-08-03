package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowThresholdMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StowThreshold;
import com.mushiny.wms.masterdata.ibbasics.repository.StowThresholdRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StowEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StowEligibilityServiceImpl implements StowEligibilityService {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final StowThresholdMapper stowThresholdMapper;
    private final UserMapper userMapper;
    private final StowThresholdRepository stowThresholdRepository;

    @Autowired
    public StowEligibilityServiceImpl(ApplicationContext applicationContext, UserRepository userRepository,
                                      StowThresholdMapper stowThresholdMapper,
                                      UserMapper userMapper,
                                      StowThresholdRepository stowThresholdRepository) {
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.stowThresholdMapper = stowThresholdMapper;
        this.userMapper = userMapper;
        this.stowThresholdRepository = stowThresholdRepository;
    }

    @Override
    public void createClientWarehouses(String userId, List<String> thresholdIds) {
        User user = userRepository.retrieve(userId);
        if (thresholdIds == null || thresholdIds.isEmpty()) {
            user.setStowThresholds(null);
        } else {
            Set<StowThreshold> stowThresholds = new HashSet<>();
            for (String sholdId : thresholdIds) {
                StowThreshold stowThreshold = stowThresholdRepository.retrieve(sholdId);
                stowThresholds.add(stowThreshold);
            }
            user.setStowThresholds(stowThresholds);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUserList() {
        List<User> entities = userRepository.findByEntityLockOrderByUsername(Constant.NOT_LOCKED,applicationContext.getCurrentWarehouse());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<StowThresholdDTO> getAssignedWarehouseByUserId(String userId) {
        User user = userRepository.retrieve(userId);
        List<StowThreshold> entities = new ArrayList<>();
        entities.addAll(user.getStowThresholds());
        return stowThresholdMapper.toDTOList(entities);
    }

    @Override
    public List<StowThresholdDTO> getUnassignedWarehouseByUserId(String userId) {
        List<StowThreshold> entities = stowThresholdRepository.getUnassignedClientWarehouses(
                userId, Constant.NOT_LOCKED);
        return stowThresholdMapper.toDTOList(entities);
    }
}