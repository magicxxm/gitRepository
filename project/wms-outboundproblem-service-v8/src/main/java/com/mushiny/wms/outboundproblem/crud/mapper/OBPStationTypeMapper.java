package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.mapper.WarehouseMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPStationTypeDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPStationTypeMapper implements BaseMapper<OBPStationTypeDTO, OBPStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    @Autowired
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

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void updateEntityFromDTO(OBPStationTypeDTO dto, OBPStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
