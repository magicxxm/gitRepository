package com.mushiny.wms.tot.ppr.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.jobrelation.domain.Jobrelation;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.ppr.query.dto.JobCategoryRelationDTO;
import com.mushiny.wms.tot.ppr.query.dto.JobCategoryRelationMapper;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import com.mushiny.wms.tot.ppr.query.dto.PprMainPageDTO;
import com.mushiny.wms.tot.ppr.query.mapper.JobCategoryRelationNewMapper;
import com.mushiny.wms.tot.ppr.repository.JobCategoryRelationRepository;
import com.mushiny.wms.tot.ppr.service.JobCategoryRelationService;
import com.mushiny.wms.tot.ppr.util.ReadExcelPlanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class JobCategoryRelationServiceImpl implements JobCategoryRelationService {
    private final JobCategoryRelationRepository repository;
    private final JobCategoryRelationMapper jobCategoryRelationMapper;
    private final JobCategoryRelationNewMapper jobCategoryRelationNewMapper;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public JobCategoryRelationServiceImpl(JobCategoryRelationRepository jobrelationRepository,
                                          JobCategoryRelationMapper mapper,
                                          JobCategoryRelationNewMapper jobCategoryRelationNewMapper) {
        this.repository = jobrelationRepository;
        this.jobCategoryRelationMapper = mapper;
        this.jobCategoryRelationNewMapper = jobCategoryRelationNewMapper;
    }

    @Override
    public List<JobCategoryRelation> getRelationsByCategory(String category) {
        List<JobCategoryRelation> jobCategoryRelation = repository.getRelations(category);
        return jobCategoryRelation;
    }

    @Override
    public List<PprMainPageDTO> getJobCategoryRelations() {
        List<PprMainPageDTO> result=null;
        List<JobCategoryRelation>jobCategoryRelations=repository.getJobCategoryRelations();
        if(!CollectionUtils.isEmpty(jobCategoryRelations))
        {
            result=jobCategoryRelationMapper.toDTOList(jobCategoryRelations);
        }
        return result;
    }

    @Override
    public List<JobCategoryRelationDTO> getJobCategoryRelation() {
        List<JobCategoryRelationDTO> result=null;
        List<JobCategoryRelation>jobCategoryRelations=repository.getJobCategoryRelations();
        if(!CollectionUtils.isEmpty(jobCategoryRelations))
        {
            result=jobCategoryRelationNewMapper.toDTOList(jobCategoryRelations);
        }
        return result;
    }

    @Override
    public JobCategoryRelationDTO create(JobCategoryRelationDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public JobCategoryRelationDTO update(JobCategoryRelationDTO dto) {
        JobCategoryRelation entity = repository.retrieve(dto.getId());
        jobCategoryRelationNewMapper.updateEntityFromDTO(dto, entity);

        return jobCategoryRelationNewMapper.toDTO(repository.save(entity));
    }

    @Override
    public JobCategoryRelationDTO retrieve(String id) {
        JobCategoryRelation entity = repository.retrieve(id);
        return jobCategoryRelationNewMapper.toDTO(entity);
    }

    @Override
    public List<JobCategoryRelationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        return null;
    }

    @Override
    public Page<JobCategoryRelationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelPlanConfig readExcel = new ReadExcelPlanConfig();
        //解析excel，获取上传的事件单
        List<JobCategoryRelationDTO> itemDataDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(itemDataDTOList);
    }

    public void createImport(List<JobCategoryRelationDTO> itemDataDTOList) {
        for (JobCategoryRelationDTO itemDataDTO : itemDataDTOList) {
            repository.save(jobCategoryRelationNewMapper.toEntity(itemDataDTO));
        }
    }
}
