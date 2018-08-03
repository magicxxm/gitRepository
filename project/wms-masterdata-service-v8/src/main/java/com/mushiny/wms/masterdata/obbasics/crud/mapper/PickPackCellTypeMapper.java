package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCellType;
import org.springframework.stereotype.Component;

@Component
public class PickPackCellTypeMapper implements BaseMapper<PickPackCellTypeDTO, PickPackCellType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    public PickPackCellTypeMapper(ApplicationContext applicationContext,
                                  ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public PickPackCellTypeDTO toDTO(PickPackCellType entity) {

        if (entity == null) {
            return null;
        }
        PickPackCellTypeDTO dto = new PickPackCellTypeDTO(entity);
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setVolume(entity.getVolume());
        dto.setLiftingCapacity(entity.getLiftingCapacity());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PickPackCellType toEntity(PickPackCellTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PickPackCellType entity = new PickPackCellType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setLiftingCapacity(dto.getLiftingCapacity());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackCellTypeDTO dto, PickPackCellType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setHeight(dto.getHeight());
        entity.setWidth(dto.getWidth());
        entity.setDepth(dto.getDepth());
        entity.setVolume(dto.getWidth().multiply(dto.getDepth()).multiply(dto.getHeight()));
        entity.setLiftingCapacity(dto.getLiftingCapacity());
    }
}
