package com.mushiny.wms.tot.attendance.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.domain.Attendance;

import java.util.List;

public interface AttendanceService extends BaseService<AttendanceDTO> {
    List<Attendance> findAttendanceListByEmployeeCode(String employeeCode,String currentWarehouseId);
}
