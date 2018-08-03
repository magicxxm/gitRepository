package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.MapDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.repository.MapRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapMapper implements BaseMapper<MapDTO, Map> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final SectionMapper sectionMapper;
    private final SectionRepository sectionRepository;
    private final MapRepository mapRepository;

    @Autowired
    public MapMapper(ApplicationContext applicationContext,
                     ClientRepository clientRepository,
                     SectionMapper sectionMapper,
                     SectionRepository sectionRepository,
                     MapRepository mapRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
        this.mapRepository = mapRepository;
    }

    @Override
    public MapDTO toDTO(Map entity) {
        if (entity == null) {
            return null;
        }
        MapDTO dto = new MapDTO(entity);
        dto.setName(entity.getName());
        dto.setNodeSize(entity.getNodeSize());
        dto.setNumberOfColumns(entity.getNumberOfColumns());
        dto.setNumberOfRows(entity.getNumberOfRows());
        dto.setSection(sectionMapper.toDTO(sectionRepository.retrieve(entity.getSectionId())));
        dto.setActive(entity.isActive());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }


    @Override
    public Map toEntity(MapDTO dto) {
        if (dto == null) {
            return null;
        }
        Map entity = new Map();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNodeSize(dto.getNodeSize());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setSectionId(dto.getSectionId());
        entity.setActive(dto.isActive());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(MapDTO dto, Map entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setNodeSize(dto.getNodeSize());
        entity.setNumberOfColumns(dto.getNumberOfColumns());
        entity.setNumberOfRows(dto.getNumberOfRows());
        entity.setSectionId(dto.getSectionId());
        if(dto.isActive()) {
            String warehouseId = applicationContext.getCurrentWarehouse();
            mapRepository.updateActiveBySection(dto.getSectionId(),warehouseId);
        }
        entity.setActive(dto.isActive());
    }
}

