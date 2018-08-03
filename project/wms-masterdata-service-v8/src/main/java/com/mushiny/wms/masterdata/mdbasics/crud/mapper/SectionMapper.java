package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.SectionDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper implements BaseMapper<SectionDTO, Section> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public SectionMapper(ApplicationContext applicationContext,
                         ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }

    @Override
    public SectionDTO toDTO(Section entity) {
        if (entity == null) {
            return null;
        }
        SectionDTO dto = new SectionDTO(entity);
        dto.setName(entity.getName());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setDescription(entity.getDescription());
        return dto;
    }


    @Override
    public Section toEntity(SectionDTO dto) {
        if (dto == null) {
            return null;
        }
        Section entity = new Section();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(SectionDTO dto, Section entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
}

