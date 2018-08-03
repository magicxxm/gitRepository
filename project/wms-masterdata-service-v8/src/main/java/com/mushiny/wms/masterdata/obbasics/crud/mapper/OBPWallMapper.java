package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPWallDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPWall;
import com.mushiny.wms.masterdata.obbasics.repository.OBPWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class OBPWallMapper implements BaseMapper<OBPWallDTO, OBPWall> {

    private final OBPWallTypeRepository obpWallTypeRepository;
    private final OBPWallTypeMapper obpWallTypeMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public OBPWallMapper(OBPWallTypeRepository obpWallTypeRepository,
                         OBPWallTypeMapper obpWallTypeMapper,
                         ApplicationContext applicationContext,
                         WarehouseMapper warehouseMapper) {
        this.obpWallTypeRepository = obpWallTypeRepository;
        this.obpWallTypeMapper = obpWallTypeMapper;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public OBPWallDTO toDTO(OBPWall entity) {
        if (entity == null) {
            return null;
        }
        OBPWallDTO dto = new OBPWallDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setState(entity.getState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setObpWallType(obpWallTypeMapper.toDTO(entity.getObpWallType()));

        return dto;
    }

    @Override
    public OBPWall toEntity(OBPWallDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPWall entity = new OBPWall();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setState("Unoccupied");
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setObpWallType(obpWallTypeRepository.retrieve(dto.getTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPWallDTO dto, OBPWall entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setState(dto.getState());
        if (dto.getTypeId() != null) {
            entity.setObpWallType(obpWallTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setObpWallType(null);
        }
    }
}
