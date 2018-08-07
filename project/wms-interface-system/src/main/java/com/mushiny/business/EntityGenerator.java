package com.mushiny.business;


import com.mushiny.common.entity.BaseEntity;

public interface EntityGenerator {

    <T extends BaseEntity> T generateEntity(Class<T> clazz);
}
