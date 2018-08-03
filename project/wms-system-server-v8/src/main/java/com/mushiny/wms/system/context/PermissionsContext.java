package com.mushiny.wms.system.context;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.system.domain.*;
import com.mushiny.wms.system.repository.UserRepository;
import com.mushiny.wms.system.repository.UserWarehouseRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PermissionsContext {

    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;


    @Autowired
    public PermissionsContext(UserWarehouseRoleRepository userWarehouseRoleRepository,
                              ApplicationContext applicationContext,
                              UserRepository userRepository) {
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
    }

    public boolean isWarehouseSuperRole() {
        // 判断用户是否拥有此仓库的超级管理员客户
        UserWarehouseRolePK pk = new UserWarehouseRolePK();
        pk.setWarehouseId(applicationContext.getCurrentWarehouse());
        pk.setUserId(applicationContext.getCurrentUser());
        pk.setRoleId(Constant.SUPER_ROLE);
        UserWarehouseRole userWarehouseRole = userWarehouseRoleRepository.findOne(pk);
        return userWarehouseRole != null;
    }

    public User getCurrentUser() {
        return Optional
                .ofNullable(userRepository.findByUsername(SecurityUtils.getCurrentUsername()))
                .orElseThrow(() -> new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString()));
    }
}
