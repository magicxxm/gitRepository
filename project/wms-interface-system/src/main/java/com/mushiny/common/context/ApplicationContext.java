package com.mushiny.common.context;

import com.mushiny.common.Constant;
import com.mushiny.common.exception.ApiException;
import com.mushiny.common.exception.ExceptionEnum;
import com.mushiny.config.security.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

@Component
public class ApplicationContext {

    @PersistenceContext
    private EntityManager entityManager;

    public String getErrorMessage(String key) {
        String username = SecurityUtils.getCurrentUsername();
        String localeSql = "SELECT U.LOCALE FROM SYS_USER U WHERE U.USERNAME = '" + username + "'";
        Query query = entityManager.createNativeQuery(localeSql);
        String locale;
        try {
            Object result = query.getSingleResult();
            if (result != null) {
                locale = String.valueOf(result);
            } else {
                locale = Constant.DEFAULT_LOCALE;
            }
        } catch (NoResultException e) {
            locale = Constant.DEFAULT_LOCALE;
        }
        try {
            String messageSql = "SELECT R.RESOURCE_VALUE FROM SYS_RESOURCE R " +
                    " WHERE R.RESOURCE_KEY = '" + key + "' " +
                    " AND R.LOCALE = '" + locale + "'";
            Query messageQuery = entityManager.createNativeQuery(messageSql);
            Object message = messageQuery.getSingleResult();
            if (message != null) {
                return String.valueOf(message);
            }
        } catch (Exception e) {
            return "Unknown Error [" + key + "]!";
        }
        return "Unknown Error [" + key + "]!";
    }

    public String getCurrentWarehouse() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String warehouseId = request.getHeader(Constant.HEADER_WAREHOUSE_KEY);
        String userId = getCurrentUser();
        String sql = "SELECT COUNT(1) FROM SYS_USER_WAREHOUSE U " +
                " WHERE U.USER_ID = '" + userId + "' " +
                " AND U.WAREHOUSE_ID = '" + warehouseId + "'";
        Query query = entityManager.createNativeQuery(sql);
        try {
            Long result = Long.valueOf(query.getSingleResult().toString());
            if (result != null && result != 0) {
                return warehouseId;
            }
        } catch (NoResultException e) {
            throw new ApiException(ExceptionEnum.EX_NOT_FOUND_WAREHOUSE.toString());
        }
        throw new ApiException(ExceptionEnum.EX_NOT_FOUND_WAREHOUSE.toString());
    }

    public String getCurrentClient() {
        String username = SecurityUtils.getCurrentUsername();
        String sql = "SELECT U.CLIENT_ID FROM SYS_USER U WHERE U.USERNAME = '" + username + "'";
        Query query = entityManager.createNativeQuery(sql);
        try {
            Object result = query.getSingleResult();
            if (result != null) {
                return String.valueOf(result);
            }
        } catch (NoResultException e) {
            throw new ApiException(ExceptionEnum.EX_NOT_FOUND_CLIENT.toString());
        }
        throw new ApiException(ExceptionEnum.EX_NOT_FOUND_CLIENT.toString());
    }

    public String getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        String sql = "SELECT U.ID FROM SYS_USER U WHERE U.USERNAME = '" + username + "'";
        Query query = entityManager.createNativeQuery(sql);
        try {
            Object result = query.getSingleResult();
            if (result != null) {
                return String.valueOf(result);
            }
        } catch (NoResultException e) {
            throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());
        }
        throw new ApiException(ExceptionEnum.EX_USER_NOT_LOGIN.toString());
    }

    public void isCurrentWarehouse(String warehouseId) {
        // 判断用户所操作的仓库是否是选择的仓库
        String currentWarehouseId = getCurrentWarehouse();
        if (!currentWarehouseId.equals(warehouseId)) {
            throw new ApiException(ExceptionEnum.EX_NOT_CURRENT_WAREHOUSE.toString());
        }
    }

    public boolean isSystemClient(String clientId) {
        // 判断用户所操作的客户是否是系统客户
        return clientId.equals(Constant.SYSTEM_CLIENT);
    }

    public void isCurrentClient(String clientId) {
        // 判断用户所操作的客户是否属于当前仓库的客户
        String currentClientId = getCurrentClient();
        if (currentClientId.equals(clientId)) {
            return;
        }
        if (currentClientId.equals(Constant.SYSTEM_CLIENT)) {
            String currentWarehouseId = getCurrentWarehouse();
            String sql = "SELECT COUNT(1) FROM SYS_WAREHOUSE_CLIENT W " +
                    " WHERE W.WAREHOUSE_ID = '" + currentWarehouseId + "' " +
                    " AND W.CLIENT_ID = '" + currentClientId + "' ";
            Query query = entityManager.createNativeQuery(sql);
            try {
                Long result = Long.valueOf(query.getSingleResult().toString());
                if (result != null && result != 0) {
                    return;
                }
            } catch (NoResultException e) {
                throw new ApiException(ExceptionEnum.EX_NOT_CURRENT_CLIENT.toString());
            }
        }
        throw new ApiException(ExceptionEnum.EX_NOT_CURRENT_CLIENT.toString());
    }
}
