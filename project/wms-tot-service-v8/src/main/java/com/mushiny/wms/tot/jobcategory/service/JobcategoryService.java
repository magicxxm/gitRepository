package com.mushiny.wms.tot.jobcategory.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobcategoryService extends BaseService<JobcategoryDTO> {
 List<JobcategoryDTO> findJobcategoryListByJobType(String jobType);

 List<JobcategoryDTO> findJobcategoryList();

 Page<JobcategoryDTO> getBySearchTerm(String searchTerm,String jobType, Pageable pageable);

 void importFile(MultipartFile file);
}
