package com.mushiny.wms.common.context;

import com.mushiny.wms.common.Constant;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class ApplicationContext {

    @PersistenceContext
    private EntityManager entityManager;

    public String getErrorMessage(String key) {
        String locale = Constant.DEFAULT_LOCALE;

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
}
