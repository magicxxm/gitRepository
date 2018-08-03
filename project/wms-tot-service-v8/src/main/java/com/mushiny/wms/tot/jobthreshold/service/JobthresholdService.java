package com.mushiny.wms.tot.jobthreshold.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;

public interface JobthresholdService extends BaseService<JobthresholdDTO> {
    void checkJobthreshold();
}
