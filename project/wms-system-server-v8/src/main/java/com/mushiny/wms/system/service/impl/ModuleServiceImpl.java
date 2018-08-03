package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.mapper.ModuleMapper;
import com.mushiny.wms.system.domain.Module;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.repository.ModuleRepository;
import com.mushiny.wms.system.service.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ApplicationContext applicationContext;
    private final PermissionsContext permissionsContext;
    private final ModuleMapper moduleMapper;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository,
                             ApplicationContext applicationContext,
                             PermissionsContext permissionsContext,
                             ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.applicationContext = applicationContext;
        this.permissionsContext = permissionsContext;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public ModuleDTO create(ModuleDTO dto) {
        Module entity = moduleMapper.toEntity(dto);
        checkModuleName(entity.getName());
        return moduleMapper.toDTO(moduleRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Module entity = moduleRepository.retrieve(id);
        moduleRepository.delete(entity);
    }

    @Override
    public ModuleDTO update(ModuleDTO dto) {
        Module entity = moduleRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkModuleName(dto.getName());
        }
        moduleMapper.updateEntityFromDTO(dto, entity);
        return moduleMapper.toDTO(moduleRepository.save(entity));
    }

    @Override
    public ModuleDTO retrieve(String id) {
        return moduleMapper.toDTO(moduleRepository.retrieve(id));
    }

    @Override
    public List<ModuleDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Module> entities = moduleRepository.getBySearchTerm(searchTerm, sort);
        return moduleMapper.toDTOList(entities);
    }

    @Override
    public Page<ModuleDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Module> entities = moduleRepository.getBySearchTerm(searchTerm, pageable);
        return moduleMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ModuleDTO> getByCurrentWarehouseIdAndUserId() {
        List<Module> entities;
        if (permissionsContext.isWarehouseSuperRole()) {
            entities = moduleRepository.findAllByOrderByName();
        } else {
            entities = moduleRepository.getByWarehouseIdAndUserId(
                    applicationContext.getCurrentWarehouse(),
                    applicationContext.getCurrentUser());
        }
        return moduleMapper.toDTOList(entities);
    }

    private void checkModuleName(String moduleName) {
        Module module = moduleRepository.findByName(moduleName);
        if (module != null) {
            throw new ApiException(SystemException.EX_SYS_MODULE_NAME_UNIQUE.toString(), moduleName);
        }
    }
}
