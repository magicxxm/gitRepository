package com.mushiny.wms.tot.jobthreshold.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.jobthreshold.crud.mapper.JobthresholdMapper;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import com.mushiny.wms.tot.jobthreshold.repository.JobthresholdRepository;
import com.mushiny.wms.tot.jobthreshold.service.JobthresholdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class JobthresholdServiceImpl implements JobthresholdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobthresholdServiceImpl.class);

    private final JobthresholdRepository JobthresholdRepository;
    private final JobthresholdMapper JobthresholdMapper;
    private final ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JobthresholdServiceImpl(JobthresholdRepository JobthresholdRepository, JobthresholdMapper JobthresholdMapper,
                                   ApplicationContext applicationContext) {
        this.JobthresholdRepository = JobthresholdRepository;
        this.JobthresholdMapper = JobthresholdMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public JobthresholdDTO create(JobthresholdDTO dto) {
        Jobthreshold Jobthreshold = JobthresholdMapper.toEntity(dto);
        return JobthresholdMapper.toDTO(JobthresholdRepository.save(Jobthreshold));
    }

    @Override
    public void delete(String id) {
        Jobthreshold entity = JobthresholdRepository.retrieve(id);
        JobthresholdRepository.delete(entity);
    }

    @Override
    public JobthresholdDTO update(JobthresholdDTO dto)
    {
        if (dto.getThresholdA()>=dto.getThresholdB()||dto.getThresholdA()<1||dto.getThresholdB()<1) {
            throw new ApiException("TOT_JOBTHRESHOLD_ERROR");
        }
        Jobthreshold entity = JobthresholdRepository.retrieve(dto.getId());
        JobthresholdMapper.updateEntityFromDTO(dto, entity);
        return JobthresholdMapper.toDTO(JobthresholdRepository.save(entity));
    }

    @Override
    public JobthresholdDTO retrieve(String id) {
        Jobthreshold entity = JobthresholdRepository.retrieve(id);
        return JobthresholdMapper.toDTO(entity);
    }

    @Override
    public List<JobthresholdDTO> getBySearchTerm(String searchTerm, Sort sort)
    {
        List<Jobthreshold> entities = JobthresholdRepository.getBySearchTerm(searchTerm, sort);
        return JobthresholdMapper.toDTOList(entities);
    }

    @Override
    public Page<JobthresholdDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        searchTerm = getSearchTerm(searchTerm, "warehouseId=="+warehouseId);
        Page<Jobthreshold> entities = JobthresholdRepository.getBySearchTerm(searchTerm, pageable);
        return JobthresholdMapper.toDTOPage(pageable, entities);
    }

    @Override
    public void checkJobthreshold() {
        String warehouseId = applicationContext.getCurrentWarehouse();
        Jobthreshold jobthreshold = JobthresholdRepository.getbyWarehouseId(warehouseId);
        if (null == jobthreshold) {
            Jobthreshold insertJobthreshold = new Jobthreshold();
            insertJobthreshold.setThresholdA(10);
            insertJobthreshold.setThresholdB(20);
            insertJobthreshold.setWarehouseId(warehouseId);
            try {
                entityManager.persist(insertJobthreshold);
            } catch (Exception e) {
                LOGGER.error("插入失败"+e.getMessage(),e);
                throw new ApiException("JOBTHRESHOLD_ERROR");
            }
        }
    }
}
