package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryPointDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.DeliveryTimeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryPoint;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;
import com.mushiny.wms.masterdata.obbasics.repository.CarrierRepository;
import com.mushiny.wms.masterdata.obbasics.repository.DeliverySortCodeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.DeliveryTimeRepository;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPointMapper implements BaseMapper<DeliveryPointDTO,DeliveryPoint> {
    private final ApplicationContext applicationContext;
    private final CarrierMapper carrierMapper;
    private final CarrierRepository carrierRepository;
    private final DeliveryTimeMapper deliveryTimeMapper;
    private final DeliveryTimeRepository deliveryTimeRepository;
    private final DeliverySortCodeMapper deliverySortCodeMapper;
    private final DeliverySortCodeRepository deliverySortCodeRepository;



    public DeliveryPointMapper(ApplicationContext applicationContext,CarrierMapper carrierMapper,
                               CarrierRepository carrierRepository,DeliveryTimeMapper deliveryTimeMapper,
                               DeliveryTimeRepository deliveryTimeRepository,DeliverySortCodeMapper deliverySortCodeMapper,
                               DeliverySortCodeRepository deliverySortCodeRepository){
            this.applicationContext=applicationContext;
        this.carrierMapper=carrierMapper;
        this.carrierRepository=carrierRepository;
        this.deliveryTimeMapper=deliveryTimeMapper;
        this.deliveryTimeRepository=deliveryTimeRepository;
        this.deliverySortCodeRepository=deliverySortCodeRepository;
        this.deliverySortCodeMapper=deliverySortCodeMapper;
    }
    @Override
    public DeliveryPointDTO toDTO(DeliveryPoint entity) {
        if (entity == null) {
            return null;
        }
        DeliveryPointDTO dto=new DeliveryPointDTO(entity);
        if(entity.getCarrier()!=null){
            dto.setCarrier(carrierMapper.toDTO(entity.getCarrier()));
        }else{
            dto.setCarrier(null);
        }
        if(entity.getDeliveryTime()!=null){
            dto.setDeliveryTime(deliveryTimeMapper.toDTO(entity.getDeliveryTime()));
        }else{
            dto.setDeliveryTime(null);
        }
        if(entity.getDeliverySortCode()!=null){
            dto.setDeliverySortCode(deliverySortCodeMapper.toDTO(entity.getDeliverySortCode()));
        }else{
            dto.setDeliverySortCode(null);
        }
        return dto;
    }

    @Override
    public DeliveryPoint toEntity(DeliveryPointDTO dto) {
        if (dto == null) {
            return null;
        }
        DeliveryPoint entity=new DeliveryPoint();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getCarrierId() != null) {
            entity.setCarrier(carrierRepository.retrieve(dto.getCarrierId()));
        }
        if (dto.getTime() != null) {
            entity.setDeliveryTime(deliveryTimeRepository.retrieve(dto.getTime()));
        }
        if (dto.getSortCodeId() != null) {
            entity.setDeliverySortCode(deliverySortCodeRepository.retrieve(dto.getSortCodeId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(DeliveryPointDTO dto, DeliveryPoint entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());

        if (dto.getCarrierId() != null) {
            entity.setCarrier(carrierRepository.retrieve(dto.getCarrierId()));
        } else {
            entity.setCarrier(null);
        }
        if (dto.getTime() != null) {
            entity.setDeliveryTime(deliveryTimeRepository.retrieve(dto.getTime()));
        } else {
            entity.setDeliveryTime(null);
        }
        if (dto.getSortCodeId() != null) {
            entity.setDeliverySortCode(deliverySortCodeRepository.retrieve(dto.getSortCodeId()));
        } else {
            entity.setDeliverySortCode(null);
        }
    }
}