package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.CarrierDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Carrier;
import org.springframework.stereotype.Component;

@Component
public class CarrierMapper implements BaseMapper<CarrierDTO, Carrier> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    public CarrierMapper(ApplicationContext applicationContext,
                         ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public CarrierDTO toDTO(Carrier entity) {

        if (entity == null) {
            return null;
        }
        CarrierDTO dto = new CarrierDTO(entity);
        dto.setName(entity.getName());
        dto.setCarrierNo(entity.getCarrier());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Carrier toEntity(CarrierDTO dto) {
        if (dto == null) {
            return null;
        }
        Carrier entity = new Carrier();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setCarrier(dto.getCarrierNo());
        entity.setName(dto.getName());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(CarrierDTO dto, Carrier entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setCarrier(dto.getCarrierNo());
        entity.setName(dto.getName());
    }
}
