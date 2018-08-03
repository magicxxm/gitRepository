package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PackingStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackingStationTypePositionMapper implements BaseMapper<PackingStationTypePositionDTO, PackingStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final PackingStationTypeRepository packingStationTypeRepository;
    private final PackingStationTypeMapper packingStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public PackingStationTypePositionMapper(ApplicationContext applicationContext,
                                            PackingStationTypeRepository packingStationTypeRepository,
                                            PackingStationTypeMapper packingStationTypeMapper,
                                            WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.packingStationTypeRepository = packingStationTypeRepository;
        this.packingStationTypeMapper = packingStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public PackingStationTypePositionDTO toDTO(PackingStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        PackingStationTypePositionDTO dto = new PackingStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setPackingStationType(packingStationTypeMapper.toDTO(entity.getPackingStationType()));

        return dto;
    }

    @Override
    public PackingStationTypePosition toEntity(PackingStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        PackingStationTypePosition entity = new PackingStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getPackingStationTypeId() != null) {
            entity.setPackingStationType(packingStationTypeRepository.retrieve(dto.getPackingStationTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PackingStationTypePositionDTO dto, PackingStationTypePosition entity) {
    }
}
