package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import org.springframework.stereotype.Component;

@Component
public class StockTakingStationMapper implements BaseMapper<StockTakingStationDTO, StockTakingStation> {

    private final ApplicationContext applicationContext;
    private final WarehouseMapper warehouseMapper;
    private final StockTakingStationTypeMapper stockTakingStationTypeMapper;
    private final StockTakingStationTypeRepository stockTakingStationTypeRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;

    public StockTakingStationMapper(ApplicationContext applicationContext,
                                    WarehouseMapper warehouseMapper,
                                    StockTakingStationTypeMapper stockTakingStationTypeMapper,
                                    StockTakingStationTypeRepository stockTakingStationTypeRepository,
                                    UserMapper userMapper,
                                    UserRepository userRepository,
                                    WorkStationMapper workStationMapper,
                                    WorkStationRepository workStationRepository) {
        this.applicationContext = applicationContext;
        this.warehouseMapper = warehouseMapper;
        this.stockTakingStationTypeMapper = stockTakingStationTypeMapper;
        this.stockTakingStationTypeRepository = stockTakingStationTypeRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
    }

    @Override
    public StockTakingStationDTO toDTO(StockTakingStation entity) {
        if (entity == null) {
            return null;
        }
        StockTakingStationDTO dto = new StockTakingStationDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWarehouseId(entity.getWarehouseId());
//        dto.setIbpStationType(stockTakingStationTypeMapper.toDTO(entity.getIbpStationType()));
        dto.setStockTakingStationType(stockTakingStationTypeMapper.toDTO(entity.getStockTakingStationType()));
        dto.setUser(userMapper.toDTO(entity.getOperatorId()));
        dto.setWorkstation(workStationMapper.toDTO(entity.getWorkStation()));

        return dto;
    }

    @Override
    public StockTakingStation toEntity(StockTakingStationDTO dto) {
        if (dto == null) {
            return null;
        }
        StockTakingStation entity = new StockTakingStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getTypeId() != null) {
            entity.setStockTakingStationType(stockTakingStationTypeRepository.retrieve(dto.getTypeId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(StockTakingStationDTO dto, StockTakingStation entity) {
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
        if (dto.getOperatorId() != null) {
            entity.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        } else {
            entity.setOperatorId(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
    }
}
