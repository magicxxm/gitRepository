package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationType;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class PickStationTypeMapper implements BaseMapper<PickStationTypeDTO, PickStationType> {

    private final ApplicationContext applicationContext;
    private final PickPackWallTypeMapper pickPackWallTypeMapper;
    private final PickPackWallTypeRepository pickPackWallTypeRepository;
    private final WarehouseMapper warehouseMapper;

    public PickStationTypeMapper(ApplicationContext applicationContext,
                                 PickPackWallTypeMapper pickPackWallTypeMapper,
                                 PickPackWallTypeRepository pickPackWallTypeRepository,
                                 WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.pickPackWallTypeMapper = pickPackWallTypeMapper;
        this.pickPackWallTypeRepository = pickPackWallTypeRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public PickStationTypeDTO toDTO(PickStationType entity) {
        if (entity == null) {
            return null;
        }
        PickStationTypeDTO dto = new PickStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPickPackWallType(pickPackWallTypeMapper.toDTO(entity.getPickPackWallType()));
        dto.setPickStationType(entity.getPickStationType());
        dto.setWarehouse(entity.getWarehouseId());
        dto.setIsPrintFHD(entity.getIsPrintFHD());
        dto.setIsPrintZXD(entity.getIsPrintZXD());
        dto.setIsPrintQD(entity.getIsPrintQD());

        return dto;
    }

    @Override
    public PickStationType toEntity(PickStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        PickStationType entity = new PickStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPickStationType(dto.getPickStationType());
        entity.setIsPrintFHD(dto.getIsPrintFHD());
        entity.setIsPrintZXD(dto.getIsPrintZXD());
        entity.setIsPrintQD(dto.getIsPrintQD());
        if(dto.getPickPackWallTypeId() != null) {
            entity.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getPickPackWallTypeId()));
        }

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickStationTypeDTO dto, PickStationType entity) {

        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPickStationType(dto.getPickStationType());
        entity.setIsPrintFHD(dto.getIsPrintFHD());
        entity.setIsPrintZXD(dto.getIsPrintZXD());
        entity.setIsPrintQD(dto.getIsPrintQD());
        if(dto.getPickPackWallTypeId() != null) {
            entity.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getPickPackWallTypeId()));
        }

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
