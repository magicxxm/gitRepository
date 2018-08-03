package com.mushiny.wms.masterdata.obbasics.repository.impl;

import com.mushiny.wms.masterdata.obbasics.common.domain.Resource;
import com.mushiny.wms.masterdata.obbasics.repository.ResourceRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ResourceRepositoryImpl implements ResourceRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(ResourceRepositoryImpl.class);

    private final EntityManager manager;

    public ResourceRepositoryImpl(JpaContext context) {
        this.manager = context.getEntityManagerByManagedType(Resource.class);
    }

    @Override
    public Resource getByKey(String key, String locale) {
        if (key == null) {
            throw new NullPointerException("getByName: parameter == null");
        }

        if (locale == null) {
            locale = Resource.LOCALE_DEFAULT;
        }

        Query query = manager.createQuery("SELECT r FROM "
                + Resource.class.getSimpleName()
                + " r "
                + "WHERE r.resourceKey = :key "
                + "AND r.locale = :locale");

        query.setParameter("key", key);
        query.setParameter("locale", locale);

        try {
            Resource resource = (Resource) query.getSingleResult();
            return resource;
        } catch (NoResultException ex) {
            return null;
        }
    }
}
