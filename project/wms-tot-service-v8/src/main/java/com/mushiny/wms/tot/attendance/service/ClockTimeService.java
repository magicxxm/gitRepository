package com.mushiny.wms.tot.attendance.service;

import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.domain.Attendance;

import java.util.List;
import java.util.Map;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/1.
 */
public interface ClockTimeService {
    int  addClockTime(Attendance params);
    int  updateClockTime(Attendance params);
    int deleteClockTime(String keyId);
    List<Attendance> getClockTimes(String emCode,String begainTime,String endTime);


}
