package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import org.springframework.stereotype.Component;

@Component
public class BoxTypeMapper implements BaseMapper<BoxTypeDTO, BoxType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    public BoxTypeMapper(ApplicationContext applicationContext,
                         ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public BoxTypeDTO toDTO(BoxType entity) {

        if (entity == null) {
            return null;
        }
        BoxTypeDTO dto = new BoxTypeDTO(entity);

        dto.setGroup(entity.getGroup());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setThickness(entity.getThickness());
        dto.setVolume(entity.getVolume());
        dto.setWeight(entity.getWeight());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public BoxType toEntity(BoxTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        BoxType entity = new BoxType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setGroup(dto.getGroup());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setThickness(dto.getThickness());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setWeight(dto.getWeight());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(BoxTypeDTO dto, BoxType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setGroup(dto.getGroup());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setThickness(dto.getThickness());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setWeight(dto.getWeight());
    }
}
