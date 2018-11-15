package wms.service;


import wms.common.entity.BaseEntity;

public interface EntityGenerator {

    <T extends BaseEntity> T generateEntity(Class<T> clazz);
}
