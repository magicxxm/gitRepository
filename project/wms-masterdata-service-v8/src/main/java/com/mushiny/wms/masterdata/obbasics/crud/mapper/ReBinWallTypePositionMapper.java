package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypePositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallTypePosition;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinWallTypePositionMapper implements BaseMapper<ReBinWallTypePositionDTO, ReBinWallTypePosition> {

    private final ReBinCellTypeRepository reBinCellTypeRepository;
    private final ReBinCellTypeMapper reBinCellTypeMapper;
    private final ReBinWallTypeMapper reBinWallTypeMapper;
    private final ReBinWallTypeRepository reBinWallTypeRepository;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public ReBinWallTypePositionMapper(ReBinCellTypeRepository reBinCellTypeRepository,
                                       ReBinCellTypeMapper reBinCellTypeMapper,
                                       ReBinWallTypeMapper reBinWallTypeMapper,
                                       ReBinWallTypeRepository reBinWallTypeRepository,
                                       ApplicationContext applicationContext,
                                       WarehouseMapper warehouseMapper) {
        this.reBinCellTypeRepository = reBinCellTypeRepository;
        this.reBinCellTypeMapper = reBinCellTypeMapper;
        this.reBinWallTypeMapper = reBinWallTypeMapper;
        this.reBinWallTypeRepository = reBinWallTypeRepository;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ReBinWallTypePositionDTO toDTO(ReBinWallTypePosition entity) {
        if (entity == null) {
            return null;
        }
        ReBinWallTypePositionDTO dto = new ReBinWallTypePositionDTO(entity);

        dto.setName(entity.getName());
        dto.setxPos(entity.getxPos());
        dto.setyPos(entity.getyPos());
        dto.setzPos(entity.getzPos());
        dto.setOrderIndex(entity.getOrderIndex());

        dto.setWarehouse(entity.getWarehouseId());
        dto.setRebinWallType(reBinWallTypeMapper.toDTO(entity.getReBinWallType()));
        dto.setRebinCellType(reBinCellTypeMapper.toDTO(entity.getReBinCellType()));

        return dto;
    }

    @Override
    public ReBinWallTypePosition toEntity(ReBinWallTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinWallTypePosition entity = new ReBinWallTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setxPos(dto.getxPos());
        entity.setyPos(dto.getyPos());
        entity.setzPos(dto.getzPos());
        entity.setOrderIndex(dto.getOrderIndex());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getRebinWallTypeId() != null) {
            entity.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getRebinWallTypeId()));
        }
        if (dto.getRebinCellTypeId() != null) {
            entity.setReBinCellType(reBinCellTypeRepository.retrieve(dto.getRebinCellTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinWallTypePositionDTO dto, ReBinWallTypePosition entity) {
    }
}
