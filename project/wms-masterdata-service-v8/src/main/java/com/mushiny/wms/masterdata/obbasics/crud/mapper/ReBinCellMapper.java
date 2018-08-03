package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCell;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReBinCellMapper implements BaseMapper<ReBinCellDTO, ReBinCell> {

    private final ApplicationContext applicationContext;
    private final ReBinCellTypeMapper reBinCellTypeMapper;
    private final ReBinCellTypeRepository reBinCellTypeRepository;
    private final ReBinWallMapper reBinWallMapper;
    private final ReBinWallRepository reBinWallRepository;

    @Autowired
    public ReBinCellMapper(ApplicationContext applicationContext,
                           ReBinCellTypeMapper reBinCellTypeMapper,
                           ReBinCellTypeRepository reBinCellTypeRepository,
                           ReBinWallMapper reBinWallMapper,
                           ReBinWallRepository reBinWallRepository) {
        this.applicationContext = applicationContext;
        this.reBinCellTypeMapper = reBinCellTypeMapper;
        this.reBinCellTypeRepository = reBinCellTypeRepository;
        this.reBinWallMapper = reBinWallMapper;
        this.reBinWallRepository = reBinWallRepository;
    }

    @Override
    public ReBinCellDTO toDTO(ReBinCell entity) {
        if (entity == null) {
            return null;
        }

        ReBinCellDTO dto = new ReBinCellDTO(entity);

        dto.setName(entity.getName());
        dto.setRebinCellType(reBinCellTypeMapper.toDTO(entity.getType()));
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setzPos(entity.getzPos());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setLabelColor(entity.getLabelColor());
        dto.setRebinWall(reBinWallMapper.toDTO(entity.getReBinWall()));
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReBinCell toEntity(ReBinCellDTO dto) {
        if (dto == null) {
            return null;
        }

        ReBinCell entity = new ReBinCell();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if (dto.getTypeId() != null) {
            entity.setType(reBinCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getRebinWallId() != null) {
            entity.setReBinWall(reBinWallRepository.retrieve(dto.getRebinWallId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinCellDTO dto, ReBinCell entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if (dto.getTypeId() != null) {
            entity.setType(reBinCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getRebinWallId() != null) {
            entity.setReBinWall(reBinWallRepository.retrieve(dto.getRebinWallId()));
        }
    }
}

