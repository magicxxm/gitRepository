package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationTypeDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationTypePositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StockTakingStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StockTakingStationTypeMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StockTakingStationTypePositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationType;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStationTypePosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationTypeRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StockTakingStationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StockTakingStationTypeServiceImpl implements StockTakingStationTypeService {

    private final StockTakingStationTypeRepository stockTakingStationTypeRepository;
    private final StockTakingStationTypeMapper stockTakingStationTypeMapper;
    private final StockTakingStationTypePositionMapper stockTakingStationTypePositionMapper;
    private final StockTakingStationRepository stockTakingStationRepository;
    private final StockTakingStationMapper stockTakingStationMapper;

    @Autowired
    public StockTakingStationTypeServiceImpl(StockTakingStationTypeRepository stockTakingStationTypeRepository,
                                             StockTakingStationTypeMapper stockTakingStationTypeMapper,
                                             StockTakingStationTypePositionMapper stockTakingStationTypePositionMapper, StockTakingStationRepository stockTakingStationRepository, StockTakingStationMapper stockTakingStationMapper) {
        this.stockTakingStationTypeRepository = stockTakingStationTypeRepository;
        this.stockTakingStationTypeMapper = stockTakingStationTypeMapper;
        this.stockTakingStationTypePositionMapper = stockTakingStationTypePositionMapper;
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.stockTakingStationMapper = stockTakingStationMapper;
    }

    @Override
    public StockTakingStationTypeDTO create(StockTakingStationTypeDTO dto) {
        StockTakingStationType entity = stockTakingStationTypeMapper.toEntity(dto);
        checkPackingStationTypeName(entity.getWarehouseId(), entity.getName());
        for (StockTakingStationTypePositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(stockTakingStationTypePositionMapper.toEntity(positionDTO));
        }
        return stockTakingStationTypeMapper.toDTO(stockTakingStationTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        StockTakingStationType entity = stockTakingStationTypeRepository.retrieve(id);
        stockTakingStationTypeRepository.delete(entity);
    }

    @Override
    public StockTakingStationTypeDTO update(StockTakingStationTypeDTO dto) {
        StockTakingStationType entity = stockTakingStationTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationTypeName(entity.getWarehouseId(), dto.getName());
        }
        stockTakingStationTypeMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<StockTakingStationTypePosition> positions = stockTakingStationTypePositionMapper.toEntityList(dto.getPositions());
        for (StockTakingStationTypePosition position : positions) {
            entity.addPosition(position);
        }
        return stockTakingStationTypeMapper.toDTO(stockTakingStationTypeRepository.save(entity));
    }

    @Override
    public StockTakingStationTypeDTO retrieve(String id) {
        StockTakingStationType entity = stockTakingStationTypeRepository.retrieve(id);
        StockTakingStationTypeDTO dto = stockTakingStationTypeMapper.toDTO(entity);
        dto.setPositions(stockTakingStationTypePositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<StockTakingStationTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<StockTakingStationType> entities = stockTakingStationTypeRepository.getNotLockList(null, sort);
        return stockTakingStationTypeMapper.toDTOList(entities);
    }

    @Override
    public List<StockTakingStationTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StockTakingStationType> entities = stockTakingStationTypeRepository.getBySearchTerm(searchTerm, sort);
        return stockTakingStationTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<StockTakingStationTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StockTakingStationType> entities = stockTakingStationTypeRepository.getBySearchTerm(searchTerm, pageable);
        Page<StockTakingStationTypeDTO> dtoPage=stockTakingStationTypeMapper.toDTOPage(pageable, entities);
        if(dtoPage.getContent()!=null){
         for(StockTakingStationTypeDTO dto:dtoPage){
             List<StockTakingStation> stockTakingStations=stockTakingStationRepository.getByStockTakingStationTypeId(dto.getId());
             dto.setStockTakingStations(stockTakingStationMapper.toDTOList(stockTakingStations));
         }
        }
        return dtoPage;
    }

    private void checkPackingStationTypeName(String warehouse, String name) {
        StockTakingStationType stockTakingStationType = stockTakingStationTypeRepository.getByName(warehouse, name);
        if (stockTakingStationType != null) {
            throw new ApiException(InBoundException.EX_MD_IN_STOCK_TAKING_STATION_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
