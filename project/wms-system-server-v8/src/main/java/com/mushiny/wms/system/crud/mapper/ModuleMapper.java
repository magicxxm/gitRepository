package com.mushiny.wms.system.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.system.crud.dto.ModuleDTO;
import com.mushiny.wms.system.domain.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper implements BaseMapper<ModuleDTO, Module> {

    @Override
    public ModuleDTO toDTO(Module entity) {
        if (entity == null) {
            return null;
        }

        ModuleDTO dto = new ModuleDTO(entity);

        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setModuleType(entity.getModuleType());
        dto.setRfActive(entity.isRfActive());
        dto.setDkActive(entity.isDkActive());
        dto.setFormPath(entity.getFormPath());
        dto.setReportFilename(entity.getReportFilename());
        dto.setReportType(entity.getReportType());
        dto.setPrintPreview(entity.isPrintPreview());
        dto.setPrintDialog(entity.isPrintDialog());
        dto.setPrintCopies(entity.getPrintCopies());
        dto.setResourceKey(entity.getResourceKey());

        return dto;
    }


    @Override
    public Module toEntity(ModuleDTO dto) {
        if (dto == null) {
            return null;
        }

        Module entity = new Module();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setModuleType(dto.getModuleType());
        entity.setRfActive(dto.isRfActive());
        entity.setDkActive(dto.isDkActive());
        entity.setFormPath(dto.getFormPath());
        entity.setReportFilename(dto.getReportFilename());
        entity.setReportType(dto.getReportType());
        entity.setPrintPreview(dto.isPrintPreview());
        entity.setPrintDialog(dto.isPrintDialog());
        entity.setPrintCopies(dto.getPrintCopies());
        entity.setResourceKey(dto.getResourceKey());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ModuleDTO dto, Module entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setRfActive(dto.isRfActive());
        entity.setDkActive(dto.isDkActive());
        entity.setFormPath(dto.getFormPath());
        entity.setReportFilename(dto.getReportFilename());
        entity.setReportType(dto.getReportType());
        entity.setPrintPreview(dto.isPrintPreview());
        entity.setPrintDialog(dto.isPrintDialog());
        entity.setPrintCopies(dto.getPrintCopies());
        entity.setResourceKey(dto.getResourceKey());
    }
}
