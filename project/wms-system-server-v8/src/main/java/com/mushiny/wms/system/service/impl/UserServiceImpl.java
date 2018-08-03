package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.UserDTO;
import com.mushiny.wms.system.crud.mapper.UserMapper;
import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.importUitl.ReadExcelUser;
import com.mushiny.wms.system.repository.*;
import com.mushiny.wms.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final PermissionsContext permissionsContext;
    private final ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserWarehouseRoleRepository userWarehouseRoleRepository,
                           PermissionsContext permissionsContext,
                           ApplicationContext applicationContext,
                           UserMapper userMapper, WarehouseRepository warehouseRepository, ClientRepository clientRepository, UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.userGroupRepository = userGroupRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.permissionsContext = permissionsContext;
        this.applicationContext = applicationContext;
        this.userMapper = userMapper;
    }

    @Override
    public void changePassword(String password, String newPassword) {
        User user = permissionsContext.getCurrentUser();
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new ApiException(SystemException.EX_SYS_USER_PASSWORD_ERROR.toString());
        }

    }

    @Override
    public void resetPassword(String id, String password) {
        User user = userRepository.retrieve(id);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        return userMapper.toDTO(permissionsContext.getCurrentUser());
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelUser readExcel = new ReadExcelUser(warehouseRepository, clientRepository, userGroupRepository);
        //解析excel，获取上传的事件单
        List<UserDTO> userDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(userDTOList);
    }

    @Override
    public UserDTO create(UserDTO dto) {
        User entity = userMapper.toEntity(dto);
        checkUsername(entity.getUsername());
        String a = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        User user = userRepository.retrieve(id);
        // delete many to many
        userWarehouseRoleRepository.deleteByUserId(id);
        // update delete flag
        user.setWarehouses(null);
        user.setEntityLock(Constant.GOING_TO_DELETE);
        userRepository.save(user);
    }

    @Override
    public UserDTO update(UserDTO dto) {
        User entity = userRepository.retrieve(dto.getId());
        if (!entity.getUsername().equalsIgnoreCase(dto.getUsername())) {
            checkUsername(dto.getUsername());
        }
        userMapper.updateEntityFromDTO(dto, entity);
        if (dto.getPassword() != null && dto.getPassword() != "") {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userMapper.toDTO(userRepository.save(entity));
    }

    @Override
    public UserDTO retrieve(String id) {
        User entity = userRepository.retrieve(id);
        applicationContext.isCurrentClient(entity.getClient().getId());
        return userMapper.toDTO(entity);
    }

    @Override
    public List<UserDTO> getBySearchTerm(String searchTerm, Sort sort) {
        String defaultSearchTerm = null;
        if (!applicationContext.isSystemClient(applicationContext.getCurrentClient())) {
            defaultSearchTerm = "client.id==" + applicationContext.getCurrentClient();
        }
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        List<User> entities = userRepository.getBySearchTerm(searchTerm, sort);
        return userMapper.toDTOList(entities);
    }

    @Override
    public Page<UserDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        String defaultSearchTerm = null;
        if (!applicationContext.isSystemClient(applicationContext.getCurrentClient())) {
            defaultSearchTerm = "client.id==" + applicationContext.getCurrentClient();
        }
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, sort);
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, pageable.getSort());
        }
        Page<User> entities = userRepository.getBySearchTerm(searchTerm, pageable);
        return userMapper.toDTOPage(pageable, entities);
    }

    private void checkUsername(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            throw new ApiException(SystemException.EX_SYS_USER_NAME_UNIQUE.toString(), userName);
        }
    }

    public void createImport(List<UserDTO> userDtoList) {
        for (UserDTO userDto : userDtoList) {
            User entity = userMapper.toEntity(userDto);
            checkUsername(entity.getUsername());
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            userRepository.save(entity);
        }
    }

}
