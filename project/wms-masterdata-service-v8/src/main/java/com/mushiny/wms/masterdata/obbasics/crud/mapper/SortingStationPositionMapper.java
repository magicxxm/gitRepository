package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationPosition;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SortingStationPositionMapper implements BaseMapper<SortingStationPositionDTO, SortingStationPosition> {

    private final ApplicationContext applicationContext;
    private final SortingStationRepository sortingStationRepository;
    private final SortingStationMapper sortingStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public SortingStationPositionMapper(ApplicationContext applicationContext,
                                        WarehouseMapper warehouseMapper,
                                        SortingStationRepository sortingStationRepository,
                                        SortingStationMapper sortingStationMapper,
                                        WorkStationPositionMapper workStationPositionMapper,
                                        WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.sortingStationRepository = sortingStationRepository;
        this.sortingStationMapper = sortingStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public SortingStationPositionDTO toDTO(SortingStationPosition entity) {
        if (entity == null) {
            return null;
        }
        SortingStationPositionDTO dto = new SortingStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setSortingStation(sortingStationMapper.toDTO(entity.getSortingStation()));

        return dto;
    }

    @Override
    public SortingStationPosition toEntity(SortingStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        SortingStationPosition entity = new SortingStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStationId() != null) {
            entity.setSortingStation(sortingStationRepository.retrieve(dto.getStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(SortingStationPositionDTO dto, SortingStationPosition entity) {
    }
}
