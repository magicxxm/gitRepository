package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationTypePositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockTakingStationBusiness {

    private final ApplicationContext applicationContext;
    private final StockTakingStationRepository stockTakingStationRepository;
    private final StockTakingStationPositionRepository stockTakingStationPositionRepository;
    private final StockTakingStationTypeRepository stockTakingStationTypeRepository;
    private final StockTakingStationTypePositionRepository stockTakingStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public StockTakingStationBusiness(ApplicationContext applicationContext,
                                      StockTakingStationRepository stockTakingStationRepository,
                                      StockTakingStationPositionRepository stockTakingStationPositionRepository,
                                      StockTakingStationTypeRepository stockTakingStationTypeRepository,
                                      StockTakingStationTypePositionRepository stockTakingStationTypePositionRepository,
                                      WorkStationRepository workStationRepository,
                                      WorkStationPositionRepository workStationPositionRepository,
                                      UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.stockTakingStationPositionRepository = stockTakingStationPositionRepository;
        this.stockTakingStationTypeRepository = stockTakingStationTypeRepository;
        this.stockTakingStationTypePositionRepository = stockTakingStationTypePositionRepository;

        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(StockTakingStationDTO dto) {
        StockTakingStationType stockTakingStationType = stockTakingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<StockTakingStationTypePosition> stockTakingStationTypePositions = stockTakingStationTypePositionRepository.getByStockTakingStationType(stockTakingStationType);

        StockTakingStation station = new StockTakingStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setStockTakingStationType(stockTakingStationTypeRepository.retrieve(dto.getTypeId()));
        if (dto.getOperatorId() != null) {
            station.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperatorId(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = stockTakingStationRepository.save(station);
        for (StockTakingStationTypePosition StationTypePosition : stockTakingStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(InBoundException.EX_MD_IN_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            StockTakingStationPosition stockTakingStationPosition = new StockTakingStationPosition();
            stockTakingStationPosition.setWorkStationPosition(workStationPosition);
            stockTakingStationPosition.setPositionState(positionState);
            stockTakingStationPosition.setStockTakingStation(station);
            stockTakingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            stockTakingStationPositionRepository.save(stockTakingStationPosition);
        }
    }

    public StockTakingStation upDateMore(StockTakingStationDTO dto) {
        StockTakingStation entity = stockTakingStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setStockTakingStationType(stockTakingStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setStockTakingStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
        StockTakingStationType stockTakingStationType = stockTakingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<StockTakingStationTypePosition> stockTakingStationTypePositions = stockTakingStationTypePositionRepository.getByStockTakingStationType(stockTakingStationType);

        for (StockTakingStationTypePosition StationTypePosition1 : stockTakingStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(InBoundException.EX_MD_IN_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            List<StockTakingStationPosition> stockTakingStationPositionList = stockTakingStationPositionRepository.getByStockTakingStationId(dto.getId());
            for (StockTakingStationPosition stockTakingStationPosition : stockTakingStationPositionList) {
                stockTakingStationPositionRepository.delete(stockTakingStationPosition);
            }
            StockTakingStationPosition stockTakingStationPosition = new StockTakingStationPosition();
            stockTakingStationPosition.setWorkStationPosition(workStationPosition);
            stockTakingStationPosition.setPositionState(positionState);
            stockTakingStationPosition.setStockTakingStation(entity);
            stockTakingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            stockTakingStationPositionRepository.save(stockTakingStationPosition);
        }
        return stockTakingStationRepository.save(entity);
    }

}
