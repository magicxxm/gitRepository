package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickStationTypePositionMapper implements BaseMapper<PickStationTypePositionDTO, PickStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final PickStationTypeRepository pickStationTypeRepository;
    private final PickStationTypeMapper pickStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public PickStationTypePositionMapper(ApplicationContext applicationContext,
                                         PickStationTypeRepository pickStationTypeRepository,
                                         PickStationTypeMapper pickStationTypeMapper,
                                         WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.pickStationTypeRepository = pickStationTypeRepository;
        this.pickStationTypeMapper = pickStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public PickStationTypePositionDTO toDTO(PickStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        PickStationTypePositionDTO dto = new PickStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setPickStationType(pickStationTypeMapper.toDTO(entity.getPickStationType()));

        return dto;
    }

    @Override
    public PickStationTypePosition toEntity(PickStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PickStationTypePosition entity = new PickStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getPickStationTypeId() != null) {
            entity.setPickStationType(pickStationTypeRepository.retrieve(dto.getPickStationTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickStationTypePositionDTO dto, PickStationTypePosition entity) {
    }
}
