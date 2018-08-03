package com.mushiny.wms.masterdata.obbasics.repository.impl;

import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCategoryRuleRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class PickingCateGoryRuleRepositoryImpl implements PickingCategoryRuleRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryRuleRepositoryImpl.class);

    private final EntityManager manager;

    public PickingCateGoryRuleRepositoryImpl(JpaContext context) {
        this.manager = context.getEntityManagerByManagedType(PickingCateGoryRule.class);
    }

    @Override
    public PickingCateGoryRule getByName(String name) {
        Query query = manager.createQuery("SELECT pc FROM "
                + PickingCateGoryRule.class.getSimpleName()
                + " pc "
                + "WHERE pc.name = :name ");
        query.setParameter("name", name);
        try {
            return (PickingCateGoryRule) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public boolean existsByName(String name) {
        manager.flush();
        Query query = manager.createQuery("SELECT pc.id FROM "
                + PickingCateGoryRule.class.getSimpleName()
                + " pc "
                + "WHERE pc.name = :name ");
        query.setParameter("name", name);
        try {
            query.getSingleResult();
        } catch (NoResultException nre) {
            return false;
        }
        return true;
    }
}
