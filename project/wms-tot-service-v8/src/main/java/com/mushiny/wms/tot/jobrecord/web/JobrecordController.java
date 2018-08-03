package com.mushiny.wms.tot.jobrecord.web;

import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.jobrecord.crud.dto.JobrecordDTO;
import com.mushiny.wms.tot.jobrecord.service.JobrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tot")
public class JobrecordController {
    private final JobrecordService jobrecordService;
    @Autowired
    public JobrecordController(JobrecordService jobrecordService) {
        this.jobrecordService = jobrecordService;
    }

    @RequestMapping(value = "/jobrecord/getDetail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //人员管理 工作详情页面Detail数据
    public ResponseEntity<Page<JobrecordDTO>> getDetail(Pageable pageable,
                                                        @RequestParam(required = false) String employeeCode,
                                                        @RequestParam(required = false) String warehouseId,
                                                        @RequestParam(required = false) String clientId,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate,@RequestParam(required = false) String dayDate) {
        if(!StringUtils.isEmpty(dayDate))
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        return ResponseEntity.ok(jobrecordService.getDetail(pageable,employeeCode,warehouseId,clientId,startDate,endDate));
    }

    @RequestMapping(value = "/jobrecord/getTotal",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //人员管理 工作详情页面Total数据
    public ResponseEntity<List<JobrecordDTO>> getTotal(@RequestParam(required = false) String employeeCode,
                                                       @RequestParam(required = false) String warehouseId,
                                                       @RequestParam(required = false) String clientId,
                                                       @RequestParam(required = false) String startDate,
                                                       @RequestParam(required = false) String endDate,@RequestParam(required = false) String dayDate) {
        if(!StringUtils.isEmpty(dayDate))
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        return ResponseEntity.ok(jobrecordService.getTotal(employeeCode,warehouseId,clientId,startDate,endDate));
    }

    @RequestMapping(value = "/jobrecord/scanJobrecord",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //间接工作打卡界面回车触发
    public ResponseEntity<JobrecordDTO> scanJobrecord(@RequestParam(required = false) String employeeCode,@RequestParam(required = false) String jobCode) {
        JobrecordDTO jobrecordDTO = jobrecordService.getJobrecord(employeeCode,jobCode);
        return ResponseEntity.ok(jobrecordDTO);
    }

    @RequestMapping(value = "/jobrecord/addJobrecord",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //打卡工作详情里的甘特图的间接工作条或无工作条点击弹出框submit按钮触发
    public ResponseEntity<JobrecordDTO> addJobrecord(@RequestParam(required = false) String employeeCode,
                                                     @RequestParam(required = false) String dateTime,
                                                     @RequestParam(required = false) String jobCode) {
        JobrecordDTO jobrecordDTO = jobrecordService.addJobrecord(employeeCode,dateTime,jobCode);
        return ResponseEntity.ok(jobrecordDTO);
    }

    @RequestMapping(value = "/jobrecords",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂时没用
    public ResponseEntity<Page<JobrecordDTO>> getBySearchTerm(
            @RequestParam(required = false) String search,Pageable pageable) {
        return ResponseEntity.ok(jobrecordService.getBySearchTerm(search,pageable));
    }

}
