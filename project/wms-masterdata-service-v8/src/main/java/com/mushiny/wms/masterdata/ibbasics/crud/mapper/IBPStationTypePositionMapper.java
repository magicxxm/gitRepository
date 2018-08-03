package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.IBPStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.IBPStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IBPStationTypePositionMapper implements BaseMapper<IBPStationTypePositionDTO, IBPStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final IBPStationTypeRepository ibpStationTypeRepository;
    private final IBPStationTypeMapper ibpStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public IBPStationTypePositionMapper(ApplicationContext applicationContext,
                                        IBPStationTypeRepository ibpStationTypeRepository,
                                        IBPStationTypeMapper ibpStationTypeMapper,
                                        WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.ibpStationTypeRepository = ibpStationTypeRepository;
        this.ibpStationTypeMapper = ibpStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public IBPStationTypePositionDTO toDTO(IBPStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        IBPStationTypePositionDTO dto = new IBPStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setIbpStationType(ibpStationTypeMapper.toDTO(entity.getIbpStationType()));

        return dto;
    }

    @Override
    public IBPStationTypePosition toEntity(IBPStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        IBPStationTypePosition entity = new IBPStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getStationTypeId() != null) {
            entity.setIbpStationType(ibpStationTypeRepository.retrieve(dto.getStationTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(IBPStationTypePositionDTO dto, IBPStationTypePosition entity) {
    }
}
