package com.mushiny.wms.masterdata.obbasics.common.business.impl;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.business.EntityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EntityGeneratorImpl implements EntityGenerator {

    private final Logger log = LoggerFactory.getLogger(EntityGeneratorImpl.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> T generateEntity(Class<T> clazz) {
        T entity = null;

        try {
            entity = clazz.newInstance();
        } catch (Exception ex) {
            log.error("Cannot get instance!", ex);
        }

        return entity;
    }
}
