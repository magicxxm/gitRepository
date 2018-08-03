package com.mushiny.wms.masterdata.general.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.general.repository.UserGroupRepository;
import com.mushiny.wms.masterdata.general.repository.WarehouseRepository;
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
    public UserMapper(UserGroupRepository userGroupRepository, ClientRepository clientRepository, WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper, ClientMapper clientMapper, UserGroupMapper userGroupMapper) {
        this.userGroupRepository = userGroupRepository;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.clientMapper = clientMapper;
        this.userGroupMapper = userGroupMapper;
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
//        entity.setWarehouse(warehouseRepository.retrieve(dto.getWarehouseId()));
//        if (dto.getUserGroupId() != null) {
//            entity.setUserGroup(userGroupRepository.retrieve(dto.getUserGroupId()));
//        } else {
            entity.setUserGroup(null);
//        }
    }
}
