package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinStationType;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinStationTypeMapper implements BaseMapper<ReBinStationTypeDTO, ReBinStationType> {

    private final ReBinWallTypeRepository reBinWallTypeRepository;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final ReBinWallTypeMapper reBinWallTypeMapper;

    public ReBinStationTypeMapper(ReBinWallTypeRepository reBinWallTypeRepository,
                                  ApplicationContext applicationContext,
                                  WarehouseMapper warehouseMapper,
                                  ReBinWallTypeMapper reBinWallTypeMapper) {
        this.reBinWallTypeRepository = reBinWallTypeRepository;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.reBinWallTypeMapper = reBinWallTypeMapper;
    }

    @Override
    public ReBinStationTypeDTO toDTO(ReBinStationType entity) {
        if (entity == null) {
            return null;
        }
        ReBinStationTypeDTO dto = new ReBinStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRebinWallType(reBinWallTypeMapper.toDTO(entity.getReBinWallType()));

        return dto;
    }

    @Override
    public ReBinStationType toEntity(ReBinStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinStationType entity = new ReBinStationType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getRebinWallTypeId() != null) {
            entity.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getRebinWallTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinStationTypeDTO dto, ReBinStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getRebinWallTypeId() != null) {
            entity.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getRebinWallTypeId()));
        } else {
            entity.setReBinWallType(null);
        }
    }
}
