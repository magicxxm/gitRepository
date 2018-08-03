package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotTypeDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.RobotType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotTypeMapper implements BaseMapper<RobotTypeDTO, RobotType> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public RobotTypeMapper(ApplicationContext applicationContext,
                           ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public RobotTypeDTO toDTO(RobotType entity) {
        if (entity == null) {
            return null;
        }
        RobotTypeDTO dto = new RobotTypeDTO(entity);
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setAdditionalContent(entity.getAdditionalContent());
        return dto;
    }


    @Override
    public RobotType toEntity(RobotTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        RobotType entity = new RobotType();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(RobotTypeDTO dto, RobotType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
    }
}

