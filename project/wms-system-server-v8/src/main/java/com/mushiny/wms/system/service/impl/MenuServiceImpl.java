package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.context.PermissionsContext;
import com.mushiny.wms.system.crud.dto.MenuDTO;
import com.mushiny.wms.system.crud.dto.MenuItemDTO;
import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.crud.mapper.MenuMapper;
import com.mushiny.wms.system.crud.mapper.ModuleMapper;
import com.mushiny.wms.system.domain.Menu;
import com.mushiny.wms.system.domain.Module;
import com.mushiny.wms.system.domain.UserWarehouseRole;
import com.mushiny.wms.system.domain.UserWarehouseRolePK;
import com.mushiny.wms.system.repository.MenuRepository;
import com.mushiny.wms.system.repository.ModuleRepository;
import com.mushiny.wms.system.repository.UserWarehouseRoleRepository;
import com.mushiny.wms.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ModuleRepository moduleRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final ApplicationContext applicationContext;
    private final PermissionsContext permissionsContext;
    private final MenuMapper menuMapper;
    private final ModuleMapper moduleMapper;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository,
                           ModuleRepository moduleRepository,
                           UserWarehouseRoleRepository userWarehouseRoleRepository,
                           ApplicationContext applicationContext,
                           PermissionsContext permissionsContext,
                           MenuMapper menuMapper,
                           ModuleMapper moduleMapper) {
        this.menuRepository = menuRepository;
        this.moduleRepository = moduleRepository;
        this.userWarehouseRoleRepository = userWarehouseRoleRepository;
        this.applicationContext = applicationContext;
        this.permissionsContext = permissionsContext;
        this.menuMapper = menuMapper;
        this.moduleMapper = moduleMapper;
    }

    @Override
    public void createAll(String parentId, List<MenuDTO> menuDTOs) {
        menuRepository.deleteByParentModuleId(parentId);
        if (menuDTOs != null && menuDTOs.size() > 0) {
            menuRepository.save(menuMapper.toEntityList(menuDTOs));
        }
    }

    @Override
    public void updateMore(List<MenuDTO> menuDTOs) {
        for (MenuDTO dto : menuDTOs) {
            Menu entity = menuRepository.retrieve(dto.getId());
            entity.setOrderIndex(dto.getOrderIndex());
            menuRepository.save(entity);
        }
    }

    @Override
    public List<MenuItemDTO> getMenuItem() {
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
        List<Menu> menus = menuRepository.findByParentModuleIdOrderByOrderIndex(root.getId());
        List<Module> modules = new ArrayList<>();
        modules.add(root);
        for (Menu menu : menus) {
            modules.add(menu.getModule());
        }
        return moduleMapper.toDTOList(modules);
    }

    @Override
    public List<ModuleDTO> getAssignedModuleByParentId(String parentId) {
        List<Menu> menus = menuRepository.findByParentModuleIdOrderByOrderIndex(parentId);
        List<Module> modules = new ArrayList<>();
        for (Menu menu : menus) {
            modules.add(menu.getModule());
        }
        return moduleMapper.toDTOList(modules);
    }

    @Override
    public List<ModuleDTO> getUnassignedModuleByParentId(String parentId) {
        List<Module> modules = moduleRepository.getUnassignedMenuModules(parentId);
        return moduleMapper.toDTOList(modules);
    }

    private List<MenuItemDTO> getMenuItemDTO(String parentId) {
        List<Menu> childMenus;
        if (permissionsContext.isWarehouseSuperRole()) {
            childMenus = menuRepository.findByParentModuleIdOrderByOrderIndex(parentId);
        } else {
            childMenus = menuRepository.getByParentIdAndWarehouseId(
                    parentId,
                    applicationContext.getCurrentWarehouse(),
                    applicationContext.getCurrentUser());
        }
        List<MenuItemDTO> menuItemDTOs = new ArrayList<>();
        if (childMenus == null || childMenus.size() <= 0) {
            return null;
        }
        for (Menu menu : childMenus) {
            MenuItemDTO menuItemDTO = new MenuItemDTO();
            MenuDTO menuDTO = menuMapper.toDTO(menu);
            menuItemDTO.setMenu(menuDTO);
            menuItemDTO.setChildMenuItems(getMenuItemDTO(menu.getModule().getId()));
            menuItemDTOs.add(menuItemDTO);
        }
        return menuItemDTOs;
    }

}
