package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinCellTypeMapper implements BaseMapper<ReBinCellTypeDTO, ReBinCellType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public ReBinCellTypeMapper(ApplicationContext applicationContext,
                               WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public ReBinCellTypeDTO toDTO(ReBinCellType entity) {
        if (entity == null) {
            return null;
        }
        ReBinCellTypeDTO dto = new ReBinCellTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setVolume(entity.getVolume());
        dto.setLiftingCapacity(entity.getLiftingCapacity());

        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReBinCellType toEntity(ReBinCellTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinCellType entity = new ReBinCellType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getVolume());
        entity.setLiftingCapacity(dto.getLiftingCapacity());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinCellTypeDTO dto, ReBinCellType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getVolume());
        entity.setLiftingCapacity(dto.getLiftingCapacity());
    }
}
