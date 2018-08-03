package com.mushiny.wms.tot.general.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.general.crud.mapper.UserMapper;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.repository.UserRepository;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.general.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseRepository warehouseRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ApplicationContext applicationContext, WarehouseRepository warehouseRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.applicationContext = applicationContext;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public UserDTO create(UserDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public UserDTO update(UserDTO dto) {
        return null;
    }

    @Override
    public UserDTO retrieve(String id) {
        return null;
    }

    @Override
    public List<UserDTO> getBySearchTerm(String searchTerm, Sort sort) {
        return null;
    }

    @Override
    public Page<UserDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public User findByUsername(String username,String currentWarehouseId) {
//        String currentWarehouseId = applicationContext.getCurrentWarehouse();
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        Set<User> users = warehouse.getUsers();
        for (User user : users) {
            if(username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }
}
