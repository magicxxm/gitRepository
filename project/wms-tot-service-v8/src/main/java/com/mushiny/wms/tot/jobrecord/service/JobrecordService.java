package com.mushiny.wms.tot.jobrecord.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.jobrecord.crud.dto.JobrecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobrecordService extends BaseService<JobrecordDTO> {
    JobrecordDTO getJobrecord(String employeeCode,String jobCode);

    Page<JobrecordDTO> getDetail(Pageable pageable, String employeeCode, String warehouseId, String clientId, String startTime, String endTime);

    List<JobrecordDTO> getTotal(String employeeCode, String warehouseId, String clientId,String startTime,String endTime);

    JobrecordDTO addJobrecord(String employeeCode,String dateTime,String jobCode);
}
