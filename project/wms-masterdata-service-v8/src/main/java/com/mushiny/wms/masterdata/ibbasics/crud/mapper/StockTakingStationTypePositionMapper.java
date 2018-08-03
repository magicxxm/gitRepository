package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockTakingStationTypePositionMapper implements BaseMapper<StockTakingStationTypePositionDTO, StockTakingStationTypePosition> {

    private final ApplicationContext applicationContext;
    private final StockTakingStationTypeRepository stockTakingStationTypeRepository;
    private final StockTakingStationTypeMapper stockTakingStationTypeMapper;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public StockTakingStationTypePositionMapper(ApplicationContext applicationContext,
                                                StockTakingStationTypeRepository stockTakingStationTypeRepository,
                                                StockTakingStationTypeMapper stockTakingStationTypeMapper,
                                                WarehouseMapper warehouseMapper) {
        this.applicationContext = applicationContext;
        this.stockTakingStationTypeRepository = stockTakingStationTypeRepository;
        this.stockTakingStationTypeMapper = stockTakingStationTypeMapper;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    public StockTakingStationTypePositionDTO toDTO(StockTakingStationTypePosition entity) {
        if (entity == null) {
            return null;
        }
        StockTakingStationTypePositionDTO dto = new StockTakingStationTypePositionDTO(entity);
        dto.setPositionIndex(entity.getPositionIndex());
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setStockTakingStationType(stockTakingStationTypeMapper.toDTO(entity.getStockTakingStationType()));

        return dto;
    }

    @Override
    public StockTakingStationTypePosition toEntity(StockTakingStationTypePositionDTO dto) {
        if (dto == null) {
            return null;
        }
        StockTakingStationTypePosition entity = new StockTakingStationTypePosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setPositionIndex(dto.getPositionIndex());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getStationTypeId() != null) {
            entity.setStockTakingStationType(stockTakingStationTypeRepository.retrieve(dto.getStationTypeId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockTakingStationTypePositionDTO dto, StockTakingStationTypePosition entity) {
    }
}
