package com.mushiny.wms.tot.job.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.job.crud.dto.JobDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobService extends BaseService<JobDTO> {
    Page<JobDTO> getBySearchTerm(String searchTerm,String jobType, Pageable pageable);
    List<JobDTO> getJobByName(String name);
    String checkJobType(String jobCode);
    void importFile(MultipartFile file);
}
