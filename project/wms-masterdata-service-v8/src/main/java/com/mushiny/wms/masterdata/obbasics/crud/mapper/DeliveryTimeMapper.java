package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import org.springframework.stereotype.Component;

@Component
public class DeliveryTimeMapper implements BaseMapper<DeliveryTimeDTO,DeliveryTime>{
    private final ApplicationContext applicationContext;
    public DeliveryTimeMapper(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    @Override
    public DeliveryTimeDTO toDTO(DeliveryTime entity) {
        if (entity == null) {
            return null;
        }
        DeliveryTimeDTO dto = new DeliveryTimeDTO(entity);
        dto.setDeliveryTime(entity.getDeliveryTime());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public DeliveryTime toEntity(DeliveryTimeDTO dto) {
        if (dto == null) {
            return null;
        }
        DeliveryTime entity = new DeliveryTime();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        if(dto.getDeliveryTime()!=null){
            entity.setDeliveryTime(dto.getDeliveryTime());
        }else{
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString(),"输入时间不能为空");
        }
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(DeliveryTimeDTO dto, DeliveryTime entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        if(dto.getDeliveryTime()!=null){
            entity.setDeliveryTime(dto.getDeliveryTime());
        }else{
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString(),"输入时间不能为空");
        }
    }
}