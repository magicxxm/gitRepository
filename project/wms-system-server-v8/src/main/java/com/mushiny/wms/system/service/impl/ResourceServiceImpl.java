package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.crud.dto.ResourceDTO;
import com.mushiny.wms.system.crud.mapper.ResourceMapper;
import com.mushiny.wms.system.domain.Resource;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.repository.ResourceRepository;
import com.mushiny.wms.system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public ResourceDTO create(ResourceDTO dto) {
        Resource entity = resourceMapper.toEntity(dto);
        checkResourceLocaleResourceKey(entity.getResourceKey(), entity.getLocale());
        return resourceMapper.toDTO(resourceRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Resource entity = resourceRepository.retrieve(id);
        resourceRepository.delete(entity);
    }

    @Override
    public ResourceDTO update(ResourceDTO dto) {
        Resource entity = resourceRepository.retrieve(dto.getId());
        if (!(entity.getResourceKey().equalsIgnoreCase(dto.getResourceKey())
                && entity.getLocale().equalsIgnoreCase(dto.getLocale()))) {
            checkResourceLocaleResourceKey(dto.getResourceKey(), dto.getLocale());
        }
        resourceMapper.updateEntityFromDTO(dto, entity);
        return resourceMapper.toDTO(resourceRepository.save(entity));
    }

    @Override
    public ResourceDTO retrieve(String id) {
        return resourceMapper.toDTO(resourceRepository.retrieve(id));
    }

    @Override
    public List<ResourceDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Resource> entities = resourceRepository.getBySearchTerm(searchTerm, sort);
        return resourceMapper.toDTOList(entities);
    }

    @Override
    public Page<ResourceDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "resourceKey"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Resource> entities = resourceRepository.getBySearchTerm(searchTerm, pageable);
        return resourceMapper.toDTOPage(pageable, entities);
    }

    @Override
    public Map<String, String> getByLocale(String locale) {
        List<Resource> entities;
        if (!locale.equals(Constant.DEFAULT_LOCALE)) {
            entities = resourceRepository.findByLocaleOrderByResourceKey(locale);
            List<Resource> defaultEntities = resourceRepository.getByDefaultLocaleNotExistsLocale(locale, Constant.DEFAULT_LOCALE);
            entities.addAll(defaultEntities);
        } else {
            entities = resourceRepository.findByLocaleOrderByResourceKey(locale);
        }
        return entities.stream()
                .collect(Collectors.toMap(Resource::getResourceKey, Resource::getResourceValue));
    }

    private void checkResourceLocaleResourceKey(String resourceKey, String locale) {
        Resource resource = resourceRepository.findByResourceKeyAndLocale(resourceKey, locale);
        if (resource != null) {
            throw new ApiException(
                    SystemException.EX_SYS_RESOURCE_KEY_LOCALE_UNIQUE.toString(), locale, resource);
        }
    }
}
