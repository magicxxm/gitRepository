package com.mushiny.wms.masterdata.obbasics.repository.impl;

import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCategoryPositionRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class PickingCateGoryPositionRepositoryImpl implements PickingCategoryPositionRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryPositionRepositoryImpl.class);

    private final EntityManager manager;

    public PickingCateGoryPositionRepositoryImpl(JpaContext context) {
        this.manager = context.getEntityManagerByManagedType(PickingCateGoryPosition.class);
    }

    @Override
    public PickingCateGoryPosition getByNumber(String number) {
        Query query = manager.createQuery("SELECT pos FROM " + PickingCateGoryPosition.class.getSimpleName() + " pos WHERE pos.number = :number");
        query = query.setParameter("number", number);
        try {
            return (PickingCateGoryPosition) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Override
    public boolean existsByNumber(String number) {
        Query query = manager.createQuery("SELECT pos FROM " + PickingCateGoryPosition.class.getSimpleName() + " pos WHERE pos.number = :number");
        query = query.setParameter("number", number);
        try {
            query.getSingleResult();
        } catch (NoResultException nre) {
            return false;
        }
        return true;
    }

    @Override
    public PickingCateGoryPosition getById(String id) {

        Query query = manager.createQuery("SELECT pos.id FROM " + PickingCateGoryPosition.class.getSimpleName() + " pos WHERE pos.id = :id");
        query = query.setParameter("id", id);
        try {
            PickingCateGoryPosition position =(PickingCateGoryPosition) query.getSingleResult();

            return position;
        } catch (NoResultException nre) {
            return null;
        }
    }
}
