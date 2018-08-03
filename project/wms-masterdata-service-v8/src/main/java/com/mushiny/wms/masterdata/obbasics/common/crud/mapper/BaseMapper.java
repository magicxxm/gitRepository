package com.mushiny.wms.masterdata.obbasics.common.crud.mapper;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface BaseMapper<T extends BaseEntity, S extends BaseDTO> {

    default List<S> mapEntitiesIntoDTOs(Iterable<T> entities) {
        if (entities == null) {
            return null;
        }

        List<S> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(mapEntityIntoDTO(e)));
        return dtos;
    }

    default List<T> mapDTOsIntoEntities(Iterable<S> dtos) {
        if (dtos == null) {
            return null;
        }

        List<T> entities = new ArrayList<>();
        dtos.forEach(e -> entities.add(mapDTOIntoEntity(e)));
        return entities;
    }

    S mapEntityIntoDTO(T entity);

    T mapDTOIntoEntity(S dto);

    void updateEntityFromDTO(S dto, T entity);

    default Page<S> mapEntityPageIntoDTOPage(Pageable pageable, Page<T> source) {
        if (source == null) {
            return null;
        }

        List<S> dtos = mapEntitiesIntoDTOs(source.getContent());
        return new PageImpl<>(dtos, pageable, source.getTotalElements());
    }

    default Warehouse warehouseFromId(String id) {
        if (id == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        return warehouse;
    }

    default Client clientFromId(String id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
