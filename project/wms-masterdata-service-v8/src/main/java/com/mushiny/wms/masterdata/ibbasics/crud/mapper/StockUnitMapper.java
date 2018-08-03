package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockUnitDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockUnitMapper implements BaseMapper<StockUnitDTO, StockUnit> {

    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationMapper storageLocationMapper;
    private final ItemDataMapper itemDataMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public StockUnitMapper(ItemDataRepository itemDataRepository,
                           StorageLocationRepository storageLocationRepository,
                           ApplicationContext applicationContext,
                           StorageLocationMapper storageLocationMapper,
                           ItemDataMapper itemDataMapper,
                           ClientRepository clientRepository) {
        this.itemDataRepository = itemDataRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.applicationContext = applicationContext;
        this.storageLocationMapper = storageLocationMapper;
        this.itemDataMapper = itemDataMapper;
        this.clientRepository = clientRepository;
    }


    @Override
    public StockUnitDTO toDTO(StockUnit entity) {
        if (entity == null) {
            return null;
        }
        StockUnitDTO dto = new StockUnitDTO(entity);

        dto.setAmount(entity.getAmount());
        dto.setReservedAmount(entity.getReservedAmount());
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
//        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public StockUnit toEntity(StockUnitDTO dto) {
        if (dto == null) {
            return null;
        }
        StockUnit entity = new StockUnit();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAmount(dto.getAmount());
        entity.setReservedAmount(dto.getReservedAmount());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockUnitDTO dto, StockUnit entity) {
    }
}
