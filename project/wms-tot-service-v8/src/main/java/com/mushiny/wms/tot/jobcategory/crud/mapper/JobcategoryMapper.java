package com.mushiny.wms.tot.jobcategory.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;
import org.springframework.stereotype.Component;

@Component
public class JobcategoryMapper implements BaseMapper<JobcategoryDTO,Jobcategory> {
    @Override
    public JobcategoryDTO toDTO(Jobcategory entity) {
        if (entity == null) {
            return null;
        }
        JobcategoryDTO dto = new JobcategoryDTO(entity);
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setJobType(entity.getJobType());
        dto.setSubproDataSource(entity.getSubproDataSource());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Jobcategory toEntity(JobcategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Jobcategory entity =new Jobcategory();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setJobType(dto.getJobType());
        entity.setSubproDataSource(dto.getSubproDataSource());
        entity.setWarehouseId(dto.getWarehouseId());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(JobcategoryDTO dto, Jobcategory entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setJobType(dto.getJobType());
        entity.setSubproDataSource(dto.getSubproDataSource());
        entity.setWarehouseId(dto.getWarehouseId());
    }
}
