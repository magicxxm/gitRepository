package com.mushiny.wms.tot.jobrelation.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.jobrelation.crud.dto.JobrelationDTO;
import com.mushiny.wms.tot.jobrelation.domain.Jobrelation;
import org.springframework.stereotype.Component;

@Component
public class JobrelationMapper implements BaseMapper<JobrelationDTO,Jobrelation> {
    @Override
    public JobrelationDTO toDTO(Jobrelation entity) {
        if (entity == null) {
            return null;
        }
        JobrelationDTO dto = new JobrelationDTO(entity);
        dto.setOperation(entity.getOperation());
        dto.setTool(entity.getTool());
        dto.setJobcategory_name(entity.getJobcategoryName());
        return dto;
    }

    @Override
    public Jobrelation toEntity(JobrelationDTO dto) {
        if (dto == null) {
            return null;
        }
        Jobrelation entity =new Jobrelation();
        entity.setOperation(dto.getOperation());
        entity.setTool(dto.getTool());
        entity.setJobcategoryName(dto.getJobcategoryName());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(JobrelationDTO dto, Jobrelation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setOperation(dto.getOperation());
        entity.setTool(dto.getTool());
        entity.setJobcategoryName(dto.getJobcategoryName());
    }
}
