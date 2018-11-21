package com.mushiny.auth.service.impl;

import com.mushiny.auth.domain.User;
import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.repository.UserRepository;
import com.mushiny.auth.repository.WarehouseRepository;
import com.mushiny.auth.security.SecurityUtils;
import com.mushiny.auth.service.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class ContextServiceImpl implements ContextService {

    private final Logger log = LoggerFactory.getLogger(ContextServiceImpl.class);

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    protected static final String WAREHOUSE_HEADER = "Warehouse";

    @Inject
    private HttpServletRequest request;

    @Inject
    private UserRepository userRepository;

    @Inject
    private WarehouseRepository warehouseRepository;

    @Override
    public User getCallersUser() {
        return Optional.ofNullable(userRepository.getByUsername(SecurityUtils.getCurrentUsername()))
                .orElse(null);
    }

    @Override
    public String getCallerUsername() {
        return SecurityUtils.getCurrentUsername();
    }

    @Override
    public Warehouse getCallersWarehouse() {
        User user = getCallersUser();
        if (user != null) {
            return user.getWarehouse();
        }
        return null;
    }

    @Override
    public Warehouse getCurrentWarehouse() {
        String warehouseId = getWarehouseHeader();
        if (warehouseId != null) {
            return warehouseRepository.findOne(warehouseId);
        }
        return null;
    }

    protected String getWarehouseHeader() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(WAREHOUSE_HEADER);
    }
}
