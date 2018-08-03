package com.mushiny.wms.tot.ppr.query.dto;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import org.springframework.stereotype.Component;

@Component
public class JobCategoryRelationMapper implements BaseMapper<PprMainPageDTO,JobCategoryRelation> {
    @Override
    public PprMainPageDTO toDTO(JobCategoryRelation entity) {
        if (entity == null) {
            return null;
        }
        PprMainPageDTO dto = new PprMainPageDTO(entity);
        dto.setMainProcesses(entity.getMainProcesses());
        dto.setCoreProcesses(entity.getCoreProcesses());
        dto.setCategoryName(entity.getProject());
        dto.setLineItems(entity.getLineItems());
        dto.setJobType(entity.getJobType());
        dto.setReorder(entity.getReorder());
        dto.setPlanRate(entity.getPlanRate());
        return dto;
    }

    @Override
    public JobCategoryRelation toEntity(PprMainPageDTO dto) {
        if (dto == null) {
            return null;
        }
        JobCategoryRelation entity =new JobCategoryRelation();
        entity.setMainProcesses(dto.getMainProcesses());
        entity.setCoreProcesses(dto.getCoreProcesses());
        entity.setProject(dto.getCategoryName());
        entity.setLineItems(dto.getLineItems());
        entity.setJobType(dto.getJobType());
        entity.setReorder(dto.getReorder());
        entity.setPlanRate(dto.getPlanRate());
        return entity;
    }
    @Override
    public void updateEntityFromDTO(PprMainPageDTO dto, JobCategoryRelation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        }
        entity.setMainProcesses(dto.getMainProcesses());
        entity.setCoreProcesses(dto.getCoreProcesses());
        entity.setProject(dto.getCategoryName());
        entity.setLineItems(dto.getLineItems());
        entity.setJobType(dto.getJobType());
        entity.setReorder(dto.getReorder());
        entity.setPlanRate(dto.getPlanRate());
    }
}
