package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationTypeDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationType;
import org.springframework.stereotype.Component;

@Component
public class SortingStationTypeMapper implements BaseMapper<SortingStationTypeDTO, SortingStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public SortingStationTypeMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public SortingStationTypeDTO toDTO(SortingStationType entity) {
        if (entity == null) {
            return null;
        }
        SortingStationTypeDTO dto = new SortingStationTypeDTO(entity);
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWarehouse(entity.getWarehouseId());
        return dto;
    }

    @Override
    public SortingStationType toEntity(SortingStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        SortingStationType entity = new SortingStationType();
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(SortingStationTypeDTO dto, SortingStationType entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
