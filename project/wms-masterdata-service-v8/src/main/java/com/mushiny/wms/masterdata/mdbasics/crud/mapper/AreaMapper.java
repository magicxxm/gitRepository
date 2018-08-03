package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper implements BaseMapper<AreaDTO, Area> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public AreaMapper(ApplicationContext applicationContext,
                      ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public AreaDTO toDTO(Area entity) {
        if (entity == null) {
            return null;
        }

        AreaDTO dto = new AreaDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setUseForGoodsIn(entity.isUseForGoodsIn());
        dto.setUseForGoodsOut(entity.isUseForGoodsOut());
        dto.setUseForPicking(entity.isUseForPicking());
        dto.setUseForReplenish(entity.isUseForReplenish());
        dto.setUseForStorage(entity.isUseForStorage());
        dto.setUseForTransfer(entity.isUseForTransfer());
        dto.setUseForReturn(entity.isUserForReturn());
        dto.setUseForTransport(entity.isUserForTransport());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }


    @Override
    public Area toEntity(AreaDTO dto) {
        if (dto == null) {
            return null;
        }

        Area entity = new Area();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUseForGoodsIn(dto.isUseForGoodsIn());
        entity.setUseForGoodsOut(dto.isUseForGoodsOut());
        entity.setUseForPicking(dto.isUseForPicking());
        entity.setUseForReplenish(dto.isUseForReplenish());
        entity.setUseForStorage(dto.isUseForStorage());
        entity.setUseForTransfer(dto.isUseForTransfer());
        entity.setUserForReturn(dto.isUseForReturn());
        entity.setUserForTransport(dto.isUseForTransport());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(AreaDTO dto, Area entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUseForGoodsIn(dto.isUseForGoodsIn());
        entity.setUseForGoodsOut(dto.isUseForGoodsOut());
        entity.setUseForPicking(dto.isUseForPicking());
        entity.setUseForReplenish(dto.isUseForReplenish());
        entity.setUseForStorage(dto.isUseForStorage());
        entity.setUseForTransfer(dto.isUseForTransfer());
        entity.setUserForReturn(dto.isUseForReturn());
        entity.setUserForTransport(dto.isUseForTransport());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
    }
}

