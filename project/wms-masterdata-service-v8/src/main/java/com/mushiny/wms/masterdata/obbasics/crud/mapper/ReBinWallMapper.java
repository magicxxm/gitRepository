package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWall;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinWallTypeRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinWallMapper implements BaseMapper<ReBinWallDTO, ReBinWall> {

    private final ReBinWallTypeRepository reBinWallTypeRepository;
    private final ReBinWallTypeMapper reBinWallTypeMapper;
    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public ReBinWallMapper(ReBinWallTypeRepository reBinWallTypeRepository,
                           ReBinWallTypeMapper reBinWallTypeMapper,
                           ApplicationContext applicationContext,
                           WarehouseMapper warehouseMapper) {
        this.reBinWallTypeRepository = reBinWallTypeRepository;
        this.reBinWallTypeMapper = reBinWallTypeMapper;
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ReBinWallDTO toDTO(ReBinWall entity) {
        if (entity == null) {
            return null;
        }
        ReBinWallDTO dto = new ReBinWallDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());

        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRebinWallType(reBinWallTypeMapper.toDTO(entity.getReBinWallType()));

        return dto;
    }

    @Override
    public ReBinWall toEntity(ReBinWallDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinWall entity = new ReBinWall();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinWallDTO dto, ReBinWall entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());

        if (dto.getTypeId() != null) {
            entity.setReBinWallType(reBinWallTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setReBinWallType(null);
        }
    }
}
