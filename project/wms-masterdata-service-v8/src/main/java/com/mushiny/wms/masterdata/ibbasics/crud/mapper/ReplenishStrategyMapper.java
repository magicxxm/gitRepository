package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReplenishStrategyDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReplenishStrategyMapper implements BaseMapper<ReplenishStrategyDTO, ReplenishStrategy> {


    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public ReplenishStrategyMapper(ApplicationContext applicationContext,
                                   ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }


    @Override
    public ReplenishStrategyDTO toDTO(ReplenishStrategy entity) {
        if (entity == null) {
            return null;
        }
        ReplenishStrategyDTO dto = new ReplenishStrategyDTO(entity);

        dto.setShipmentDay(entity.getShipmentDay());
        dto.setUnitsShipment(entity.getUnitsShipment());
        dto.setReplenishTrigger(entity.getReplenishTrigger());
        dto.setFudStrategy(entity.getFudStrategy());
        dto.setReplenishPadTime(entity.getReplenishPadTime());
        dto.setSkuMaxType(entity.getSkuMaxType());
        dto.setReceiveIsReplenish(entity.getReceiveIsReplenish());
        dto.setReceiveIsReplenishCondition(entity.getReceiveIsReplenishCondition());
        dto.setReplenishCount(entity.getReplenishCount());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public ReplenishStrategy toEntity(ReplenishStrategyDTO dto) {
        if (dto == null) {
            return null;
        }
        ReplenishStrategy entity = new ReplenishStrategy();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setShipmentDay(dto.getShipmentDay());
        entity.setUnitsShipment(dto.getUnitsShipment());
        entity.setReplenishTrigger(dto.getReplenishTrigger());
        entity.setFudStrategy(dto.getFudStrategy());
        entity.setReplenishPadTime(dto.getReplenishPadTime());
        entity.setSkuMaxType(dto.getSkuMaxType());
        entity.setReceiveIsReplenish(dto.getReceiveIsReplenish());
        entity.setReceiveIsReplenishCondition(dto.getReceiveIsReplenishCondition());
        entity.setReplenishCount(dto.getReplenishCount());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReplenishStrategyDTO dto, ReplenishStrategy entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setShipmentDay(dto.getShipmentDay());
        entity.setUnitsShipment(dto.getUnitsShipment());
        entity.setReplenishTrigger(dto.getReplenishTrigger());
        entity.setFudStrategy(dto.getFudStrategy());
        entity.setReplenishPadTime(dto.getReplenishPadTime());
        entity.setSkuMaxType(dto.getSkuMaxType());
        entity.setReceiveIsReplenish(dto.getReceiveIsReplenish());
        entity.setReceiveIsReplenishCondition(dto.getReceiveIsReplenishCondition());
        entity.setReplenishCount(dto.getReplenishCount());
    }
}
