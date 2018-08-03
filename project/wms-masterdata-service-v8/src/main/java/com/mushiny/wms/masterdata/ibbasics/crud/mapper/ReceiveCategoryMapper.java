package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategory;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveDestinationRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiveCategoryMapper implements BaseMapper<ReceiveCategoryDTO, ReceiveCategory> {

    private final ReceiveDestinationRepository receivingDestinationRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveDestinationMapper receivingDestinationMapper;
    private final ClientMapper clientMapper;
    private final WarehouseMapper warehouseMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ReceiveCategoryMapper(ApplicationContext applicationContext,
                                 ClientMapper clientMapper,
                                 WarehouseMapper warehouseMapper,
                                 ReceiveDestinationRepository receivingDestinationRepository,
                                 ReceiveDestinationMapper receivingDestinationMapper,
                                 ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.warehouseMapper = warehouseMapper;
        this.receivingDestinationRepository = receivingDestinationRepository;
        this.receivingDestinationMapper = receivingDestinationMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ReceiveCategoryDTO toDTO(ReceiveCategory entity) {
        if (entity == null) {
            return null;
        }
        ReceiveCategoryDTO dto = new ReceiveCategoryDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setCategoryType(entity.getCategoryType());

        dto.setReceiveDestination(receivingDestinationMapper.toDTO(entity.getReceiveDestination()));
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReceiveCategory toEntity(ReceiveCategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveCategory entity = new ReceiveCategory();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setCategoryType(dto.getCategoryType());

        entity.setReceiveDestination(receivingDestinationRepository.retrieve(dto.getReceiveDestinationId()));
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveCategoryDTO dto, ReceiveCategory entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setCategoryType(dto.getCategoryType());

        entity.setReceiveDestination(receivingDestinationRepository.retrieve(dto.getReceiveDestinationId()));
    }
}
