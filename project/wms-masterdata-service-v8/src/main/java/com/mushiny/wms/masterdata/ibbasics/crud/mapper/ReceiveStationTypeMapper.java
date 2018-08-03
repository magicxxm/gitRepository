package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationType;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveStationTypeMapper implements BaseMapper<ReceiveStationTypeDTO, ReceiveStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public ReceiveStationTypeMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ReceiveStationTypeDTO toDTO(ReceiveStationType entity) {
        if (entity == null) {
            return null;
        }
        ReceiveStationTypeDTO dto = new ReceiveStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setReceiveStationType(entity.getReceiveStationType());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public ReceiveStationType toEntity(ReceiveStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveStationType entity = new ReceiveStationType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setReceiveStationType(dto.getReceiveStationType());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveStationTypeDTO dto, ReceiveStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setReceiveStationType(dto.getReceiveStationType());
    }
}
