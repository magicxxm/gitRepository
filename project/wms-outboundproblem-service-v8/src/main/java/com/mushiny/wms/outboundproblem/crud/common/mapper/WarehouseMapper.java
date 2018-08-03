package com.mushiny.wms.outboundproblem.crud.common.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.dto.WarehouseDTO;
import com.mushiny.wms.outboundproblem.domain.common.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper implements BaseMapper<WarehouseDTO, Warehouse> {

    @Override
    public WarehouseDTO toDTO(Warehouse entity) {
        if (entity == null) {
            return null;
        }

        WarehouseDTO dto = new WarehouseDTO(entity);

        dto.setName(entity.getName());
        dto.setWarehouseNo(entity.getWarehouseNo());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setFax(entity.getFax());

        return dto;
    }

    @Override
    public Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) {
            return null;
        }

        Warehouse entity = new Warehouse();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setWarehouseNo(dto.getWarehouseNo());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setFax(dto.getFax());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(WarehouseDTO dto, Warehouse entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setFax(dto.getFax());
    }
}
