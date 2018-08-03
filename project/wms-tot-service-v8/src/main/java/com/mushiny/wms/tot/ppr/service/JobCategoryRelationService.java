package com.mushiny.wms.tot.ppr.service;


import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;
import com.mushiny.wms.tot.ppr.query.dto.JobCategoryRelationDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprMainPageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobCategoryRelationService extends BaseService<JobCategoryRelationDTO> {

    //用于ppr首页
    List<PprMainPageDTO> getJobCategoryRelations();
    //用于配置页面
    List<JobCategoryRelationDTO> getJobCategoryRelation();

    List<JobCategoryRelation> getRelationsByCategory(String category);

    void importFile(MultipartFile file);

}
