package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IBPStationPositionMapper implements BaseMapper<IBPStationPositionDTO, IBPStationPosition> {

    private final ApplicationContext applicationContext;
    private final IBPStationRepository ibpStationRepository;
    private final IBPStationMapper ibpStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public IBPStationPositionMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper,
                                    IBPStationRepository ibpStationRepository,
                                    IBPStationMapper ibpStationMapper,
                                    WorkStationPositionMapper workStationPositionMapper,
                                    WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.ibpStationRepository = ibpStationRepository;
        this.ibpStationMapper = ibpStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public IBPStationPositionDTO toDTO(IBPStationPosition entity) {
        if (entity == null) {
            return null;
        }
        IBPStationPositionDTO dto = new IBPStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setIbpStation(ibpStationMapper.toDTO(entity.getIbpStation()));

        return dto;
    }

    @Override
    public IBPStationPosition toEntity(IBPStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        IBPStationPosition entity = new IBPStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStationId() != null) {
            entity.setIbpStation(ibpStationRepository.retrieve(dto.getStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(IBPStationPositionDTO dto, IBPStationPosition entity) {
    }
}
