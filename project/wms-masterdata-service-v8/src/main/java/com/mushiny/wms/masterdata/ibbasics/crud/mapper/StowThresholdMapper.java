package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowThreshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StowThresholdMapper implements BaseMapper<StowThresholdDTO, StowThreshold> {

    private final ApplicationContext applicationContext;

    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public StowThresholdMapper(ApplicationContext applicationContext,
                               WarehouseMapper warehouseMapper,
                               ClientRepository clientRepository) {
        this.warehouseMapper = warehouseMapper;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public StowThresholdDTO toDTO(StowThreshold entity) {
        if (entity == null) {
            return null;
        }

        StowThresholdDTO dto = new StowThresholdDTO(entity);

        dto.setName(entity.getName());
        dto.setThreshold(entity.getThreshold());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setClientId(entity.getClientId());

        return dto;
    }

    @Override
    public StowThreshold toEntity(StowThresholdDTO dto) {
        if (dto == null) {
            return null;
        }

        StowThreshold entity = new StowThreshold();

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
    public void updateEntityFromDTO(StowThresholdDTO dto, StowThreshold entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setThreshold(dto.getThreshold());

    }
}

