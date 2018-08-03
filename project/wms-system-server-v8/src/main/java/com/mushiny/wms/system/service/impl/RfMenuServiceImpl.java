package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.dto.RfMenuDTO;
import com.mushiny.wms.system.crud.dto.RfMenuItemDTO;
import com.mushiny.wms.system.crud.mapper.ModuleMapper;
import com.mushiny.wms.system.crud.mapper.RfMenuMapper;
import com.mushiny.wms.system.domain.Module;
import com.mushiny.wms.system.domain.RfMenu;
import com.mushiny.wms.system.repository.ModuleRepository;
import com.mushiny.wms.system.repository.RfMenuRepository;
import com.mushiny.wms.system.service.RfMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RfMenuServiceImpl implements RfMenuService {

    private final RfMenuRepository rfMenuRepository;
    private final ModuleRepository moduleRepository;
    private final ApplicationContext applicationContext;
    private final PermissionsContext permissionsContext;
    private final RfMenuMapper rfMenuMapper;
    private final ModuleMapper moduleMapper;

    @Autowired
    public RfMenuServiceImpl(RfMenuRepository rfMenuRepository,
                             ModuleRepository moduleRepository,
                             ApplicationContext applicationContext,
                             PermissionsContext permissionsContext,
                             RfMenuMapper rfMenuMapper,
                             ModuleMapper moduleMapper) {
        this.rfMenuRepository = rfMenuRepository;
        this.moduleRepository = moduleRepository;
        this.applicationContext = applicationContext;
        this.permissionsContext = permissionsContext;
        this.rfMenuMapper = rfMenuMapper;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public void createAll(String parentId, List<RfMenuDTO> rfMenuDTOS) {
        rfMenuRepository.deleteByParentModuleId(parentId);
        if (rfMenuDTOS != null && rfMenuDTOS.size() > 0) {
            rfMenuRepository.save(rfMenuMapper.toEntityList(rfMenuDTOS));
        }
    }

    @Override
    public void updateMore(List<RfMenuDTO> rfMenuDTOS) {
        for (RfMenuDTO rfMenuDTO : rfMenuDTOS) {
            RfMenu entity = rfMenuRepository.retrieve(rfMenuDTO.getId());
            entity.setOrderIndex(rfMenuDTO.getOrderIndex());
            rfMenuRepository.save(entity);
        }
    }

    @Override
    public List<RfMenuDTO> getByModuleId(String moduleId) {
        Module module = moduleRepository.retrieve(moduleId);
        List<RfMenu> rfMenus = rfMenuRepository.findByModuleOrderByOrderIndex(module);
        return rfMenuMapper.toDTOList(rfMenus);
    }

    @Override
    public List<RfMenuItemDTO> getMenuItem() {
        Module root = Optional
                .ofNullable(moduleRepository.findByName(Constant.MODULE_ROOT))
                .orElseThrow(() -> new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString()));
        return getMenuItemDTO(root.getId());
    }

    @Override
    public List<ModuleDTO> getAssignedModuleByRoot() {
        Module root = Optional
                .ofNullable(moduleRepository.findByName(Constant.MODULE_ROOT))
                .orElseThrow(() -> new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString()));
        List<Module> modules = new ArrayList<>();
        modules.add(root);
        modules.addAll(moduleRepository.getAllRfMenuModules());
        return moduleMapper.toDTOList(modules);
    }

    @Override
    public List<ModuleDTO> getAssignedModuleByParentId(String parentId) {
        List<RfMenu> rfMenus = rfMenuRepository.findByParentModuleIdOrderByOrderIndex(parentId);
        List<Module> modules = new ArrayList<>();
        for (RfMenu rfMenu : rfMenus) {
            modules.add(rfMenu.getModule());
        }
        return moduleMapper.toDTOList(modules);
    }

    @Override
    public List<ModuleDTO> getUnassignedModuleByParentId(String parentId) {
        List<Module> modules = moduleRepository.getUnassignedRfMenuModules(parentId);
        return moduleMapper.toDTOList(modules);
    }

    private List<RfMenuItemDTO> getMenuItemDTO(String parentId) {
        List<RfMenu> childRfMenus;
        if (permissionsContext.isWarehouseSuperRole()) {
            childRfMenus = rfMenuRepository.findByParentModuleIdOrderByOrderIndex(parentId);
        } else {
            childRfMenus = rfMenuRepository.getByParentIdAndWarehouseId(
                    parentId,
                    applicationContext.getCurrentWarehouse(),
                    applicationContext.getCurrentUser());
        }
        List<RfMenuItemDTO> rfMenuItemDTOS = new ArrayList<>();
        if (childRfMenus == null || childRfMenus.size() <= 0) {
            return null;
        }
        for (RfMenu rfMenu : childRfMenus) {
            RfMenuItemDTO rfMenuItemDTO = new RfMenuItemDTO();
            RfMenuDTO rfMenuDTO = rfMenuMapper.toDTO(rfMenu);
            rfMenuItemDTO.setRfMenu(rfMenuDTO);
            rfMenuItemDTO.setChildMenuItems(getMenuItemDTO(rfMenu.getModule().getId()));
            rfMenuItemDTOS.add(rfMenuItemDTO);
        }
        return rfMenuItemDTOS;
    }
}
