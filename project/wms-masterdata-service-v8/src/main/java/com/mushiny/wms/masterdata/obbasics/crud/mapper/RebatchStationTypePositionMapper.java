package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RebatchStationTypePositionMapper implements BaseMapper<RebatchStationTypePositionDTO, RebatchStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final RebatchStationTypeRepository rebatchStationTypeRepository;
    private final RebatchStationTypeMapper rebatchStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public RebatchStationTypePositionMapper(ApplicationContext applicationContext,
                                            RebatchStationTypeRepository rebatchStationTypeRepository,
                                            RebatchStationTypeMapper rebatchStationTypeMapper,
                                            WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.rebatchStationTypeRepository = rebatchStationTypeRepository;
        this.rebatchStationTypeMapper = rebatchStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public RebatchStationTypePositionDTO toDTO(RebatchStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        RebatchStationTypePositionDTO dto = new RebatchStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRebatchStationType(rebatchStationTypeMapper.toDTO(entity.getRebatchStationType()));

        return dto;
    }

    @Override
    public RebatchStationTypePosition toEntity(RebatchStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        RebatchStationTypePosition entity = new RebatchStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setRebatchStationType(rebatchStationTypeRepository.retrieve(dto.getTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(RebatchStationTypePositionDTO dto, RebatchStationTypePosition entity) {
    }
}
