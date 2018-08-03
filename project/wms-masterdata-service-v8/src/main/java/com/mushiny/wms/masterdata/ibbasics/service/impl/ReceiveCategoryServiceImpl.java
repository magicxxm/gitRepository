package com.mushiny.wms.masterdata.ibbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveCategoryMapper;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveCategoryPositionMapper;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategory;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryPosition;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveCategoryRepository;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveCategoryService;
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
public class ReceiveCategoryServiceImpl implements ReceiveCategoryService {

    private final ReceiveCategoryRepository receivingCategoryRepository;
    private final ApplicationContext applicationContext;
    private final ReceiveCategoryMapper receivingCategoryMapper;
    private final ReceiveCategoryPositionMapper receivingCategoryPositionMapper;

    @Autowired
    public ReceiveCategoryServiceImpl(ApplicationContext applicationContext,
                                      ReceiveCategoryRepository receivingCategoryRepository,
                                      ReceiveCategoryMapper receivingCategoryMapper,
                                      ReceiveCategoryPositionMapper receivingCategoryPositionMapper) {
        this.applicationContext = applicationContext;
        this.receivingCategoryRepository = receivingCategoryRepository;
        this.receivingCategoryMapper = receivingCategoryMapper;
        this.receivingCategoryPositionMapper = receivingCategoryPositionMapper;
    }

    @Override
    public ReceiveCategoryDTO create(ReceiveCategoryDTO dto) {
        ReceiveCategory entity = receivingCategoryMapper.toEntity(dto);
        checkReceivingCategoryName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        for (ReceiveCategoryPositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(receivingCategoryPositionMapper.toEntity(positionDTO));
        }
        return receivingCategoryMapper.toDTO(receivingCategoryRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        ReceiveCategory entity = receivingCategoryRepository.retrieve(id);
        receivingCategoryRepository.delete(entity);
    }

    @Override
    public ReceiveCategoryDTO update(ReceiveCategoryDTO dto) {
        ReceiveCategory entity = receivingCategoryRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkReceivingCategoryName(entity.getWarehouseId(), entity.getClientId(), dto.getName());
        }
        receivingCategoryMapper.updateEntityFromDTO(dto, entity);
        entity.getPositions().clear();
        List<ReceiveCategoryPosition> positions = receivingCategoryPositionMapper.toEntityList(dto.getPositions());
        for (ReceiveCategoryPosition position : positions) {
            entity.addPosition(position);
        }
        return receivingCategoryMapper.toDTO(receivingCategoryRepository.save(entity));
    }

    @Override
    public ReceiveCategoryDTO retrieve(String id) {
        ReceiveCategory entity = receivingCategoryRepository.retrieve(id);
        ReceiveCategoryDTO dto = receivingCategoryMapper.toDTO(entity);
        dto.setPositions(receivingCategoryPositionMapper.toDTOList(entity.getPositions()));
        return dto;
    }

    @Override
    public List<ReceiveCategoryDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ReceiveCategory> entities = receivingCategoryRepository.getBySearchTerm(searchTerm, sort);
        return receivingCategoryMapper.toDTOList(entities);
    }

    @Override
    public Page<ReceiveCategoryDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "orderIndex"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ReceiveCategory> entities = receivingCategoryRepository.getBySearchTerm(searchTerm, pageable);
        return receivingCategoryMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ReceiveCategoryDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<ReceiveCategory> entities = receivingCategoryRepository.getNotLockList(clientId, sort);
        return receivingCategoryMapper.toDTOList(entities);
    }

    private void checkReceivingCategoryName(String warehouse, String client, String name) {
        ReceiveCategory receivingCategory = receivingCategoryRepository.getByName(warehouse, client, name);
        if (receivingCategory != null) {
            throw new ApiException(InBoundException.EX_MD_IN_RECEIVE_CATEGORY_NAME_UNIQUE.toString(), name);
        }
    }
}
