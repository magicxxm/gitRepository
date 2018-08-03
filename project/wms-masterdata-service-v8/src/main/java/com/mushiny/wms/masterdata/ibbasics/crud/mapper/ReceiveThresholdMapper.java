package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveThresholdMapper implements BaseMapper<ReceiveThresholdDTO, ReceiveThreshold> {

    private final ApplicationContext applicationContext;

    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ReceiveThresholdMapper(ApplicationContext applicationContext,
                                  WarehouseMapper warehouseMapper,
                                  ClientRepository clientRepository) {
        this.warehouseMapper = warehouseMapper;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public ReceiveThresholdDTO toDTO(ReceiveThreshold entity) {
        if (entity == null) {
            return null;
        }

        ReceiveThresholdDTO dto = new ReceiveThresholdDTO(entity);

        dto.setName(entity.getName());
        dto.setThreshold(entity.getThreshold());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setClientId(entity.getClientId());

        return dto;
    }

    @Override
    public ReceiveThreshold toEntity(ReceiveThresholdDTO dto) {
        if (dto == null) {
            return null;
        }

        ReceiveThreshold entity = new ReceiveThreshold();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setThreshold(dto.getThreshold());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveThresholdDTO dto, ReceiveThreshold entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setThreshold(dto.getThreshold());

    }
}

