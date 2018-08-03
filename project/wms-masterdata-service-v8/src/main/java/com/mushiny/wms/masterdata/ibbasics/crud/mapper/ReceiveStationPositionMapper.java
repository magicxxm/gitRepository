package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStationPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveDestinationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveStationPositionMapper implements BaseMapper<ReceiveStationPositionDTO, ReceiveStationPosition> {

    private final ApplicationContext applicationContext;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final ReceiveStationMapper receiveStationMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final ReceiveStationRepository receiveStationRepository;

    @Autowired
    public ReceiveStationPositionMapper(ApplicationContext applicationContext,
                                        WorkStationPositionMapper workStationPositionMapper,
                                        ReceiveStationMapper receiveStationMapper,
                                        WorkStationPositionRepository workStationPositionRepository,
                                        ReceiveStationRepository receiveStationRepository) {
        this.applicationContext = applicationContext;
        this.workStationPositionMapper = workStationPositionMapper;
        this.receiveStationMapper = receiveStationMapper;
        this.workStationPositionRepository = workStationPositionRepository;
        this.receiveStationRepository = receiveStationRepository;
    }

    @Override
    public ReceiveStationPositionDTO toDTO(ReceiveStationPosition entity) {
        if (entity == null) {
            return null;
        }
        ReceiveStationPositionDTO dto = new ReceiveStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());

//        dto.setWarehouse(warehouseMapper.toDTO(entity.getWarehouse()));
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setReceiveStation(receiveStationMapper.toDTO(entity.getReceiveStation()));

        return dto;
    }

    @Override
    public ReceiveStationPosition toEntity(ReceiveStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveStationPosition entity = new ReceiveStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPosition()!= null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getReceiveStationId() != null) {
            entity.setReceiveStation(receiveStationRepository.retrieve(dto.getReceiveStationId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveStationPositionDTO dto, ReceiveStationPosition entity) {
    }
}
