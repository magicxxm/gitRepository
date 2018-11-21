package com.mushiny.auth.service.mapper;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.service.dto.UserDTO;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserMapper implements BaseMapper<User, UserDTO> {

    @Inject
    private WarehouseMapper warehouseMapper;

    @Override
    public UserDTO mapEntityIntoDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO(user);

        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setLocale(user.getLocale());

        if (user.getWarehouses() != null) {
            userDTO.setWarehouses(warehouseMapper.mapEntitiesIntoDTOs(user.getWarehouses()));
        }
        return userDTO;
    }

    @Override
    public User mapDTOIntoEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        userDTO.merge(user);
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setLocale(userDTO.getLocale());
        return user;
    }

    @Override
    public void updateEntityFromDTO(UserDTO userDTO, User user) {
        userDTO.merge(user);
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setLocale(userDTO.getLocale());
    }
}
