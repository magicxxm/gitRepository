package com.mushiny.wms.common.crud.mapper;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper<D extends BaseDTO, T extends BaseEntity> {

    default Page<D> toDTOPage(Pageable pageable, Page<T> source) {
        List<D> dtoList = toDTOList(source.getContent());
        return new PageImpl<>(dtoList, pageable, source.getTotalElements());
    }

    default List<D> toDTOList(List<T> entityList) {
        if (entityList == null) {
            return null;
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    default List<T> toEntityList(List<D> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    D toDTO(T entity);

    T toEntity(D dto);

    void updateEntityFromDTO(D dto, T entity);
}
