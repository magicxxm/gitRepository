package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.crud.dto.UserGroupDTO;
import com.mushiny.wms.system.crud.mapper.UserGroupMapper;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.domain.UserGroup;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.repository.UserGroupRepository;
import com.mushiny.wms.system.repository.UserRepository;
import com.mushiny.wms.system.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserGroupMapper userGroupMapper;

    @Autowired
    public UserGroupServiceImpl(UserGroupRepository userGroupRepository,
                                UserRepository userRepository,
                                UserGroupMapper userGroupMapper) {
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.userGroupMapper = userGroupMapper;
    }

    @Override
    public UserGroupDTO create(UserGroupDTO dto) {
        UserGroup entity = userGroupMapper.toEntity(dto);
        checkUserGroupName(entity.getName());
        return userGroupMapper.toDTO(userGroupRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        UserGroup entity = userGroupRepository.retrieve(id);
        List<User> users = userRepository.findByUserGroup(entity);
        if (users != null && users.size() > 0) {
            List<User> updateUsers = new ArrayList<>();
            for (User user : users) {
                user.setUserGroup(null);
                updateUsers.add(user);
            }
            userRepository.save(updateUsers);
        }
        userGroupRepository.delete(entity);
    }

    @Override
    public UserGroupDTO update(UserGroupDTO dto) {
        UserGroup entity = userGroupRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkUserGroupName(dto.getName());
        }
        userGroupMapper.updateEntityFromDTO(dto, entity);
        return userGroupMapper.toDTO(userGroupRepository.save(entity));
    }

    @Override
    public UserGroupDTO retrieve(String id) {
        return userGroupMapper.toDTO(userGroupRepository.retrieve(id));
    }

    @Override
    public List<UserGroupDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<UserGroup> entities = userGroupRepository.getBySearchTerm(searchTerm, sort);
        return userGroupMapper.toDTOList(entities);
    }

    @Override
    public Page<UserGroupDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(), sort);
        }
        Page<UserGroup> entities = userGroupRepository.getBySearchTerm(searchTerm, pageable);
        return userGroupMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<UserGroupDTO> getAll() {
        List<UserGroup> entities = userGroupRepository.findAllByOrderByName();
        return userGroupMapper.toDTOList(entities);
    }

    private void checkUserGroupName(String userGroupName) {
        UserGroup userGroup = userGroupRepository.findByName(userGroupName);
        if (userGroup != null) {
            throw new ApiException(SystemException.EX_SYS_USER_GROUP_NAME_UNIQUE.toString(), userGroupName);
        }
    }
}
