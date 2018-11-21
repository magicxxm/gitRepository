package com.mushiny.auth.repository.impl;

import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.repository.WarehouseRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class WarehouseRepositoryImpl implements WarehouseRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(WarehouseRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Warehouse getByNumber(String number) {
        Query query = entityManager.createQuery("SELECT w FROM "
                + Warehouse.class.getSimpleName()
                + " w "
                + "WHERE w.number = :number");

        query.setParameter("number", number);

        try {
            Warehouse warehouse = (Warehouse) query.getSingleResult();
            return warehouse;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Warehouse getByName(String name) {
        if (name == null) {
            throw new NullPointerException("getByName: parameter == null");
        }

        Query query = entityManager.createQuery("SELECT w FROM "
                + Warehouse.class.getSimpleName()
                + " w "
                + "WHERE w.name = :name");

        query.setParameter("name", name);

        try {
            Warehouse warehouse = (Warehouse) query.getSingleResult();
            return warehouse;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Warehouse getSystemWarehouse() {
        Warehouse warehouse;
        warehouse = entityManager.find(Warehouse.class, "0");
        if (warehouse == null) {
            log.error("no system warehouse configured - WMS may not operate properly at all - trying to create one");

            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO WAREHOUSE ")
                    .append("(ID, VERSION, ENTITY_LOCK, CREATED_DATE, MODIFIED_DATE, ")
                    .append("ADDITIONAL_CONTENT, NAME, WAREHOUSE_NO, EMAIL, PHONE, FAX)")
                    .append(" VALUES ")
                    .append("('0', 0, 0, ")
                    .append("'2016-12-31'")
                    .append(", ")
                    .append("'2016-12-31'")
                    .append(", '', 'System Warehouse', '0', '', '', '')");

            log.info("trying to insert the system warehouse: " + strBuf.toString());

            Query systemWarehouseQuery = entityManager.createNativeQuery(strBuf.toString());
            int entityCount = systemWarehouseQuery.executeUpdate();
            if (entityCount == 0) {
                log.error("failed to insert the system warehouse - error is unrecoverable");
                return null;
            }

            entityManager.flush();

            warehouse = entityManager.find(Warehouse.class, "0");
            if (warehouse == null) {
                log.error("failed to find the freshly inserted system warehouse - this is very strange!");
                return null;
            }
        }
        return warehouse;
    }
}
