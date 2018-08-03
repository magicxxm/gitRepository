package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStationType;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StowStationTypeMapper implements BaseMapper<StowStationTypeDTO, StowStationType> {

    private final ApplicationContext applicationContext;

    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public StowStationTypeMapper(ApplicationContext applicationContext,
                                 WarehouseMapper warehouseMapper,
                                 ClientRepository clientRepository) {
        this.warehouseMapper = warehouseMapper;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public StowStationTypeDTO toDTO(StowStationType entity) {
        if (entity == null) {
            return null;
        }

        StowStationTypeDTO dto = new StowStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public StowStationType toEntity(StowStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        StowStationType entity = new StowStationType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(StowStationTypeDTO dto, StowStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());;
    }
}

