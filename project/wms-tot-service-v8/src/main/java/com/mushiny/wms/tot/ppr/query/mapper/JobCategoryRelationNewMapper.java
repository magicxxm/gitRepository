package com.mushiny.wms.tot.ppr.query.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import com.mushiny.wms.tot.ppr.query.dto.JobCategoryRelationDTO;
import org.springframework.stereotype.Component;

@Component
public class JobCategoryRelationNewMapper implements BaseMapper<JobCategoryRelationDTO,JobCategoryRelation> {
    @Override
    public JobCategoryRelationDTO toDTO(JobCategoryRelation entity) {
        if (entity == null) {
            return null;
        }
        JobCategoryRelationDTO dto = new JobCategoryRelationDTO(entity);
        dto.setMainProcesses(entity.getMainProcesses());
        dto.setCoreProcesses(entity.getCoreProcesses());
        dto.setCategoryName(entity.getProject());
        dto.setLineItems(entity.getLineItems());
        dto.setReorder(entity.getReorder());
        dto.setPlanRate(entity.getPlanRate());
        return dto;
    }

    @Override
    public JobCategoryRelation toEntity(JobCategoryRelationDTO dto) {
        if (dto == null) {
            return null;
        }
        JobCategoryRelation entity =new JobCategoryRelation();
        entity.setMainProcesses(dto.getMainProcesses());
        entity.setCoreProcesses(dto.getCoreProcesses());
        entity.setProject(dto.getCategoryName());
        entity.setLineItems(dto.getLineItems());
        entity.setReorder(dto.getReorder());
        entity.setPlanRate(dto.getPlanRate());
        return entity;
    }
    @Override
    public void updateEntityFromDTO(JobCategoryRelationDTO dto, JobCategoryRelation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        }
        entity.setPlanRate(dto.getPlanRate());
        //下面不做修改
//        entity.setMainProcesses(dto.getMainProcesses());
//        entity.setCoreProcesses(dto.getCoreProcesses());
//        entity.setProject(dto.getCategoryName());
//        entity.setLineItems(dto.getLineItems());

    }
}
