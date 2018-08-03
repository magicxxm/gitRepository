package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;
import org.springframework.stereotype.Component;

@Component
public class RebatchStationTypeMapper implements BaseMapper<RebatchStationTypeDTO, RebatchStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public RebatchStationTypeMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public RebatchStationTypeDTO toDTO(RebatchStationType entity) {
        if (entity == null) {
            return null;
        }
        RebatchStationTypeDTO dto = new RebatchStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public RebatchStationType toEntity(RebatchStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        RebatchStationType entity = new RebatchStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(RebatchStationTypeDTO dto, RebatchStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
