package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.StockTakingStationBusiness;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StockTakingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StockTakingStationMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StockTakingStationPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StockTakingStationService;
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
public class StockTakingStationServiceImpl implements StockTakingStationService {

    private final StockTakingStationRepository stockTakingStationRepository;
    private final StockTakingStationMapper stockTakingStationMapper;
    private final StockTakingStationPositionMapper stockTakingStationPositionMapper;
    private final StockTakingStationBusiness stockTakingStationBusiness;

    @Autowired
    public StockTakingStationServiceImpl(StockTakingStationRepository stockTakingStationRepository,
                                         StockTakingStationMapper stockTakingStationMapper,
                                         StockTakingStationPositionMapper stockTakingStationPositionMapper,
                                         StockTakingStationBusiness stockTakingStationBusiness) {
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.stockTakingStationMapper = stockTakingStationMapper;
        this.stockTakingStationPositionMapper = stockTakingStationPositionMapper;
        this.stockTakingStationBusiness = stockTakingStationBusiness;
    }

    @Override
    public void createMore(StockTakingStationDTO dto) {
        checkPackingStationName(dto.getWarehouseId(), dto.getName());
        stockTakingStationBusiness.createMore(dto);
    }

    @Override
    public StockTakingStationDTO create(StockTakingStationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {
        StockTakingStation entity = stockTakingStationRepository.retrieve(id);
        if (entity.getOperatorId() != null) {
            throw new ApiException("该状态的工作站不能删除");
        }
        stockTakingStationRepository.delete(entity);
    }

    @Override
    public StockTakingStationDTO update(StockTakingStationDTO dto) {
        StockTakingStation entity = stockTakingStationRepository.retrieve(dto.getId());
        if (entity.getOperatorId() != null) {
            throw new ApiException("该状态的工作站不能更新");
        }
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkPackingStationName(entity.getWarehouseId(), dto.getName());
        }
        stockTakingStationBusiness.upDateMore(dto);
        return stockTakingStationMapper.toDTO(entity);
    }

    @Override
    public StockTakingStationDTO retrieve(String id) {
        StockTakingStation entity = stockTakingStationRepository.retrieve(id);
        StockTakingStationDTO dto = stockTakingStationMapper.toDTO(entity);
        dto.setPositions(stockTakingStationPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<StockTakingStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<StockTakingStation> entities = stockTakingStationRepository.getNotLockList(null, sort);
        return stockTakingStationMapper.toDTOList(entities);
    }

    @Override
    public List<StockTakingStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StockTakingStation> entities = stockTakingStationRepository.getBySearchTerm(searchTerm, sort);
        return stockTakingStationMapper.toDTOList(entities);
    }

    @Override
    public Page<StockTakingStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StockTakingStation> entities = stockTakingStationRepository.getBySearchTerm(searchTerm, pageable);
        return stockTakingStationMapper.toDTOPage(pageable, entities);
    }

    private void checkPackingStationName(String warehouse, String name) {
        StockTakingStation packingStation = stockTakingStationRepository.getByName(warehouse, name);
        if (packingStation != null) {
            throw new ApiException(InBoundException.EX_MD_IN_STOCKING_TAKING_STATION_NAME_UNIQUE.toString(), name);
        }
    }
}
