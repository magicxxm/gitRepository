package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationType;
import org.springframework.stereotype.Component;

@Component
public class IBPStationTypeMapper implements BaseMapper<IBPStationTypeDTO, IBPStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public IBPStationTypeMapper(ApplicationContext applicationContext,
                                WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public IBPStationTypeDTO toDTO(IBPStationType entity) {
        if (entity == null) {
            return null;
        }
        IBPStationTypeDTO dto = new IBPStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public IBPStationType toEntity(IBPStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        IBPStationType entity = new IBPStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(IBPStationTypeDTO dto, IBPStationType entity) {

        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
