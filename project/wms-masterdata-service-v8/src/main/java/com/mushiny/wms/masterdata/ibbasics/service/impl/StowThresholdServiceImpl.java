package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.StowThresholdMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.StowThreshold;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StowThresholdRepository;
import com.mushiny.wms.masterdata.ibbasics.service.StowThresholdService;
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
public class StowThresholdServiceImpl implements StowThresholdService {

    private final StowThresholdRepository stowThresholdRepository;
    private final ApplicationContext applicationContext;
    private final StowThresholdMapper stowThresholdMapper;

    @Autowired
    public StowThresholdServiceImpl(StowThresholdRepository stowThresholdRepository,
                                    ApplicationContext applicationContext,
                                    StowThresholdMapper stowThresholdMapper) {
        this.stowThresholdRepository = stowThresholdRepository;
        this.applicationContext = applicationContext;
        this.stowThresholdMapper = stowThresholdMapper;
    }

    @Override
    public StowThresholdDTO create(StowThresholdDTO dto) {
        StowThreshold entity = stowThresholdMapper.toEntity(dto);
        checkAreaName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return stowThresholdMapper.toDTO(stowThresholdRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        StowThreshold entity = stowThresholdRepository.retrieve(id);
        stowThresholdRepository.delete(entity);
    }

    @Override
    public StowThresholdDTO update(StowThresholdDTO dto) {
        StowThreshold entity = stowThresholdRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName())
                && entity.getClientId().equalsIgnoreCase(dto.getClientId()))) {
            String client = dto.getClientId();
            checkAreaName(entity.getWarehouseId(), client, dto.getName());
        }
        stowThresholdMapper.updateEntityFromDTO(dto, entity);
        return stowThresholdMapper.toDTO(stowThresholdRepository.save(entity));
    }

    @Override
    public StowThresholdDTO retrieve(String id) {
        return stowThresholdMapper.toDTO(stowThresholdRepository.retrieve(id));
    }

    @Override
    public List<StowThresholdDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StowThreshold> entities = stowThresholdRepository.getBySearchTerm(searchTerm, sort);
        return stowThresholdMapper.toDTOList(entities);
    }

    @Override
    public Page<StowThresholdDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StowThreshold> entities = stowThresholdRepository.getBySearchTerm(searchTerm, pageable);
        return stowThresholdMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<StowThresholdDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<StowThreshold> entities = stowThresholdRepository.getList(clientId, sort);
        return stowThresholdMapper.toDTOList(entities);
    }

    private void checkAreaName(String warehouse, String client, String areaName) {
        StowThreshold stowThreshold = stowThresholdRepository.getByName(warehouse, client, areaName);
        if (stowThreshold != null) {
            throw new ApiException(InBoundException.EX_MD_IN_STOW_THRESHOLD_NAME_UNIQUE.toString(), areaName);
        }
    }
}