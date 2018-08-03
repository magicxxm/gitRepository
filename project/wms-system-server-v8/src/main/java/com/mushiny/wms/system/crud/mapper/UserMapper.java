package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.repository.ClientRepository;
import com.mushiny.wms.system.repository.UserGroupRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements BaseMapper<UserDTO, User> {

    private final UserGroupRepository userGroupRepository;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final ClientMapper clientMapper;
    private final UserGroupMapper userGroupMapper;

    @Autowired
    public UserMapper(UserGroupMapper userGroupMapper,
                      WarehouseMapper warehouseMapper,
                      UserGroupRepository userGroupRepository,
                      WarehouseRepository warehouseRepository,
                      ClientRepository clientRepository,
                      ClientMapper clientMapper) {
        this.userGroupMapper = userGroupMapper;
        this.warehouseMapper = warehouseMapper;
        this.userGroupRepository = userGroupRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        UserDTO dto = new UserDTO(entity);

        dto.setUsername(entity.getUsername());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setLocale(entity.getLocale());
        dto.setAvatar(entity.getAvatar());

        dto.setUserGroup(userGroupMapper.toDTO(entity.getUserGroup()));
        dto.setClient(clientMapper.toDTO(entity.getClient()));
        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouse()));

        return dto;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User entity = new User();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setLocale(dto.getLocale());
        entity.setAvatar(dto.getAvatar());

        entity.setClient(clientRepository.retrieve(dto.getClientId()));
        entity.setWarehouse(warehouseRepository.retrieve(dto.getWarehouseId()));
        if (dto.getUserGroupId() != null) {
            entity.setUserGroup(userGroupRepository.retrieve(dto.getUserGroupId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(UserDTO dto, User entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setLocale(dto.getLocale());
        entity.setAvatar(dto.getAvatar());

        entity.setClient(clientRepository.retrieve(dto.getClientId()));
        entity.setWarehouse(warehouseRepository.retrieve(dto.getWarehouseId()));
        if (dto.getUserGroupId() != null) {
            entity.setUserGroup(userGroupRepository.retrieve(dto.getUserGroupId()));
        } else {
            entity.setUserGroup(null);
        }
    }
}
