package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockTakingStationPositionMapper implements BaseMapper<StockTakingStationPositionDTO, StockTakingStationPosition> {

    private final ApplicationContext applicationContext;
    private final StockTakingStationRepository stockTakingStationRepository;
    private final StockTakingStationMapper stockTakingStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final WarehouseMapper warehouseMapper;

    @Autowired
    public StockTakingStationPositionMapper(ApplicationContext applicationContext,
                                            WarehouseMapper warehouseMapper,
                                            StockTakingStationRepository stockTakingStationRepository,
                                            StockTakingStationMapper stockTakingStationMapper,
                                            WorkStationPositionMapper workStationPositionMapper,
                                            WorkStationPositionRepository workStationPositionRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.stockTakingStationMapper = stockTakingStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.workStationPositionRepository = workStationPositionRepository;
    }

    @Override
    public StockTakingStationPositionDTO toDTO(StockTakingStationPosition entity) {
        if (entity == null) {
            return null;
        }
        StockTakingStationPositionDTO dto = new StockTakingStationPositionDTO(entity);
        dto.setPositionState(entity.getPositionState());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setWorkStationPosition(workStationPositionMapper.toDTO(entity.getWorkStationPosition()));
        dto.setStockTakingStation(stockTakingStationMapper.toDTO(entity.getStockTakingStation()));

        return dto;
    }

    @Override
    public StockTakingStationPosition toEntity(StockTakingStationPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        StockTakingStationPosition entity = new StockTakingStationPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionState(dto.getPositionState());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getWorkStationPositionId() != null) {
            entity.setWorkStationPosition(workStationPositionRepository.retrieve(dto.getWorkStationPositionId()));
        }
        if (dto.getStationId() != null) {
            entity.setStockTakingStation(stockTakingStationRepository.retrieve(dto.getStationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockTakingStationPositionDTO dto, StockTakingStationPosition entity) {
    }
}
