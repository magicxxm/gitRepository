package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SortingStationTypePositionMapper implements BaseMapper<SortingStationTypePositionDTO, SortingStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final SortingStationTypeRepository sortingStationTypeRepository;
    private final SortingStationTypeMapper sortingStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public SortingStationTypePositionMapper(ApplicationContext applicationContext,
                                            SortingStationTypeRepository sortingStationTypeRepository,
                                            SortingStationTypeMapper sortingStationTypeMapper,
                                            WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.sortingStationTypeRepository = sortingStationTypeRepository;
        this.sortingStationTypeMapper = sortingStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public SortingStationTypePositionDTO toDTO(SortingStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        SortingStationTypePositionDTO dto = new SortingStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setSortingStationType(sortingStationTypeMapper.toDTO(entity.getSortingStationType()));

        return dto;
    }

    @Override
    public SortingStationTypePosition toEntity(SortingStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        SortingStationTypePosition entity = new SortingStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setSortingStationType(sortingStationTypeRepository.retrieve(dto.getTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(SortingStationTypePositionDTO dto, SortingStationTypePosition entity) {
    }
}
