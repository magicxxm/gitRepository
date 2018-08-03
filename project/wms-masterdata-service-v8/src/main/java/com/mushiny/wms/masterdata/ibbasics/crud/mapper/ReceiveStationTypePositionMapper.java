package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveDestinationRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveStationTypePositionMapper implements BaseMapper<ReceiveStationTypePositionDTO, ReceiveStationTypePosition> {

    private final ReceiveDestinationRepository receivingDestinationRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveStationTypeMapper receivingStationTypeMapper;
    private final ReceiveDestinationMapper receivingDestinationMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public ReceiveStationTypePositionMapper(ApplicationContext applicationContext,
                                            WarehouseMapper warehouseMapper,
                                            ReceiveDestinationRepository receivingDestinationRepository,
                                            ReceiveDestinationMapper receivingDestinationMapper,
                                            ReceiveStationTypeMapper receivingStationTypeMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.receivingDestinationRepository = receivingDestinationRepository;
        this.receivingDestinationMapper = receivingDestinationMapper;
        this.receivingStationTypeMapper = receivingStationTypeMapper;
    }

    @Override
    public ReceiveStationTypePositionDTO toDTO(ReceiveStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        ReceiveStationTypePositionDTO dto = new ReceiveStationTypePositionDTO(entity);

        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouse()));
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setReceiveStationType(receivingStationTypeMapper.toDTO(entity.getReceivingStationType()));
        dto.setReceiveDestination(receivingDestinationMapper.toDTO(entity.getReceivingDestination()));

        return dto;
    }

    @Override
    public ReceiveStationTypePosition toEntity(ReceiveStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveStationTypePosition entity = new ReceiveStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getReceiveDestinationId() != null) {
            entity.setReceivingDestination(receivingDestinationRepository.retrieve(dto.getReceiveDestinationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveStationTypePositionDTO dto, ReceiveStationTypePosition entity) {
    }
}
