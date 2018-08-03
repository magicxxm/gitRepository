package com.mushiny.wms.tot.report.service;

import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;
import com.mushiny.wms.tot.report.query.dto.CTimeDetailDTO;
import com.mushiny.wms.tot.report.query.dto.JobTypeDTO;
import com.mushiny.wms.tot.report.query.dto.NewStatisticsDTO;

import java.text.ParseException;
import java.util.List;

public interface ReportService {

    List<NewStatisticsDTO> getStatistics(String warehouseId, String clientId, String dayDate, String startDate, String endDate, String userName) throws ParseException;

    List<CTimeDetailDTO> getCtimedetail(String warehouseId,String clientId,String startDate,String endDate,String dayDate,String userName) throws ParseException;

    UserDTO getUserInfo(String employeeCode);

    UserDTO checkUserInfo(String employeeCode);

    List<JobTypeDTO>getDJobType(String typeTable);

//    Jobthreshold getThreshold();
}
