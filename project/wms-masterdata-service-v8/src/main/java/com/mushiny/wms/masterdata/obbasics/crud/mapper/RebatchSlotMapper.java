package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchSlotDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RebatchSlotMapper implements BaseMapper<RebatchSlotDTO, RebatchSlot> {

    private final ApplicationContext applicationContext;

    @Autowired
    public RebatchSlotMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public RebatchSlotDTO toDTO(RebatchSlot entity) {
        if (entity == null) {
            return null;
        }

        RebatchSlotDTO dto = new RebatchSlotDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setLabelColor(entity.getLabelColor());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public RebatchSlot toEntity(RebatchSlotDTO dto) {
        if (dto == null) {
            return null;
        }

        RebatchSlot entity = new RebatchSlot();

        entity.setName(dto.getName());
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setOrderIndex(dto.getOrderIndex());
        if (dto.getLabelColor() != null) {
            entity.setLabelColor(dto.getLabelColor());
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(RebatchSlotDTO dto, RebatchSlot entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setName(dto.getName());
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setOrderIndex(dto.getOrderIndex());
        if (dto.getLabelColor() != null) {
            entity.setLabelColor(dto.getLabelColor());
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}

