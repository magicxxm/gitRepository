package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPStationTypePositionDTO;
import com.mushiny.wms.outboundproblem.domain.OBPStationTypePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPStationTypePositionMapper implements BaseMapper<OBPStationTypePositionDTO, OBPStationTypePosition> {

    @Autowired
    private final ApplicationContext applicationContext;
    private final OBPStationTypeMapper obpStationTypeMapper;

    public OBPStationTypePositionMapper(ApplicationContext applicationContext,
                                        OBPStationTypeMapper obpStationTypeMapper) {
        this.applicationContext = applicationContext;
        this.obpStationTypeMapper = obpStationTypeMapper;
    }

    @Override
    public OBPStationTypePositionDTO toDTO(OBPStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        OBPStationTypePositionDTO dto = new OBPStationTypePositionDTO(entity);

        dto.setPositionIndex(entity.getPositionIndex());

        dto.setWarehouse(entity.getWarehouseId());
        dto.setObpStationType(obpStationTypeMapper.toDTO(entity.getObpStationType()));

        return dto;
    }

    @Override
    public OBPStationTypePosition toEntity(OBPStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPStationTypePosition entity = new OBPStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationTypePositionDTO dto, OBPStationTypePosition entity) {
    }
}
