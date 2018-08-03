package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPCellDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPCell;
import com.mushiny.wms.masterdata.obbasics.repository.OBPCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.OBPWallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPCellMapper implements BaseMapper<OBPCellDTO, OBPCell> {

    private final ApplicationContext applicationContext;
    private final OBPCellTypeMapper obpCellTypeMapper;
    private final OBPCellTypeRepository obpCellTypeRepository;
    private final OBPWallMapper obpWallMapper;
    private final OBPWallRepository obpWallRepository;

    @Autowired
    public OBPCellMapper(ApplicationContext applicationContext,
                         OBPCellTypeMapper obpCellTypeMapper,
                         OBPCellTypeRepository obpCellTypeRepository,
                         OBPWallMapper obpWallMapper,
                         OBPWallRepository obpWallRepository) {
        this.applicationContext = applicationContext;
        this.obpCellTypeMapper = obpCellTypeMapper;
        this.obpCellTypeRepository = obpCellTypeRepository;
        this.obpWallMapper = obpWallMapper;
        this.obpWallRepository = obpWallRepository;
    }

    @Override
    public OBPCellDTO toDTO(OBPCell entity) {
        if (entity == null) {
            return null;
        }

        OBPCellDTO dto = new OBPCellDTO(entity);

        dto.setName(entity.getName());
        dto.setObpCellType(obpCellTypeMapper.toDTO(entity.getObpCellType()));
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setzPos(entity.getzPos());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setLabelColor(entity.getLabelColor());
        dto.setState(entity.getState());
        dto.setObpWall(obpWallMapper.toDTO(entity.getObpWall()));
//        dto.setLabelController(labelControllerMapper.toDTO(entity.getLabelController()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBPCell toEntity(OBPCellDTO dto) {
        if (dto == null) {
            return null;
        }

        OBPCell entity = new OBPCell();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if (dto.getTypeId() != null) {
            entity.setObpCellType(obpCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        entity.setState(dto.getState());
        if (dto.getWallId() != null) {
            entity.setObpWall(obpWallRepository.retrieve(dto.getWallId()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPCellDTO dto, OBPCell entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if (dto.getTypeId() != null) {
            entity.setObpCellType(obpCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        entity.setState(dto.getState());
        if (dto.getWallId() != null) {
            entity.setObpWall(obpWallRepository.retrieve(dto.getWallId()));
        }
    }
}

