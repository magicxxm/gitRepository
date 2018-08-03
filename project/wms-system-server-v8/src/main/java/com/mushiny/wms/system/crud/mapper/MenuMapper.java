package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.system.crud.dto.MenuDTO;
import com.mushiny.wms.system.domain.Menu;
import com.mushiny.wms.system.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Autowired
    public MenuMapper(ModuleMapper moduleMapper,
                      ModuleRepository moduleRepository) {
        this.moduleMapper = moduleMapper;
        this.moduleRepository = moduleRepository;
    }

    public MenuDTO toDTO(Menu entity) {
        if (entity == null) {
            return null;
        }

        MenuDTO dto = new MenuDTO();

        dto.setId(entity.getId());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setModule(moduleMapper.toDTO(entity.getModule()));
        dto.setParentModule(moduleMapper.toDTO(entity.getParentModule()));

        return dto;
    }

    public List<MenuDTO> toDTOList(List<Menu> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Menu toEntity(MenuDTO dto) {
        if (dto == null) {
            return null;
        }

        Menu entity = new Menu();

        entity.setId(dto.getId());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setModule(moduleRepository.retrieve(dto.getChildId()));
        entity.setParentModule(moduleRepository.retrieve(dto.getParentId()));

        return entity;
    }

    public List<Menu> toEntityList(List<MenuDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
