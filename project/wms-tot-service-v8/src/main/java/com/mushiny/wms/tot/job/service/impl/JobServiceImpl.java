package com.mushiny.wms.tot.job.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.job.crud.dto.JobDTO;
import com.mushiny.wms.tot.job.crud.mapper.JobMapper;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.job.repository.JobRepository;
import com.mushiny.wms.tot.job.service.JobService;
import com.mushiny.wms.tot.job.util.ReadExcelJob;
import com.mushiny.wms.tot.jobcategory.repository.JobcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JobcategoryRepository jobcategoryRepository;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, JobcategoryRepository jobcategoryRepository,
                          ApplicationContext applicationContext) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.jobcategoryRepository = jobcategoryRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public JobDTO create(JobDTO dto) {
        checkDataExist(dto);
        Job job =jobMapper.toEntity(dto);
        return jobMapper.toDTO(jobRepository.save(job));
    }

    @Override
    public void delete(String id) {
        Job entity = jobRepository.retrieve(id);
        jobRepository.delete(entity);
    }

    @Override
    public JobDTO update(JobDTO dto)
    {
        Job entity = jobRepository.retrieve(dto.getId());
        checkDataExist(dto,entity);
        jobMapper.updateEntityFromDTO(dto, entity);
        return jobMapper.toDTO(jobRepository.save(entity));
    }

    @Override
    public JobDTO retrieve(String id) {
        Job entity = jobRepository.retrieve(id);
        return jobMapper.toDTO(entity);
    }

    @Override
    public List<JobDTO> getBySearchTerm(String searchTerm, Sort sort) {
        String defaultSearchTerm = null;
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        List<Job> entities = jobRepository.getBySearchTerm(searchTerm, sort);
        return jobMapper.toDTOList(entities);
    }

    @Override
    public Page<JobDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        String defaultSearchTerm = null;
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        Page<Job> entities = jobRepository.getBySearchTerm(searchTerm, pageable);
        return jobMapper.toDTOPage(pageable, entities);
    }
    @Override
    public Page<JobDTO> getBySearchTerm(String searchTerm,String jobType, Pageable pageable) {
        searchTerm = getSearchTerm(searchTerm, jobType);
        Page<Job> entities = jobRepository.getBySearchTerm(searchTerm, pageable);
        return jobMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<JobDTO> getJobByName(String name) {
        List<Job> jobs = jobRepository.getJobByCategory(name);
        return jobMapper.toDTOList(jobs);
    }

    @Override
    public String checkJobType(String jobCode) {
        Job job = jobRepository.getJob(jobCode);
        return job.getJobType();
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelJob readExcel = new ReadExcelJob(jobcategoryRepository,applicationContext);
        //解析excel，获取上传的事件单
        List<JobDTO> jobDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(jobDTOList);
    }

    public void createImport(List<JobDTO> itemDataDTOList) {
        for (JobDTO jobDTO : itemDataDTOList) {
            checkDataExist(jobDTO);
            jobRepository.save(jobMapper.toEntity(jobDTO));
        }
    }


    private void checkDataExist(JobDTO dto) {
        Job job = jobRepository.getJob(dto.getCode());
        if (job != null) {
            throw new ApiException("TOT_JOB_EXISTS");
        }
    }

    private void checkDataExist(JobDTO dto,Job entity) {
        Job job = jobRepository.getJob(dto.getCode());
        if (job != null&&!(dto.getCode().equals(entity.getCode()))) {
            throw new ApiException("TOT_JOB_EXISTS");
        }
    }
}
