package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.dto.UserGroupDTO;
import com.mushiny.wms.masterdata.general.crud.mapper.UserGroupMapper;
import com.mushiny.wms.masterdata.general.domain.UserGroup;
import com.mushiny.wms.masterdata.general.repository.UserGroupRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ProcessPathMapper;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.repository.ProcessPathRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickingProcessEligibilityService;
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
public class PickingProcessEligibilityServiceImpl implements PickingProcessEligibilityService {
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;
    private final ProcessPathRepository processPathRepository;
    private final ProcessPathMapper processPathMapper;
    private final UserMapper userMapper;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupMapper userGroupMapper;

    @Autowired
    public PickingProcessEligibilityServiceImpl(ApplicationContext applicationContext, UserRepository userRepository,
                                                ProcessPathRepository processPathRepository,
                                                ProcessPathMapper processPathMapper,
                                                UserMapper userMapper,
                                                UserGroupRepository userGroupRepository,
                                                UserGroupMapper userGroupMapper) {
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.processPathRepository = processPathRepository;
        this.processPathMapper = processPathMapper;
        this.userMapper = userMapper;
        this.userGroupRepository = userGroupRepository;
        this.userGroupMapper = userGroupMapper;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void createPickingAreaEligibility(String processPathId, List<String> userIds) {
        ProcessPath processPath = processPathRepository.retrieve(processPathId);
        List<User> users = new ArrayList<>();
        if (userIds != null && !userIds.isEmpty()) {
            for (String userId : userIds) {
                User user = userRepository.retrieve(userId);
                users.add(user);
            }
            processPath.setUsers(users);
        } else {
            processPath.setUsers(null);
        }
        processPathRepository.save(processPath);
    }
    @SuppressWarnings("Duplicates")
    @Override
    public void savePPToUser(String userId, List<String> processPathIds) {
        User user = userRepository.retrieve(userId);
        List<ProcessPath> ProcessPathList = new ArrayList<>();
        if (processPathIds != null && !processPathIds.isEmpty()) {
            for (String processPathId : processPathIds) {
                ProcessPath processPath = processPathRepository.retrieve(processPathId);
                ProcessPathList.add(processPath);
            }
            user.setProcessPath(ProcessPathList);
        } else {
            user.setProcessPath(null);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAssignedUserByProcessPathId(String processPathId) {
        ProcessPath processPath = processPathRepository.retrieve(processPathId);
        List<User> entities = new ArrayList<>();
        entities.addAll(processPath.getUsers());
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<UserDTO> getUnassignedUserByProcessPathId(String processPathId) {
        ProcessPath processPath = processPathRepository.retrieve(processPathId);
        List<User> entities = userRepository.getUnassignedProcessPathUsers(
                processPath.getWarehouseId(), processPath.getId(), Constant.NOT_LOCKED);
        return userMapper.toDTOList(entities);
    }

    @Override
    public List<ProcessPathDTO> getAssignedPPByUserId(String userId) {
        List<ProcessPath> processPathList= processPathRepository.findByEntityLock(Constant.NOT_LOCKED);
        List<ProcessPath> processPathList1=new ArrayList<>();
        for(ProcessPath processPath:processPathList){
            List<User> userList=processPath.getUsers();
            for (User user:userList){
                if(userId.equals(user.getId())){
                    processPathList1.add(processPath);
                }
            }
        }
        return processPathMapper.toDTOList(processPathList1);

    }

    @Override
    public List<ProcessPathDTO> getUnassignedPPByUserId(String userId) {
        User user = userRepository.retrieve(userId);
        List<ProcessPath> processPath= processPathRepository.findByUserId(Constant.NOT_LOCKED,user.getWarehouse().getId(),userId);
        return processPathMapper.toDTOList(processPath);
    }

    @Override
    public List<UserGroupDTO> getUserGroup() {
        List<UserGroup> userGroup=userGroupRepository.getByEntityLock(Constant.NOT_LOCKED);
        return userGroupMapper.toDTOList(userGroup);
    }

    @Override
    public List<UserDTO> getUserByUserGroupId(String userGroupId) {
        List<User> user=userRepository.findByUserGroupId(userGroupId,Constant.NOT_LOCKED,applicationContext.getCurrentWarehouse());
        return userMapper.toDTOList(user);
    }
}
