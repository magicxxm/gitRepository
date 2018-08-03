package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.BoxTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.BoxTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.BoxTypeService;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoxTypeServiceImpl implements BoxTypeService {

    private final BoxTypeRepository boxTypeRepository;
    private final ApplicationContext applicationContext;
    private final BoxTypeMapper boxTypeMapper;

    public BoxTypeServiceImpl(BoxTypeRepository boxTypeRepository,
                              ApplicationContext applicationContext,
                              BoxTypeMapper boxTypeMapper) {
        this.boxTypeRepository = boxTypeRepository;
        this.applicationContext = applicationContext;
        this.boxTypeMapper = boxTypeMapper;
    }

    @Override
    public BoxTypeDTO create(BoxTypeDTO dto) {
        BoxType entity = boxTypeMapper.toEntity(dto);
        checkBoxTypeName(entity.getWarehouseId(), entity.getClientId(), entity.getName());
        return boxTypeMapper.toDTO(boxTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        BoxType entity = boxTypeRepository.retrieve(id);
        boxTypeRepository.delete(entity);
    }

    @Override
    public BoxTypeDTO update(BoxTypeDTO dto) {
        BoxType entity = boxTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkBoxTypeName(entity.getWarehouseId(), entity.getClientId(), dto.getName());
        }
        boxTypeMapper.updateEntityFromDTO(dto, entity);
        return boxTypeMapper.toDTO(boxTypeRepository.save(entity));
    }


    @Override
    public BoxTypeDTO retrieve(String id) {
        return boxTypeMapper.toDTO(boxTypeRepository.retrieve(id));
    }

    @Override
    public List<BoxTypeDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<BoxType> entities = boxTypeRepository.getNotLockList(clientId, sort);
        return boxTypeMapper.toDTOList(entities);
    }

    @Override
    public List<BoxTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<BoxType> entities = boxTypeRepository.getBySearchTerm(searchTerm, sort);
        return boxTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<BoxTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return boxTypeMapper.toDTOPage(pageable, boxTypeRepository.getBySearchTerm(searchTerm, pageable));
    }

    private void checkBoxTypeName(String warehouse, String client, String name) {
        BoxType boxType = boxTypeRepository.getByName(warehouse, client, name);
        if (boxType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_BOX_TYPE_NAME_UNIQUE.toString(), name);
        }
    }
}
