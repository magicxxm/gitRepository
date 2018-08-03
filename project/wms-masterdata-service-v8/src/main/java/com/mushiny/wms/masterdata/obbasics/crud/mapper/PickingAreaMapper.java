package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingAreaDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingArea;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PickingAreaMapper implements BaseMapper<PickingAreaDTO, PickingArea> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public PickingAreaMapper(ApplicationContext applicationContext,
                             WarehouseMapper warehouseMapper,
                             ClientMapper clientMapper,
                             ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public PickingAreaDTO toDTO(PickingArea entity) {
        if (entity == null) {
            return null;
        }
        PickingAreaDTO dto = new PickingAreaDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PickingArea toEntity(PickingAreaDTO dto) {
        if (dto == null) {
            return null;
        }
        PickingArea entity = new PickingArea();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickingAreaDTO dto, PickingArea entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
