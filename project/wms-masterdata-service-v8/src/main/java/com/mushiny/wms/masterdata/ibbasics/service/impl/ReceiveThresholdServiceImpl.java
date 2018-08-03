package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveThresholdMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveThresholdRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveThresholdService;
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
public class ReceiveThresholdServiceImpl implements ReceiveThresholdService {

    private final ReceiveThresholdRepository receiveThresholdRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveThresholdMapper receiveThresholdMapper;

    @Autowired
    public ReceiveThresholdServiceImpl(ReceiveThresholdRepository receiveThresholdRepository,
                                       ApplicationContext applicationContext,
                                       ReceiveThresholdMapper receiveThresholdMapper) {
        this.receiveThresholdRepository = receiveThresholdRepository;
        this.applicationContext = applicationContext;
        this.receiveThresholdMapper = receiveThresholdMapper;
    }

    @Override
    public ReceiveThresholdDTO create(ReceiveThresholdDTO dto) {
        ReceiveThreshold entity = receiveThresholdMapper.toEntity(dto);
        checkAreaName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return receiveThresholdMapper.toDTO(receiveThresholdRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReceiveThreshold entity = receiveThresholdRepository.retrieve(id);
        receiveThresholdRepository.delete(entity);
    }

    @Override
    public ReceiveThresholdDTO update(ReceiveThresholdDTO dto) {
        ReceiveThreshold entity = receiveThresholdRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName())
                && entity.getClientId().equalsIgnoreCase(dto.getClientId()))) {
            String client = dto.getClientId();
            checkAreaName(entity.getWarehouseId(), client, dto.getName());
        }
        receiveThresholdMapper.updateEntityFromDTO(dto, entity);
        return receiveThresholdMapper.toDTO(receiveThresholdRepository.save(entity));
    }

    @Override
    public ReceiveThresholdDTO retrieve(String id) {
        return receiveThresholdMapper.toDTO(receiveThresholdRepository.retrieve(id));
    }

    @Override
    public List<ReceiveThresholdDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveThreshold> entities = receiveThresholdRepository.getBySearchTerm(searchTerm, sort);
        return receiveThresholdMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveThresholdDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveThreshold> entities = receiveThresholdRepository.getBySearchTerm(searchTerm, pageable);
        return receiveThresholdMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReceiveThresholdDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<ReceiveThreshold> entities = receiveThresholdRepository.getList(clientId, sort);
        return receiveThresholdMapper.toDTOList(entities);
    }

    private void checkAreaName(String warehouse, String client, String areaName) {
        ReceiveThreshold receiveThreshold = receiveThresholdRepository.getByName(warehouse, client, areaName);
        if (receiveThreshold != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_THRESHOLD_NAME_UNIQUE.toString(), areaName);
        }
    }
}