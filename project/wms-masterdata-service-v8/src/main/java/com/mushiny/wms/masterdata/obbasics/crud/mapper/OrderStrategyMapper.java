package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OrderStrategyDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OrderStrategy;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderStrategyMapper implements BaseMapper<OrderStrategyDTO, OrderStrategy> {

    private final StorageLocationRepository storageLocationRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationMapper storageLocationMapper;
    private final ClientRepository clientRepository;
    private final WarehouseMapper warehouseMapper;

    public OrderStrategyMapper(StorageLocationRepository storageLocationRepository,
                               ApplicationContext applicationContext,
                               StorageLocationMapper storageLocationMapper,
                               ClientRepository clientRepository,
                               WarehouseMapper warehouseMapper) {
        this.storageLocationRepository = storageLocationRepository;
        this.applicationContext = applicationContext;
        this.storageLocationMapper = storageLocationMapper;
        this.clientRepository = clientRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public OrderStrategyDTO toDTO(OrderStrategy entity) {
        if (entity == null) {
            return null;
        }
        OrderStrategyDTO dto = new OrderStrategyDTO(entity);

        dto.setName(entity.getName());
        dto.setCreateShippingOrder(entity.isCreateShippingOrder());
        dto.setCreateFollowupPicks(entity.isCreateFollowupPicks());
        dto.setManualCreationIndex(entity.isManualCreationIndex());
        dto.setPreferMatchingStock(entity.isPreferMatchingStock());
        dto.setPreferUnopened(entity.isPreferUnopened());
        dto.setUseLockedLot(entity.isUseLockedLot());
        dto.setUseLockedStock(entity.isUseLockedStock());

        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));

        return dto;
    }

    @Override
    public OrderStrategy toEntity(OrderStrategyDTO dto) {
        if (dto == null) {
            return null;
        }
        OrderStrategy entity = new OrderStrategy();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setCreateShippingOrder(dto.isCreateShippingOrder());
        entity.setCreateFollowupPicks(dto.isCreateFollowupPicks());
        entity.setManualCreationIndex(dto.isManualCreationIndex());
        entity.setPreferMatchingStock(dto.isPreferMatchingStock());
        entity.setPreferUnopened(dto.isPreferUnopened());
        entity.setUseLockedLot(dto.isUseLockedLot());
        entity.setUseLockedStock(dto.isUseLockedStock());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getDefaultDestinationId() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getDefaultDestinationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OrderStrategyDTO dto, OrderStrategy entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setCreateShippingOrder(dto.isCreateShippingOrder());
        entity.setCreateFollowupPicks(dto.isCreateFollowupPicks());
        entity.setManualCreationIndex(dto.isManualCreationIndex());
        entity.setPreferMatchingStock(dto.isPreferMatchingStock());
        entity.setPreferUnopened(dto.isPreferUnopened());
        entity.setUseLockedLot(dto.isUseLockedLot());
        entity.setUseLockedStock(dto.isUseLockedStock());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        if (dto.getDefaultDestinationId() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getDefaultDestinationId()));
        } else {
            entity.setStorageLocation(null);
        }
    }
}
