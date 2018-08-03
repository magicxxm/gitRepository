package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReplenishStrategyDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReplenishStrategyMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReplenishStrategy;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReplenishStrategyRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReplenishStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReplenishStrategyServiceImpl implements ReplenishStrategyService {

    private final ReplenishStrategyRepository replenishStrategyRepository;
    private final ApplicationContext applicationContext;
    private final ReplenishStrategyMapper replenishStrategyMapper;

    @Autowired
    public ReplenishStrategyServiceImpl(ReplenishStrategyRepository replenishStrategyRepository,
                                        ApplicationContext applicationContext,
                                        ReplenishStrategyMapper replenishStrategyMapper) {
        this.replenishStrategyRepository = replenishStrategyRepository;
        this.applicationContext = applicationContext;
        this.replenishStrategyMapper = replenishStrategyMapper;
    }

    @Override
    public ReplenishStrategyDTO create(ReplenishStrategyDTO dto) {
        ReplenishStrategy entity = replenishStrategyMapper.toEntity(dto);
        checkReplenishStrategy(entity.getWarehouseId(), entity.getClientId());
        return replenishStrategyMapper.toDTO(replenishStrategyRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReplenishStrategy entity = replenishStrategyRepository.retrieve(id);
        replenishStrategyRepository.delete(entity);
    }

    @Override
    public ReplenishStrategyDTO update(ReplenishStrategyDTO dto) {
        ReplenishStrategy entity = replenishStrategyRepository.retrieve(dto.getId());
        replenishStrategyMapper.updateEntityFromDTO(dto, entity);
        return replenishStrategyMapper.toDTO(replenishStrategyRepository.save(entity));
    }

    @Override
    public ReplenishStrategyDTO retrieve(String id) {
        return replenishStrategyMapper.toDTO(replenishStrategyRepository.retrieve(id));
    }

    @Override
    public List<ReplenishStrategyDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReplenishStrategy> entities = replenishStrategyRepository.getBySearchTerm(searchTerm, sort);
        return replenishStrategyMapper.toDTOList(entities);
    }

    @Override
    public Page<ReplenishStrategyDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<ReplenishStrategy> entities = replenishStrategyRepository.getBySearchTerm(searchTerm, pageable);
        return replenishStrategyMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReplenishStrategyDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "modifiedDate"));
        applicationContext.isCurrentClient(clientId);
        List<ReplenishStrategy> entities = replenishStrategyRepository.getNotLockList(clientId, sort);
        return replenishStrategyMapper.toDTOList(entities);
    }

    private void checkReplenishStrategy(String warehouse, String client) {
        ReplenishStrategy strategy = replenishStrategyRepository.getByClient(warehouse, client);
        if (strategy != null) {
            throw new ApiException(
                    InBoundException.EX_MD_IN_REPLENISH_STRATEGY_UNIQUE.toString());
        }
    }
}
