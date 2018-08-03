package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StowStationPositionMapper implements BaseMapper<StowStationPositionDTO, StowStationPosition> {

    private final ApplicationContext applicationContext;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final StowStationMapper stowStationMapper;
    private final StowStationRepository stowStationRepository;

    @Autowired
    public StowStationPositionMapper(ApplicationContext applicationContext,
                                     WorkStationPositionMapper workStationPositionMapper,
                                     WorkStationPositionRepository workStationPositionRepository,
                                     StowStationMapper stowStationMapper,
                                     StowStationRepository stowStationRepository) {
        this.applicationContext = applicationContext;
        this.workStationPositionMapper = workStationPositionMapper;
        this.stowStationMapper = stowStationMapper;
        this.stowStationRepository = stowStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public StowStationPositionDTO toDTO(StowStationPosition entity) {
        if (entity == null) {
            return null;
        }
        StowStationPositionDTO dto = new StowStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setStowStation(stowStationMapper.toDTO(entity.getStowStation()));

        return dto;
    }

    @Override
    public StowStationPosition toEntity(StowStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        StowStationPosition entity = new StowStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPosition()!= null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStowStationId() != null) {
            entity.setStowStation(stowStationRepository.retrieve(dto.getStowStationId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(StowStationPositionDTO dto, StowStationPosition entity) {

    }
}
