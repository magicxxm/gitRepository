package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCell;

import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallRepository;
import org.springframework.stereotype.Component;

@Component
public class PickPackCellMapper implements BaseMapper<PickPackCellDTO, PickPackCell> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final PickPackCellTypeMapper pickPackCellTypeMapper;
    private final PickPackCellTypeRepository pickPackCellTypeRepository;
    private final PickPackWallMapper pickPackWallMapper;
    private final PickPackWallRepository pickPackWallRepository;
    private final DigitalLabelMapper digitalLabelMapper;
    private final DigitalLabelRepository digitalLabelRepository;

    public PickPackCellMapper(ApplicationContext applicationContext,
                              ClientRepository clientRepository,
                              PickPackCellTypeMapper pickPackCellTypeMapper,
                              PickPackCellTypeRepository pickPackCellTypeRepository,
                              PickPackWallMapper pickPackWallMapper,
                              PickPackWallRepository pickPackWallRepository,
                              DigitalLabelMapper digitalLabelMapper,
                              DigitalLabelRepository digitalLabelRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.pickPackCellTypeMapper = pickPackCellTypeMapper;
        this.pickPackCellTypeRepository = pickPackCellTypeRepository;
        this.pickPackWallMapper = pickPackWallMapper;
        this.pickPackWallRepository = pickPackWallRepository;
        this.digitalLabelMapper = digitalLabelMapper;
        this.digitalLabelRepository = digitalLabelRepository;
    }

    @Override
    public PickPackCellDTO toDTO(PickPackCell entity) {

        if (entity == null) {
            return null;
        }
        PickPackCellDTO dto = new PickPackCellDTO(entity);

        dto.setName(entity.getName());
        dto.setPickPackCellType(pickPackCellTypeMapper.toDTO(entity.getPickPackCellType()));
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setzPos(entity.getzPos());
        dto.setField(entity.getField());
        dto.setFieldIndex(entity.getFieldIndex());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setLabelColor(entity.getLabelColor());
        dto.setPickPackWall(pickPackWallMapper.toDTO(entity.getPickPackWall()));
        dto.setDigitalLabel1(digitalLabelMapper.toDTO(entity.getDigitalLabel1()));
        dto.setDigitalLabel2(digitalLabelMapper.toDTO(entity.getDigitalLabel2()));
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setArea(entity.getArea());
        dto.setStorageLocationType(entity.getStorageLocationType());

        return dto;
    }

    @Override
    public PickPackCell toEntity(PickPackCellDTO dto) {
        if (dto == null) {
            return null;
        }

        PickPackCell entity = new PickPackCell();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if(dto.getTypeId() != null) {
            entity.setPickPackCellType(pickPackCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setField(dto.getField());
        entity.setFieldIndex(dto.getFieldIndex());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        if(dto.getPickPackWallId() != null) {
            entity.setPickPackWall(pickPackWallRepository.retrieve(dto.getPickPackWallId()));
        }
        if(dto.getDigitalLabel1Id().length() > 0) {
            entity.setDigitalLabel1(digitalLabelRepository.retrieve(dto.getDigitalLabel1Id()));
        }
        if(dto.getDigitalLabel2Id().length() > 0) {
            entity.setDigitalLabel2(digitalLabelRepository.retrieve(dto.getDigitalLabel2Id()));
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setArea(dto.getArea());
        entity.setStorageLocationType(dto.getStorageLocationType());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackCellDTO dto, PickPackCell entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        if(dto.getTypeId() != null) {
            entity.setPickPackCellType(pickPackCellTypeRepository.retrieve(dto.getTypeId()));
        }
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setField(dto.getField());
        entity.setFieldIndex(dto.getFieldIndex());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setLabelColor(dto.getLabelColor());
        if(dto.getPickPackWallId() != null) {
            entity.setPickPackWall(pickPackWallRepository.retrieve(dto.getPickPackWallId()));
        }
        if(dto.getDigitalLabel1Id().length() > 0) {
            entity.setDigitalLabel1(digitalLabelRepository.retrieve(dto.getDigitalLabel1Id()));
        }
        if(dto.getDigitalLabel2Id().length() > 0) {
            entity.setDigitalLabel2(digitalLabelRepository.retrieve(dto.getDigitalLabel2Id()));
        }
        entity.setArea(dto.getArea());
        entity.setStorageLocationType(dto.getStorageLocationType());
    }
}
