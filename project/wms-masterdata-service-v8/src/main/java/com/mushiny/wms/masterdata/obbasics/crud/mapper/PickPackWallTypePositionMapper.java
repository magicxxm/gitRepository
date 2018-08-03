package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackFieldTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class PickPackWallTypePositionMapper implements BaseMapper<PickPackWallTypePositionDTO, PickPackWallTypePosition> {

    private final PickPackWallTypeMapper pickPackWallTypeMapper;
    private final PickPackWallTypeRepository pickPackWallTypeRepository;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final PickPackFieldTypeMapper pickPackFieldTypeMapper;
    private final PickPackFieldTypeRepository pickPackFieldTypeRepository;

    public PickPackWallTypePositionMapper(PickPackWallTypeMapper pickPackWallTypeMapper,
                                          PickPackWallTypeRepository pickPackWallTypeRepository,
                                          ApplicationContext applicationContext,
                                          WarehouseMapper warehouseMapper,
                                          PickPackFieldTypeMapper pickPackFieldTypeMapper,
                                          PickPackFieldTypeRepository pickPackFieldTypeRepository) {
        this.pickPackWallTypeMapper = pickPackWallTypeMapper;
        this.pickPackWallTypeRepository = pickPackWallTypeRepository;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.pickPackFieldTypeMapper = pickPackFieldTypeMapper;
        this.pickPackFieldTypeRepository = pickPackFieldTypeRepository;
    }

    @Override
    public PickPackWallTypePositionDTO toDTO(PickPackWallTypePosition entity) {
        if (entity == null) {
            return null;
        }
        PickPackWallTypePositionDTO dto = new PickPackWallTypePositionDTO(entity);

        dto.setPickPackWallType(pickPackWallTypeMapper.toDTO(entity.getPickPackWallType()));
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setPickPackFieldType(pickPackFieldTypeMapper.toDTO(entity.getPickPackFieldType()));
        dto.setWarehouse(entity.getWarehouseId());
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setPosition(entity.getPosition());
        return dto;
    }

    @Override
    public PickPackWallTypePosition toEntity(PickPackWallTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PickPackWallTypePosition entity = new PickPackWallTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());

        if (dto.getWallTypeId() != null) {
            entity.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getWallTypeId()));
        }
        entity.setOrderIndex(dto.getOrderIndex());
        if (dto.getFieldTypeId() != null) {
            entity.setPickPackFieldType(pickPackFieldTypeRepository.retrieve(dto.getFieldTypeId()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setPosition(dto.getPosition());
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackWallTypePositionDTO dto, PickPackWallTypePosition entity) {
    }
}
