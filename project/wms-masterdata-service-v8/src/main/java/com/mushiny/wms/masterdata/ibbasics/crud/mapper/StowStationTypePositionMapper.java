package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StowStationTypePositionMapper implements BaseMapper<StowStationTypePositionDTO, StowStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final StowStationTypeMapper stowStationTypeMapper;
    private final StowStationTypeRepository stowStationTypeRepository;

    @Autowired
    public StowStationTypePositionMapper(ApplicationContext applicationContext,
                                         StowStationTypeMapper stowStationTypeMapper,
                                         StowStationTypeRepository stowStationTypeRepository) {
        this.applicationContext = applicationContext;
        this.stowStationTypeMapper = stowStationTypeMapper;
        this.stowStationTypeRepository = stowStationTypeRepository;
    }

    @Override
    public StowStationTypePositionDTO toDTO(StowStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        StowStationTypePositionDTO dto = new StowStationTypePositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setStowStationType(stowStationTypeMapper.toDTO(entity.getStowStationType()));
        return dto;
    }

    @Override
    public StowStationTypePosition toEntity(StowStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        StowStationTypePosition entity = new StowStationTypePosition();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getStowStationTypeId() != null) {
            entity.setStowStationType(stowStationTypeRepository.retrieve(dto.getStowStationTypeId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(StowStationTypePositionDTO dto, StowStationTypePosition entity) {

    }
}
