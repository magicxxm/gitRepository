package com.mushiny.repository;

import com.mushiny.model.SystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class SystemPropertyRepositoryImpl implements SystemPropertyRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(SystemPropertyRepositoryImpl.class);

    private final EntityManager em;

    public SystemPropertyRepositoryImpl(JpaContext context) {
        this.em = context.getEntityManagerByManagedType(SystemProperty.class);
    }

    @Override
    public String getString(String warehouse, String key) {
        String value = null;
        Query query = em.createQuery("SELECT sp.value FROM " + SystemProperty.class.getSimpleName() + " sp " +
                "WHERE sp.key = :key AND sp.warehouseId = :warehouseId");
        query.setParameter("key", key);
        query.setParameter("warehouseId", warehouse);

        try {
            value = (String) query.getSingleResult();
            return value == null ? null : value.trim();
        } catch (NoResultException ne) {
        }

        return value == null ? null : value.trim();
    }

    @Override
    public boolean getBoolean(String warehouse, String key) {
        String valueS = getString(warehouse, key);
        if (valueS == null) {
            valueS = "";
        }
        valueS = valueS.toLowerCase();
        if ("1".equals(valueS)) {
            return true;
        } else if ("true".equals(valueS)) {
            return true;
        } else if ("yes".equals(valueS)) {
            return true;
        }

        return false;
    }

    @Override
    public long getLong(String warehouse, String key) {
        String valueS = getString(warehouse, key);
        if (valueS == null) {
            valueS = "";
        }

        long valueL = 0L;
        try {
            valueL = Long.valueOf(valueS);
        } catch (NumberFormatException e) {
        }

        return valueL;
    }
}
