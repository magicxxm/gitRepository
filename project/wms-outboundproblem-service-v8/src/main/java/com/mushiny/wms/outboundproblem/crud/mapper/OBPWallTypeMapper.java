package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.dto.OBPWallTypeDTO;
import com.mushiny.wms.outboundproblem.domain.OBPWallType;
import org.springframework.stereotype.Component;

@Component
public class OBPWallTypeMapper implements BaseMapper<OBPWallTypeDTO, OBPWallType> {

    private final ApplicationContext applicationContext;

    public OBPWallTypeMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public OBPWallTypeDTO toDTO(OBPWallType entity) {
        if (entity == null) {
            return null;
        }
        OBPWallTypeDTO dto = new OBPWallTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setNumberOfColumns(entity.getNumberOfColumns());

        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBPWallType toEntity(OBPWallTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPWallType entity = new OBPWallType();

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
    public void updateEntityFromDTO(OBPWallTypeDTO dto, OBPWallType entity) {
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
