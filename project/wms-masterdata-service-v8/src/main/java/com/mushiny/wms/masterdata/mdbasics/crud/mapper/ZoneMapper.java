package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ZoneDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Zone;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZoneMapper implements BaseMapper<ZoneDTO, Zone> {

    private final ApplicationContext applicationContext;
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;
    private final SectionMapper sectionMapper;
    private final SectionRepository sectionRepository;

    @Autowired
    public ZoneMapper(ApplicationContext applicationContext,
                      ClientMapper clientMapper,
                      ClientRepository clientRepository, SectionMapper sectionMapper, SectionRepository sectionRepository) {
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public ZoneDTO toDTO(Zone entity) {
        if (entity == null) {
            return null;
        }

        ZoneDTO dto = new ZoneDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSection(sectionMapper.toDTO(sectionRepository.retrieve(entity.getSectionId())));
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    @Override
    public Zone toEntity(ZoneDTO dto) {
        if (dto == null) {
            return null;
        }

        Zone entity = new Zone();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSectionId(dto.getSectionId());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ZoneDTO dto, Zone entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSectionId(dto.getSectionId());
        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
    }
}

