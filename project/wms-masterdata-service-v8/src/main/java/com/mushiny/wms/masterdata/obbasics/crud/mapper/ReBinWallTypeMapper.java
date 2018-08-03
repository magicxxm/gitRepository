package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinWallType;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class ReBinWallTypeMapper implements BaseMapper<ReBinWallTypeDTO, ReBinWallType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public ReBinWallTypeMapper(ApplicationContext applicationContext,
                               WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }


    @Override
    public ReBinWallTypeDTO toDTO(ReBinWallType entity) {
        if (entity == null) {
            return null;
        }
        ReBinWallTypeDTO dto = new ReBinWallTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReBinWallType toEntity(ReBinWallTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        ReBinWallType entity = new ReBinWallType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setNumberOfColumns(dto.getNumberOfColumns());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReBinWallTypeDTO dto, ReBinWallType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setNumberOfColumns(dto.getNumberOfColumns());

    }
}
