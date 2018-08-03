package com.mushiny.wms.masterdata.obbasics.repository.impl;

import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class PickingCateGoryRepositoryImpl implements PickingCateGoryRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryRepositoryImpl.class);

    private final EntityManager manager;

    public PickingCateGoryRepositoryImpl(JpaContext context) {
        this.manager = context.getEntityManagerByManagedType(PickingCateGory.class);
    }

    @Override
    public List<PickingCateGory> getList(Warehouse warehouse, Client client) {
        Query query = manager.createQuery("SELECT pc FROM "
                + PickingCateGory.class.getSimpleName()
                + " pc "
                + "WHERE pc.warehouse = :warehouse "
                + "AND pc.client = :client "
                + "ORDER BY pc.index");
        query.setParameter("warehouse", warehouse);
        query.setParameter("client", client);
        List<PickingCateGory> result = (List<PickingCateGory>) query.getResultList();
        return result;
    }

    @Override
    public List<PickingCateGory> getListByProcessPath(Warehouse warehouse, ProcessPath processPath) {
        Query query = manager.createQuery("SELECT pc FROM "
                + PickingCateGory.class.getSimpleName()
                + " pc "
                + "WHERE pc.warehouse = :warehouse "
                + "AND pc.processPath = :processPath");
        query.setParameter("warehouse", warehouse);
        query.setParameter("processPath", processPath);
        List<PickingCateGory> result = (List<PickingCateGory>) query.getResultList();
        return result;
    }

    @Override
    public PickingCateGory getByName(String warehouse, String client, String name) {
        Query query = manager.createQuery("SELECT pc FROM "
                + PickingCateGory.class.getSimpleName()
                + " pc "
                + "WHERE pc.name = :name "
                + "AND pc.warehouse = :warehouse "
                + "AND pc.client = :client");
        query.setParameter("name", name);
        query.setParameter("warehouse", warehouse);
        query.setParameter("client", client);
        try {
            return (PickingCateGory) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

//    @Override
//    public PickingCategory getByName(Warehouse warehouse, Client client, String name) {
//        Query query = manager.createQuery("SELECT pc FROM "
//                + PickingCategory.class.getSimpleName()
//                + " pc "
//                + "WHERE pc.name = :name "
//                + "AND pc.warehouse = :warehouse "
//                + "AND pc.client = :client");
//        query.setParameter("name", name);
//        query.setParameter("warehouse", warehouse);
//        query.setParameter("client", client);
//        try {
//            return (PickingCategory) query.getSingleResult();
//        } catch (NoResultException nre) {
//            return null;
//        }
//    }

    @Override
    public boolean existsByName(Warehouse warehouse, Client client, String name) {
        manager.flush();
        Query query = manager.createQuery("SELECT pc.id FROM "
                + PickingCateGory.class.getSimpleName()
                + " pc "
                + "WHERE pc.name = :name "
                + "AND pc.warehouse = :warehouse "
                + "AND pc.client = :client");
        query.setParameter("name", name);
        query.setParameter("warehouse", warehouse);
        query.setParameter("client", client);
        try {
            query.getSingleResult();
        } catch (NoResultException nre) {
            return false;
        }
        return true;
    }

    @Override
    public int getMaxIndex(String warehouseId, String clientId) {
        Query query = manager.createQuery("SELECT MAX(pc.index) FROM "
                + PickingCateGory.class.getSimpleName()
                + " pc "
                + "WHERE pc.warehouseId = :warehouseId "
                + "AND pc.clientId = :clientId");
        query.setParameter("warehouseId", warehouseId);
        query.setParameter("clientId", clientId);
        Integer result = (Integer) query.getSingleResult();
        if (result != null) {
            return result.intValue();
        } else {
            return 0;
        }
    }
}
