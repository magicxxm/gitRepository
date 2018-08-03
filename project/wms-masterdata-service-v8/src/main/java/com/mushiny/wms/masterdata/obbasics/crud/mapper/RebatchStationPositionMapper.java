package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationPosition;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RebatchStationPositionMapper implements BaseMapper<RebatchStationPositionDTO, RebatchStationPosition> {

    private final ApplicationContext applicationContext;
    private final RebatchStationRepository rebatchStationRepository;
    private final RebatchStationMapper rebatchStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public RebatchStationPositionMapper(ApplicationContext applicationContext,
                                        WarehouseMapper warehouseMapper,
                                        RebatchStationRepository rebatchStationRepository,
                                        RebatchStationMapper rebatchStationMapper,
                                        WorkStationPositionMapper workStationPositionMapper,
                                        WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.rebatchStationRepository = rebatchStationRepository;
        this.rebatchStationMapper = rebatchStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public RebatchStationPositionDTO toDTO(RebatchStationPosition entity) {
        if (entity == null) {
            return null;
        }
        RebatchStationPositionDTO dto = new RebatchStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setRebatchStation(rebatchStationMapper.toDTO(entity.getRebatchStation()));

        return dto;
    }

    @Override
    public RebatchStationPosition toEntity(RebatchStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        RebatchStationPosition entity = new RebatchStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStationId() != null) {
            entity.setRebatchStation(rebatchStationRepository.retrieve(dto.getStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(RebatchStationPositionDTO dto, RebatchStationPosition entity) {
    }
}
