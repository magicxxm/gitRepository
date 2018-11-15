package com.mushiny.wms.schedule.common;


import com.mushiny.wms.common.entity.BaseEntity;

public interface EntityGenerator {

    <T extends BaseEntity> T generateEntity(Class<T> clazz);
}
