package com.mushiny.wms.masterdata.obbasics.common.business;


import com.mushiny.wms.common.entity.BaseEntity;

public interface EntityGenerator {

    <T extends BaseEntity> T generateEntity(Class<T> clazz);
}
