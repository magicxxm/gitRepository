package com.mushiny.wms.tot.attendance.web;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.attendance.crud.dto.AttendanceDTO;
import com.mushiny.wms.tot.attendance.domain.Attendance;
import com.mushiny.wms.tot.attendance.domain.enums.ClockMethod;
import com.mushiny.wms.tot.attendance.domain.enums.ClockType;
import com.mushiny.wms.tot.attendance.service.AttendanceService;
import com.mushiny.wms.tot.attendance.service.ClockTimeService;
import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 上下班打卡控制层
 */
@RestController
@RequestMapping("/tot/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final UserService userService;
    private final ClockTimeService clockTimeService;
    private final ApplicationContext applicationContext;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, UserService userService,
                                ClockTimeService service,ApplicationContext applicationContext) {
        this.attendanceService = attendanceService;
        this.userService = userService;
        this.clockTimeService=service;
        this.applicationContext = applicationContext;
    }
    @RequestMapping(value = "/createAttendance/check",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //上下班打卡页面回车触发
    public ResponseEntity<AttendanceDTO> create(@RequestParam String employeeCode) {
        AttendanceDTO dto = new AttendanceDTO();
        //获取当前库房
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
        User user = userService.findByUsername(employeeCode,currentWarehouseId);
        //判断对应库房是否有当前用户
        if (user == null) {
            throw new ApiException("TOT_EMPLOYEECODE_ERROR");
        }
        List<Attendance> attendances = attendanceService.findAttendanceListByEmployeeCode(employeeCode,currentWarehouseId);
        if (attendances.size() > 0 && !attendances.isEmpty() && attendances != null) {
            Attendance  lastAtt = attendances.get(0);
            if (ClockType.CLOCK_IN.name().equals(lastAtt.getClockType())) {
                dto.setClockType(ClockType.CLOCK_OFF.name());
            } else {
                dto.setClockType(ClockType.CLOCK_IN.name());
            }
        } else {
            dto.setClockType(ClockType.CLOCK_IN.name());
        }
        dto.setEmployeeCode(user.getUsername());
        dto.setEmployeeName(user.getName());
        dto.setClockTime(DateTimeUtil.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dto.setClockMethod(ClockMethod.HARD.name());
        dto.setWarehouseId(currentWarehouseId);
        dto.setClientId("SYSTEM");
        return ResponseEntity.ok(attendanceService.create(dto));
    }

    @RequestMapping(value = "/getColckTimes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //更改打卡时间页面的获取打卡记录
    public ResponseEntity<List<Attendance>> getColckTimes(@RequestParam String emCode,@RequestParam String beginTime,@RequestParam String endTime,@RequestParam String dayDate) {
        if(!StringUtils.isEmpty(dayDate))
        {
            beginTime=DateTimeUtil.getFirstTimeOfDay(dayDate);
            endTime=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        //库房限制
        List<Attendance> result=clockTimeService.getClockTimes(emCode, beginTime, endTime);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(value = "/deleteColckTimes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //更改打卡时间删除打卡记录触发
    public ResponseEntity<Integer> deleteColckTimes(@RequestParam String keyId) {
        int result=clockTimeService.deleteClockTime(keyId);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(value = "/addColckTimes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //更改打卡时间页面的补打卡操作
    public ResponseEntity<Integer> addColckTimes(Attendance att) {

        int result=clockTimeService.addClockTime(att);
        return ResponseEntity.ok(result);
    }
}
