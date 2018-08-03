package com.mushiny.wms.tot.job.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.job.crud.dto.JobDTO;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.jobcategory.crud.mapper.JobcategoryMapper;
import com.mushiny.wms.tot.jobcategory.repository.JobcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobMapper implements BaseMapper<JobDTO,Job> {

    private final JobcategoryMapper categoryMapper;
    private final JobcategoryRepository categoryRepository;

    @Autowired
    public JobMapper(JobcategoryMapper categoryMapper,JobcategoryRepository categoryRepository){
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public JobDTO toDTO(Job entity) {
        if (entity == null) {
            return null;
        }
        JobDTO dto = new JobDTO(entity);
        dto.setJobType(entity.getJobType());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setKeyword(entity.getKeyword());
        dto.setIndirectType(entity.getIndirectType());
        dto.setCategoryDTO(categoryMapper.toDTO(entity.getCategory()));
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public Job toEntity(JobDTO dto) {
        if (dto == null) {
            return null;
        }
        Job entity =new Job();
        entity.setJobType(dto.getJobType());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setKeyword(dto.getKeyword());
        entity.setIndirectType(dto.getIndirectType());
        if(dto.getCategoryId() != null){
            entity.setCategory(categoryRepository.retrieve(dto.getCategoryId()));
        }
        entity.setWarehouseId(dto.getWarehouseId());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(JobDTO dto, Job entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setJobType(dto.getJobType());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setKeyword(dto.getKeyword());
        entity.setIndirectType(dto.getIndirectType());
        if(dto.getCategoryId() != null){
            entity.setCategory(categoryRepository.retrieve(dto.getCategoryId()));
        }
        entity.setWarehouseId(dto.getWarehouseId());
    }
}
