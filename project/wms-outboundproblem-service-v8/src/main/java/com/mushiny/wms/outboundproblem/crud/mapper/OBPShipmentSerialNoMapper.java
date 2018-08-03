package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.dto.OBPShipmentSerialNoDTO;
import com.mushiny.wms.outboundproblem.domain.OBPShipmentSerialNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBPShipmentSerialNoMapper implements BaseMapper<OBPShipmentSerialNoDTO, OBPShipmentSerialNo> {

    private final ApplicationContext applicationContext;

    @Autowired
    public OBPShipmentSerialNoMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public OBPShipmentSerialNoDTO toDTO(OBPShipmentSerialNo entity) {
        if (entity == null) {
            return null;
        }
        OBPShipmentSerialNoDTO dto = new OBPShipmentSerialNoDTO();
        dto.setId(entity.getId());
        dto.setShipmentId(entity.getShipmentId());
        dto.setItemDataId(entity.getItemDataId());
        dto.setSerialNo(entity.getSerialNo());
        dto.setScaned(entity.getScaned());
        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public OBPShipmentSerialNo toEntity(OBPShipmentSerialNoDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPShipmentSerialNo entity = new OBPShipmentSerialNo();
        entity.setId(dto.getId());
        entity.setShipmentId(dto.getShipmentId());
        entity.setItemDataId(dto.getItemDataId());
        entity.setSerialNo(dto.getSerialNo());
        entity.setScaned(dto.getScaned());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(dto.getWarehouseId());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPShipmentSerialNoDTO dto, OBPShipmentSerialNo entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
    }
}
