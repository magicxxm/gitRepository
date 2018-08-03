package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCellDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPCellMapper implements BaseMapper<OBPCellDTO, OBPCell> {

    private final OBPWallMapper obpWallMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public OBPCellMapper(OBPWallMapper obpWallMapper,
                         ApplicationContext applicationContext) {
        this.obpWallMapper = obpWallMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public OBPCellDTO toDTO(OBPCell entity) {
        if (entity == null) {
            return null;
        }
        OBPCellDTO dto = new OBPCellDTO(entity);

//        dto.setName(entity.getName());
//        dto.setLabelColor(entity.getLabelColor());
//        dto.setObpWall(obpWallMapper.toDTO(entity.getObpWall()));
//        dto.setOrderIndex(entity.getOrderIndex());
//        dto.setState(entity.getState());

        dto.setWarehouse(entity.getWarehouseId());

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

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPCellDTO dto, OBPCell entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
    }
}
