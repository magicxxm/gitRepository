package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.system.crud.dto.RfMenuDTO;
import com.mushiny.wms.system.domain.RfMenu;
import com.mushiny.wms.system.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RfMenuMapper {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;

    @Autowired
    public RfMenuMapper(ModuleMapper moduleMapper,
                        ModuleRepository moduleRepository) {
        this.moduleMapper = moduleMapper;
        this.moduleRepository = moduleRepository;
    }

    public RfMenuDTO toDTO(RfMenu entity) {
        if (entity == null) {
            return null;
        }

        RfMenuDTO dto = new RfMenuDTO();

        dto.setId(entity.getId());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setModule(moduleMapper.toDTO(entity.getModule()));
        dto.setParentModule(moduleMapper.toDTO(entity.getParentModule()));

        return dto;
    }

    public List<RfMenuDTO> toDTOList(List<RfMenu> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public RfMenu toEntity(RfMenuDTO dto) {
        if (dto == null) {
            return null;
        }

        RfMenu entity = new RfMenu();

        entity.setId(dto.getId());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setModule(moduleRepository.retrieve(dto.getChildId()));
        entity.setParentModule(moduleRepository.retrieve(dto.getParentId()));

        return entity;
    }

    public List<RfMenu> toEntityList(List<RfMenuDTO> dtoList) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
