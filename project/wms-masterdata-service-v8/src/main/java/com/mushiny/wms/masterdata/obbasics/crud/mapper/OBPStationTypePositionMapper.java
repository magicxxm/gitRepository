package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPStationTypePositionMapper implements BaseMapper<OBPStationTypePositionDTO, OBPStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final OBPStationTypeRepository obpStationTypeRepository;
    private final OBPStationTypeMapper obpStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public OBPStationTypePositionMapper(ApplicationContext applicationContext,
                                        OBPStationTypeRepository obpStationTypeRepository,
                                        OBPStationTypeMapper obpStationTypeMapper,
                                        WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.obpStationTypeMapper = obpStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public OBPStationTypePositionDTO toDTO(OBPStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        OBPStationTypePositionDTO dto = new OBPStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setObpStationType(obpStationTypeMapper.toDTO(entity.getObpStationType()));

        return dto;
    }

    @Override
    public OBPStationTypePosition toEntity(OBPStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPStationTypePosition entity = new OBPStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getStationTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getStationTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPStationTypePositionDTO dto, OBPStationTypePosition entity) {
    }
}
