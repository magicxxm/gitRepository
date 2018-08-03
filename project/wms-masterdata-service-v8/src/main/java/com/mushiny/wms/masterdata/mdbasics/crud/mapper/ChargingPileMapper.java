package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ChargingPileDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.ChargingPile;
import com.mushiny.wms.masterdata.mdbasics.repository.MapRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargingPileMapper implements BaseMapper<ChargingPileDTO, ChargingPile> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final MapMapper mapMapper;
    private final MapRepository mapRepository;
    private final SectionMapper sectionMapper;
    private final SectionRepository sectionRepository;

    @Autowired
    public ChargingPileMapper(ApplicationContext applicationContext,
                              ClientRepository clientRepository, MapMapper mapMapper, MapRepository mapRepository, SectionMapper sectionMapper, SectionRepository sectionRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.mapMapper = mapMapper;
        this.mapRepository = mapRepository;
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
    }


    @Override
    public ChargingPileDTO toDTO(ChargingPile entity) {
        if (entity == null) {
            return null;
        }
        ChargingPileDTO dto = new ChargingPileDTO(entity);
        dto.setName(entity.getName());
        dto.setPlaceMark(entity.getPlaceMark());
        dto.setToWard(entity.getToWard());
        dto.setState(entity.getState());
        if (entity.getSection() != null) {
            dto.setSection(sectionMapper.toDTO(entity.getSection()));
        } else {
            dto.setSection(null);
        }
        dto.setChargerId(entity.getChargerId());
        dto.setChargerType(entity.getChargerType());
        return dto;
    }

    @Override
    public ChargingPile toEntity(ChargingPileDTO dto) {
        if (dto == null) {
            return null;
        }
        ChargingPile entity = new ChargingPile();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setName(dto.getName());
        entity.setPlaceMark(dto.getPlaceMark());
        entity.setToWard(dto.getToWard());
        entity.setState(dto.getState());
        if (dto.getSectionId() != null) {
            entity.setSection(sectionRepository.retrieve(dto.getSectionId()));
        }
        entity.setChargerId(dto.getChargerId());
        entity.setChargerType(dto.getChargerType());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(ChargingPileDTO dto, ChargingPile entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setName(dto.getName());
        entity.setPlaceMark(dto.getPlaceMark());
        entity.setState(dto.getState());
        entity.setToWard(dto.getToWard());

        if (dto.getSectionId() != null) {
            entity.setSection(sectionRepository.retrieve(dto.getSectionId()));
        }
        entity.setChargerId(dto.getChargerId());
        entity.setChargerType(dto.getChargerType());
    }
}

