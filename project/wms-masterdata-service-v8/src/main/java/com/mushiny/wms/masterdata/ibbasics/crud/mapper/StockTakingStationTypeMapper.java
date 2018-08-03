package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationType;
import org.springframework.stereotype.Component;

@Component
public class StockTakingStationTypeMapper implements BaseMapper<StockTakingStationTypeDTO, StockTakingStationType> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;

    public StockTakingStationTypeMapper(ApplicationContext applicationContext,
                                        WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public StockTakingStationTypeDTO toDTO(StockTakingStationType entity) {
        if (entity == null) {
            return null;
        }
        StockTakingStationTypeDTO dto = new StockTakingStationTypeDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStationType(entity.getStationType());
        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }

    @Override
    public StockTakingStationType toEntity(StockTakingStationTypeDTO dto) {
        if (dto == null) {
            return null;
        }
        StockTakingStationType entity = new StockTakingStationType();

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStationType(dto.getStationType());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockTakingStationTypeDTO dto, StockTakingStationType entity) {

        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setStationType(dto.getStationType());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}
