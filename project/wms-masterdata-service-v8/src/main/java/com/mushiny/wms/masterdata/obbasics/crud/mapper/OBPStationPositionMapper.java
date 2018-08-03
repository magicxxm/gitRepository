package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPStationPositionMapper implements BaseMapper<OBPStationPositionDTO, OBPStationPosition> {

    private final ApplicationContext applicationContext;
    private final OBPStationRepository obpStationRepository;
    private final OBPStationMapper obpStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public OBPStationPositionMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper,
                                    OBPStationRepository obpStationRepository,
                                    OBPStationMapper obpStationMapper,
                                    WorkStationPositionMapper workStationPositionMapper,
                                    WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.obpStationRepository = obpStationRepository;
        this.obpStationMapper = obpStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public OBPStationPositionDTO toDTO(OBPStationPosition entity) {
        if (entity == null) {
            return null;
        }
        OBPStationPositionDTO dto = new OBPStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setObpStation(obpStationMapper.toDTO(entity.getObpStation()));

        return dto;
    }

    @Override
    public OBPStationPosition toEntity(OBPStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPStationPosition entity = new OBPStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStationId() != null) {
            entity.setObpStation(obpStationRepository.retrieve(dto.getStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationPositionDTO dto, OBPStationPosition entity) {
    }
}
