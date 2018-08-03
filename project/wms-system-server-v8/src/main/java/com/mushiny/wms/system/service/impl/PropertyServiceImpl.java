package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.system.crud.dto.PropertyDTO;
import com.mushiny.wms.system.crud.mapper.PropertyMapper;
import com.mushiny.wms.system.domain.Property;
import com.mushiny.wms.system.repository.PropertyRepository;
import com.mushiny.wms.system.service.PropertyService;
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
public class PropertyServiceImpl implements PropertyService {

    private final ApplicationContext applicationContext;
    private final PropertyMapper propertyMapper;
    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(ApplicationContext applicationContext, PropertyMapper propertyMapper, PropertyRepository propertyRepository) {
        this.applicationContext = applicationContext;
        this.propertyMapper = propertyMapper;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public PropertyDTO create(PropertyDTO dto) {
        Property entity = propertyMapper.toEntity(dto);
        return propertyMapper.toDTO(propertyRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Property entity = propertyRepository.retrieve(id);
        propertyRepository.delete(entity);
    }

    @Override
    public PropertyDTO update(PropertyDTO dto) {
        Property entity = propertyRepository.retrieve(dto.getId());
        propertyMapper.updateEntityFromDTO(dto, entity);
        return propertyMapper.toDTO(propertyRepository.save(entity));
    }

    @Override
    public PropertyDTO retrieve(String id) {
        return propertyMapper.toDTO(propertyRepository.retrieve(id));
    }

    @Override
    public List<PropertyDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Property> entities = propertyRepository.getBySearchTerm(searchTerm, sort);
        return propertyMapper.toDTOList(entities);
    }

    @Override
    public Page<PropertyDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "systemKey"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Property> entities = propertyRepository.getBySearchTerm(searchTerm,pageable);
        return propertyMapper.toDTOPage(pageable, entities);
    }
}
