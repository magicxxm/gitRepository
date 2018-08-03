package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWallType;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackFieldTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PickPackWallTypeMapper implements BaseMapper<PickPackWallTypeDTO, PickPackWallType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    public PickPackWallTypeMapper(ApplicationContext applicationContext,
                                  ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public PickPackWallTypeDTO toDTO(PickPackWallType entity) {

        if (entity == null) {
            return null;
        }
        PickPackWallTypeDTO dto = new PickPackWallTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public PickPackWallType toEntity(PickPackWallTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PickPackWallType entity = new PickPackWallType();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(PickPackWallTypeDTO dto, PickPackWallType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}
