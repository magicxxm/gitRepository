package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;
import org.springframework.stereotype.Component;

@Component
public class OBPStationTypeMapper implements BaseMapper<OBPStationTypeDTO, OBPStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public OBPStationTypeMapper(ApplicationContext applicationContext,
                                WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public OBPStationTypeDTO toDTO(OBPStationType entity) {
        if (entity == null) {
            return null;
        }
        OBPStationTypeDTO dto = new OBPStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setDescription(entity.getDescription());

        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBPStationType toEntity(OBPStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPStationType entity = new OBPStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationTypeDTO dto, OBPStationType entity) {

        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
