package com.mushiny.wms.tot.general.context;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PermissionsContext {

    private final ApplicationContext applicationContext;
    private final UserRepository userRepository;


    @Autowired
    public PermissionsContext(ApplicationContext applicationContext,
                              UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
    }


    public User getCurrentUser() {
        return Optional
                .ofNullable(userRepository.findByUsername(SecurityUtils.getCurrentUsername()))
                .orElseThrow(() -> new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString()));
    }
}
