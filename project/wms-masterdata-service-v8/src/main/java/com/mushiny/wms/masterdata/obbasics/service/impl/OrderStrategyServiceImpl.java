package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OrderStrategyDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.OrderStrategyMapper;
import com.mushiny.wms.masterdata.obbasics.domain.OrderStrategy;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OrderStrategyRepository;
import com.mushiny.wms.masterdata.obbasics.service.OrderStrategyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderStrategyServiceImpl implements OrderStrategyService {

    private final OrderStrategyRepository orderStrategyRepository;
    private final ApplicationContext applicationContext;
    private final OrderStrategyMapper orderStrategyMapper;

    public OrderStrategyServiceImpl(OrderStrategyRepository orderStrategyRepository,
                                    ApplicationContext applicationContext,
                                    OrderStrategyMapper orderStrategyMapper) {
        this.orderStrategyRepository = orderStrategyRepository;
        this.applicationContext = applicationContext;
        this.orderStrategyMapper = orderStrategyMapper;
    }

    @Override
    public OrderStrategyDTO create(OrderStrategyDTO dto) {
        OrderStrategy entity = orderStrategyMapper.toEntity(dto);
        checkOrderStrategyName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return orderStrategyMapper.toDTO(orderStrategyRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OrderStrategy entity = orderStrategyRepository.retrieve(id);
        orderStrategyRepository.delete(entity);
    }

    @Override
    public OrderStrategyDTO update(OrderStrategyDTO dto) {
        OrderStrategy entity = orderStrategyRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkOrderStrategyName(entity.getWarehouseId(), entity.getClientId(), dto.getName());
        }
        orderStrategyMapper.updateEntityFromDTO(dto, entity);
        return orderStrategyMapper.toDTO(orderStrategyRepository.save(entity));
    }

    @Override
    public OrderStrategyDTO retrieve(String id) {
        return orderStrategyMapper.toDTO(orderStrategyRepository.retrieve(id));
    }

    @Override
    public List<OrderStrategyDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<OrderStrategy> entities = orderStrategyRepository.getNotLockList(clientId, sort);
        return orderStrategyMapper.toDTOList(entities);
    }

    @Override
    public List<OrderStrategyDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OrderStrategy> entities = orderStrategyRepository.getBySearchTerm(searchTerm, sort);
        return orderStrategyMapper.toDTOList(entities);
    }

    @Override
    public Page<OrderStrategyDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<OrderStrategy> entities = orderStrategyRepository.getBySearchTerm(searchTerm, pageable);
        return orderStrategyMapper.toDTOPage(pageable, entities);
    }

    private void checkOrderStrategyName(String warehouse, String client, String name) {
        OrderStrategy orderStrategy = orderStrategyRepository.getByName(warehouse, client, name);
        if (orderStrategy != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_ORDER_STRATEGY_NAME_UNIQUE.toString(), name);
        }
    }
}
