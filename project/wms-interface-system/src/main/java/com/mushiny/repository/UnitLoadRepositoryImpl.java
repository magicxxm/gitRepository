package com.mushiny.repository;

import com.mushiny.model.UnitLoad;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2018/2/8.
 */
public class UnitLoadRepositoryImpl implements UnitLoadRepositoryCustom {

    private EntityManager manager;

    public UnitLoadRepositoryImpl(JpaContext context) {
        this.manager = context.getEntityManagerByManagedType(UnitLoad.class);
    }

    @Override
    public UnitLoad getByStorageLocation(String storageLocationName, String id) {

        return null;
    }
}
