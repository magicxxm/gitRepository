package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.CellIndexDTO;
import com.mushiny.wms.masterdata.obbasics.domain.CellIndex;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class CellIndexMapper implements BaseMapper<CellIndexDTO, CellIndex> {

    private final ApplicationContext applicationContext;
    private final PickPackWallTypeRepository pickPackWallTypeRepository;
    private final PickPackWallTypeMapper pickPackWallTypeMapper;

    public CellIndexMapper(ApplicationContext applicationContext, PickPackWallTypeRepository pickPackWallTypeRepository, PickPackWallTypeMapper pickPackWallTypeMapper) {
        this.applicationContext = applicationContext;
        this.pickPackWallTypeRepository = pickPackWallTypeRepository;
        this.pickPackWallTypeMapper = pickPackWallTypeMapper;
    }


    @Override
    public CellIndexDTO toDTO(CellIndex entity) {
        if (entity == null) {
            return null;
        }
        CellIndexDTO dto = new CellIndexDTO(entity);
        dto.setNumber(entity.getNumber());
        dto.setCellIndex(entity.getCellIndex());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setPickPackWallType(pickPackWallTypeMapper.toDTO(entity.getPickPackWallType1()));
        return dto;
    }

    @Override
    public CellIndex toEntity(CellIndexDTO dto) {
        CellIndex entity = new CellIndex();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setNumber(dto.getNumber());
        entity.setCellIndex(dto.getCellIndex());
        entity.setPickPackWallType1(pickPackWallTypeMapper.toEntity(dto.getPickPackWallType()));
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(CellIndexDTO dto, CellIndex entity) {

    }
}
