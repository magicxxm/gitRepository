package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataSerialNumberDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataSerialNumber;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemDataSerialNumberMapper implements BaseMapper<ItemDataSerialNumberDTO, ItemDataSerialNumber> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public ItemDataSerialNumberMapper(ApplicationContext applicationContext,
                                      ClientRepository clientRepository){
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public ItemDataSerialNumberDTO toDTO(ItemDataSerialNumber entity) {
        if (entity == null) {
            return null;
        }

        ItemDataSerialNumberDTO dto = new ItemDataSerialNumberDTO(entity);
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setItemdata(entity.getItemData());
        dto.setSerialNo(entity.getSerialNo());
        return dto;
    }

    @Override
    public ItemDataSerialNumber toEntity(ItemDataSerialNumberDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemDataSerialNumber entity = new ItemDataSerialNumber();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        entity.setItemData(dto.getItemdata());
        entity.setSerialNo(dto.getSerialNo());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ItemDataSerialNumberDTO dto, ItemDataSerialNumber entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setItemData(dto.getItemdata());
        entity.setSerialNo(dto.getSerialNo());
    }
}

