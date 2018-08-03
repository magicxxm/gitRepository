package com.mushiny.wms.tot.jobcategory.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.job.crud.dto.JobDTO;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
import com.mushiny.wms.tot.jobcategory.crud.mapper.JobcategoryMapper;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;
import com.mushiny.wms.tot.jobcategory.repository.JobcategoryRepository;
import com.mushiny.wms.tot.jobcategory.service.JobcategoryService;
import com.mushiny.wms.tot.jobcategory.util.ReadExcelJobcategory;
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
public class JobcategoryServiceImpl implements JobcategoryService {
    private final JobcategoryRepository jobcategoryRepository;
    private final JobcategoryMapper jobcategoryMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobcategoryServiceImpl(JobcategoryRepository jobcategoryRepository, JobcategoryMapper jobcategoryMapper,
                                  ApplicationContext applicationContext) {
        this.jobcategoryRepository = jobcategoryRepository;
        this.jobcategoryMapper = jobcategoryMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public JobcategoryDTO create(JobcategoryDTO dto) {
        checkDataExist(dto);
        Jobcategory jobcategory = jobcategoryMapper.toEntity(dto);
        return jobcategoryMapper.toDTO(jobcategoryRepository.save(jobcategory));
    }

    @Override
    public void delete(String id) {
        Jobcategory entity = jobcategoryRepository.retrieve(id);
        jobcategoryRepository.delete(entity);
    }

    @Override
    public JobcategoryDTO update(JobcategoryDTO dto)
    {
        Jobcategory entity = jobcategoryRepository.retrieve(dto.getId());
        checkDataExist(dto,entity);
        jobcategoryMapper.updateEntityFromDTO(dto, entity);
        return jobcategoryMapper.toDTO(jobcategoryRepository.save(entity));
    }

    @Override
    public JobcategoryDTO retrieve(String id) {
        Jobcategory entity = jobcategoryRepository.retrieve(id);
        return jobcategoryMapper.toDTO(entity);
    }

    @Override
    public List<JobcategoryDTO> getBySearchTerm(String searchTerm, Sort sort)
    {
        String defaultSearchTerm = null;
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        List<Jobcategory> entities = jobcategoryRepository.getBySearchTerm(searchTerm, sort);
        return jobcategoryMapper.toDTOList(entities);
    }

    @Override
    public Page<JobcategoryDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        String defaultSearchTerm = null;
        searchTerm = getSearchTerm(searchTerm, defaultSearchTerm);
        Page<Jobcategory> entities = jobcategoryRepository.getBySearchTerm(searchTerm, pageable);
        return jobcategoryMapper.toDTOPage(pageable, entities);
    }
    @Override
    public Page<JobcategoryDTO> getBySearchTerm(String searchTerm,String jobType, Pageable pageable) {
        searchTerm = getSearchTerm(searchTerm, jobType);
        Page<Jobcategory> entities = jobcategoryRepository.getBySearchTerm(searchTerm, pageable);
        return jobcategoryMapper.toDTOPage(pageable, entities);
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelJobcategory readExcel = new ReadExcelJobcategory(applicationContext);
        //解析excel，获取上传的事件单
        List<JobcategoryDTO> jobcategoryDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(jobcategoryDTOList);
    }

    public void createImport(List<JobcategoryDTO> itemDataDTOList) {
        for (JobcategoryDTO jobcategoryDTO : itemDataDTOList) {
            checkDataExist(jobcategoryDTO);
            jobcategoryRepository.save(jobcategoryMapper.toEntity(jobcategoryDTO));
        }
    }

    @Override
    public List<JobcategoryDTO> findJobcategoryListByJobType(String jobType) {
        List<Jobcategory>  jobcategorys = jobcategoryRepository.findJobcategoryListByJobType(jobType);
        return jobcategoryMapper.toDTOList(jobcategorys);
    }

    @Override
    public List<JobcategoryDTO> findJobcategoryList() {
        List<Jobcategory>  jobcategorys = jobcategoryRepository.findJobcategoryList();
        return jobcategoryMapper.toDTOList(jobcategorys);
    }
    private void checkDataExist(JobcategoryDTO dto) {
        Jobcategory jobcategory = jobcategoryRepository.findJobcategoryByCode(dto.getCode());
        if (jobcategory != null) {
            throw new ApiException("TOT_CATERORY_EXISTS");
        }
    }

    private void checkDataExist(JobcategoryDTO dto,Jobcategory entity) {
        Jobcategory jobcategory = jobcategoryRepository.findJobcategoryByCode(dto.getCode());
        if (jobcategory != null&&!(dto.getCode().equals(entity.getCode()))) {
            throw new ApiException("TOT_CATERORY_EXISTS");
        }
    }
}
