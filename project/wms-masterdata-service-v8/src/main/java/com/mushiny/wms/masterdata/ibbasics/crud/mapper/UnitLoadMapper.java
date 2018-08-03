package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.UnitLoadDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.UnitLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitLoadMapper implements BaseMapper<UnitLoadDTO, UnitLoad> {

    private final ApplicationContext applicationContext;

    @Autowired
    public UnitLoadMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public UnitLoadDTO toDTO(UnitLoad entity) {
        if (entity == null) {
            return null;
        }
        UnitLoadDTO dto=new UnitLoadDTO(entity);
        if(entity.getStationName()!=null){
            dto.setStationName(entity.getStationName());
        }else{
            dto.setStationName("");
        }

        return dto;
    }

    @Override
    public UnitLoad toEntity(UnitLoadDTO dto) {
        if (dto == null) {
            return null;
        }
        UnitLoad entity=new UnitLoad();
        entity.setId(dto.getId());
        entity.setStationName(dto.getStationName());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(UnitLoadDTO dto, UnitLoad entity) {

    }
}
