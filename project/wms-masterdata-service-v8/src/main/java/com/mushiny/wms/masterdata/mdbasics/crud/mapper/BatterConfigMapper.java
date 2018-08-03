package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.BatterConfigDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.BatterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatterConfigMapper implements BaseMapper<BatterConfigDTO, BatterConfig> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public BatterConfigMapper(ApplicationContext applicationContext,
                              ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public BatterConfigDTO toDTO(BatterConfig entity) {
        if (entity == null) {
            return null;
        }
        BatterConfigDTO dto = new BatterConfigDTO(entity);
        dto.setName(entity.getName());
        dto.setStartNumber(entity.getStartNumber());
        dto.setEndNumber(entity.getEndNumber());
        return dto;
    }


    @Override
    public BatterConfig toEntity(BatterConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        BatterConfig entity = new BatterConfig();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStartNumber(dto.getStartNumber());
        entity.setEndNumber(dto.getEndNumber());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(BatterConfigDTO dto, BatterConfig entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStartNumber(dto.getStartNumber());
        entity.setEndNumber(dto.getEndNumber());
    }
}

