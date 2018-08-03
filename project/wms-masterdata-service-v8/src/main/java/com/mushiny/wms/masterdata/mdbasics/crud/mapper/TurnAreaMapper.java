package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TurnAreaMapper implements BaseMapper<TurnAreaDTO, TurnArea> {

    private final ApplicationContext applicationContext;

    @Autowired
    public TurnAreaMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;;
    }

    @Override
    public TurnAreaDTO toDTO(TurnArea entity) {
        if (entity == null) {
            return null;
        }

        TurnAreaDTO dto = new TurnAreaDTO(entity);
        dto.setName(entity.getName());
        dto.setStationId(entity.getStation());
        dto.setMapId(entity.getMap());
        dto.setSectionId(entity.getSection());

        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }


    @Override
    public TurnArea toEntity(TurnAreaDTO dto) {
        if (dto == null) {
            return null;
        }

        TurnArea entity = new TurnArea();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStation(dto.getStationId());
        entity.setMap(dto.getMapId());
        entity.setSection(dto.getSectionId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(TurnAreaDTO dto, TurnArea entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStation(dto.getStationId());
        entity.setMap(dto.getMapId());
        entity.setSection(dto.getSectionId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}

