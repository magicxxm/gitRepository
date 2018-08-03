package com.mushiny.wms.tot.report.web;

import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.report.bussiness.UserBussness;
import com.mushiny.wms.tot.report.query.dto.JobTypeDTO;
import com.mushiny.wms.tot.report.query.dto.NewStatisticsDTO;
import com.mushiny.wms.tot.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
@RestController
@RequestMapping("/tot/report")
public class ReportController {
    private final ReportService reportService;
    @Autowired
    private UserBussness bussiness;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @RequestMapping(value = "/statisticsData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //员工打卡两个按钮都触发此方法
    public ResponseEntity<List<NewStatisticsDTO>> getStatisticsData(@RequestParam(required = false) String warehouseId,
                                                                    @RequestParam(required = false)String clientId,
                                                                    @RequestParam(required = false)String dayDate,
                                                                    @RequestParam(required = false)String startDate,
                                                                    @RequestParam(required = false)String endDate,
                                                                    @RequestParam(required = false)String employeeCode) throws ParseException {
        if(!StringUtils.isEmpty(dayDate))
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        return ResponseEntity.ok(reportService.getStatistics(warehouseId,clientId,dayDate,startDate,endDate,employeeCode));
    }

    @RequestMapping(value = "/ctimedetailData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //点击员工打卡页面的蓝色员工姓名超链接触发
    public ResponseEntity getCtimedetailData(@RequestParam(required = false) String warehouseId,
                                                                   @RequestParam(required = false)String clientId,
                                                                   @RequestParam(required = false)String dayDate,
                                                                   @RequestParam(required = false)String startDate,
                                                                   @RequestParam(required = false)String endDate,
                                                                   @RequestParam(required = false)String employeeCode) throws ParseException {

        if(!StringUtils.isEmpty(dayDate))
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        if(StringUtils.isEmpty(startDate)||StringUtils.isEmpty(endDate))
        {
            return  ResponseEntity.ok(Collections.EMPTY_LIST);
        }else{
            return ResponseEntity.ok(reportService.getCtimedetail(warehouseId,clientId,startDate,endDate,dayDate,employeeCode));
        }


    }

    @RequestMapping(value = "/getUserInfo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //无用方法
    public ResponseEntity<UserDTO> getUserInfo(@RequestParam(required = false)String employeeCode){
        return ResponseEntity.ok(reportService.getUserInfo(employeeCode));
    }
    @RequestMapping(value = "/checkUserInfo",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //间接打卡输入员工号回车触发的方法
    public ResponseEntity<UserDTO> checkUserInfo(@RequestParam(required = false)String employeeCode){
        return ResponseEntity.ok(reportService.checkUserInfo(employeeCode));
    }
    @RequestMapping(value = "/getDJobType/{typeTable}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //直接项目页面新增修改页面下拉框查询方法
    public ResponseEntity<List<JobTypeDTO>> getDJobType(@PathVariable String typeTable) {
        return  ResponseEntity.ok(reportService.getDJobType(typeTable));
    }

    @RequestMapping(value = "/getWareHouseAndClient",
            method = RequestMethod.GET)
    //无用方法
    public ResponseEntity<Object> getWareHouseAndClient(@RequestParam("userCode")  String params) {
        return  ResponseEntity.ok(bussiness.getWareHouseAndClient(params));
    }
}
