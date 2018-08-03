package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveThresholdMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveThresholdRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ReceiveEligibilityServiceImpl implements ReceiveEligibilityService {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final ReceiveThresholdMapper receiveThresholdMapper;
    private final UserMapper userMapper;
    private final ReceiveThresholdRepository receiveThresholdRepository;

    @Autowired
    public ReceiveEligibilityServiceImpl(ApplicationContext applicationContext, UserRepository userRepository,
                                         ReceiveThresholdMapper receiveThresholdMapper,
                                         UserMapper userMapper,
                                         ReceiveThresholdRepository receiveThresholdRepository) {
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.receiveThresholdMapper = receiveThresholdMapper;
        this.userMapper = userMapper;
        this.receiveThresholdRepository = receiveThresholdRepository;
    }

    @Override
    public void createClientWarehouses(String userId, List<String> thresholdIds) {
        User user = userRepository.retrieve(userId);
        if (thresholdIds == null || thresholdIds.isEmpty()) {
            user.setReceiveThresholds(null);
        } else {
            Set<ReceiveThreshold> receiveThresholds = new HashSet<>();
            for (String sholdId : thresholdIds) {
                ReceiveThreshold receiveThreshold = receiveThresholdRepository.retrieve(sholdId);
                receiveThresholds.add(receiveThreshold);
            }
            user.setReceiveThresholds(receiveThresholds);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUserList() {
        List<User> entities = userRepository.findByEntityLockOrderByUsername(Constant.NOT_LOCKED,applicationContext.getCurrentWarehouse());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<ReceiveThresholdDTO> getAssignedWarehouseByUserId(String userId) {
        User user = userRepository.retrieve(userId);
        List<ReceiveThreshold> entities = new ArrayList<>();
        entities.addAll(user.getReceiveThresholds());
        return receiveThresholdMapper.toDTOList(entities);
    }

    @Override
    public List<ReceiveThresholdDTO> getUnassignedWarehouseByUserId(String userId) {
        List<ReceiveThreshold> entities = receiveThresholdRepository.getUnassignedClientWarehouses(
                userId, Constant.NOT_LOCKED);
        return receiveThresholdMapper.toDTOList(entities);
    }
}