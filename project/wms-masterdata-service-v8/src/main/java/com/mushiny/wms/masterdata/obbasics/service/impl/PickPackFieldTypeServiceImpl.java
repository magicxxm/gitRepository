package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackFieldTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackFieldTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackFieldType;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackFieldTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickPackFieldTypeService;
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
public class PickPackFieldTypeServiceImpl implements PickPackFieldTypeService {

    private final PickPackFieldTypeRepository pickPackFieldTypeRepository;
    private final ApplicationContext applicationContext;
    private final PickPackFieldTypeMapper pickPackFieldTypeMapper;

    @Autowired
    public PickPackFieldTypeServiceImpl(PickPackFieldTypeRepository pickPackFieldTypeRepository,
                                        ApplicationContext applicationContext,
                                        PickPackFieldTypeMapper pickPackFieldTypeMapper) {
        this.pickPackFieldTypeRepository = pickPackFieldTypeRepository;
        this.applicationContext = applicationContext;
        this.pickPackFieldTypeMapper = pickPackFieldTypeMapper;
    }

    @Override
    public PickPackFieldTypeDTO create(PickPackFieldTypeDTO dto) {
        PickPackFieldType entity = pickPackFieldTypeMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        return pickPackFieldTypeMapper.toDTO(pickPackFieldTypeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PickPackFieldType entity = pickPackFieldTypeRepository.retrieve(id);
        pickPackFieldTypeRepository.delete(entity);
    }

    @Override
    public PickPackFieldTypeDTO update(PickPackFieldTypeDTO dto) {
        PickPackFieldType entity = pickPackFieldTypeRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        pickPackFieldTypeMapper.updateEntityFromDTO(dto, entity);
        return pickPackFieldTypeMapper.toDTO(pickPackFieldTypeRepository.save(entity));
    }

    @Override
    public PickPackFieldTypeDTO retrieve(String id) {
        return pickPackFieldTypeMapper.toDTO(pickPackFieldTypeRepository.retrieve(id));
    }

    @Override
    public List<PickPackFieldTypeDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PickPackFieldType> entities = pickPackFieldTypeRepository.getNotLockList(null, sort);
        return pickPackFieldTypeMapper.toDTOList(entities);
    }

    @Override
    public List<PickPackFieldTypeDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickPackFieldType> entities = pickPackFieldTypeRepository.getBySearchTerm(searchTerm, sort);
        return pickPackFieldTypeMapper.toDTOList(entities);
    }

    @Override
    public Page<PickPackFieldTypeDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickPackFieldType> entities = pickPackFieldTypeRepository.getBySearchTerm(searchTerm, pageable);
        return pickPackFieldTypeMapper.toDTOPage(pageable, entities);
    }

    private void checkName(String warehouse, String areaName) {
        PickPackFieldType pickPackFieldType = pickPackFieldTypeRepository.getByName(warehouse, areaName);
        if (pickPackFieldType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICKPACK_FIELD_TYPE_NAME_UNIQUE.toString(), areaName);
        }
    }
}