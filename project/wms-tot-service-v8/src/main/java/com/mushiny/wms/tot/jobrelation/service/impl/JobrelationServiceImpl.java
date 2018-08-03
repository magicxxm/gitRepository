package com.mushiny.wms.tot.jobrelation.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.jobrelation.crud.dto.JobrelationDTO;
import com.mushiny.wms.tot.jobrelation.crud.mapper.JobrelationMapper;
import com.mushiny.wms.tot.jobrelation.domain.Jobrelation;
import com.mushiny.wms.tot.jobrelation.repository.JobrelationRepository;
import com.mushiny.wms.tot.jobrelation.service.JobrelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobrelationServiceImpl implements JobrelationService {
    private final JobrelationRepository jobrelationRepository;
    private final JobrelationMapper jobrelationMapper;

    @Autowired
    public JobrelationServiceImpl(JobrelationRepository jobrelationRepository, JobrelationMapper jobrelationMapper) {
        this.jobrelationRepository = jobrelationRepository;
        this.jobrelationMapper = jobrelationMapper;
    }

    @Override
    public JobrelationDTO create(JobrelationDTO dto) {
        checkDataExist(dto);
        Jobrelation jobrelation = jobrelationMapper.toEntity(dto);
        return jobrelationMapper.toDTO(jobrelationRepository.save(jobrelation));
    }

    @Override
    public void delete(String id) {
        Jobrelation entity = jobrelationRepository.retrieve(id);
        jobrelationRepository.delete(entity);
    }

    @Override
    public JobrelationDTO update(JobrelationDTO dto)
    {
        checkDataExist(dto);
        Jobrelation entity = jobrelationRepository.retrieve(dto.getId());
        jobrelationMapper.updateEntityFromDTO(dto, entity);
        return jobrelationMapper.toDTO(jobrelationRepository.save(entity));
    }

    @Override
    public JobrelationDTO retrieve(String id) {
        Jobrelation entity = jobrelationRepository.retrieve(id);
        return jobrelationMapper.toDTO(entity);
    }

    @Override
    public List<JobrelationDTO> getBySearchTerm(String searchTerm, Sort sort)
    {
        List<Jobrelation> entities = jobrelationRepository.getBySearchTerm(searchTerm, sort);
        return jobrelationMapper.toDTOList(entities);
    }

    @Override
    public Page<JobrelationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<Jobrelation> entities = jobrelationRepository.getBySearchTerm(searchTerm, pageable);
        return jobrelationMapper.toDTOPage(pageable, entities);
    }

    private void checkDataExist(JobrelationDTO dto) {
        Jobrelation jobrelationList = jobrelationRepository.findByJobcategoryNameOrOperationAndTool(dto.getJobcategoryName(),dto.getOperation(),dto.getTool());
        if (jobrelationList != null) {
            throw new ApiException("TOT_JOBRELATION_ERROR");
        }
    }
}
