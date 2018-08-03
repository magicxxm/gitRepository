package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationPosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickStationPositionMapper implements BaseMapper<PickStationPositionDTO, PickStationPosition> {

    private final ApplicationContext applicationContext;
    private final PickStationRepository pickStationRepository;
    private final PickStationMapper pickStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public PickStationPositionMapper(ApplicationContext applicationContext,
                                     WarehouseMapper warehouseMapper,
                                     PickStationRepository pickStationRepository,
                                     PickStationMapper pickStationMapper,
                                     WorkStationPositionMapper workStationPositionMapper,
                                     WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.pickStationRepository = pickStationRepository;
        this.pickStationMapper = pickStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public PickStationPositionDTO toDTO(PickStationPosition entity) {
        if (entity == null) {
            return null;
        }
        PickStationPositionDTO dto = new PickStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setPickStation(pickStationMapper.toDTO(entity.getPickStation()));

        return dto;
    }

    @Override
    public PickStationPosition toEntity(PickStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PickStationPosition entity = new PickStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getPickStationId() != null) {
            entity.setPickStation(pickStationRepository.retrieve(dto.getPickStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickStationPositionDTO dto, PickStationPosition entity) {
    }
}
