package com.mushiny.wms.tot.ppr.web;

import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.tot.ppr.query.dto.PprDetailOfJobDTO;
import com.mushiny.wms.tot.ppr.query.dto.PprMainPageDTO;
import com.mushiny.wms.tot.ppr.query.enums.DateType;
import com.mushiny.wms.tot.ppr.service.PprService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/tot/ppr")
public class PprController {
    private final PprService pprService;
    @Autowired
    public PprController(PprService pprService) {
        this.pprService = pprService;
    }

    @RequestMapping(value = "/getWeekOfMonth",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //日历控件换算触发
    public ResponseEntity<Map<String,String>> getRecordsForPpr(@RequestParam(required = false) String dateTime) throws ParseException {

        Map <String,String> result=new HashMap<>();
        result.put("srcDate",dateTime);
        result.put("convertDate",DateTimeUtil.getWeekOf(dateTime));

        return ResponseEntity.ok(result);
    }
    @RequestMapping(value = "/recordsForPpr",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //工作汇总按钮触发事件
    public ResponseEntity<List<PprMainPageDTO>> getRecordsForPpr(@RequestParam(required = false) String warehouseId,
                                                                 @RequestParam(required = false)String clientId,
                                                                 @RequestParam(required = false)String dayDate,
                                                                 @RequestParam(required = false)String startDate,
                                                                 @RequestParam(required = false)String endDate,
                                                                 @RequestParam(required = false)int dateType
                                                                ) throws ParseException {
        if(dateType== DateType.DAY.getValue())
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        if(dateType==DateType.WEEK.getValue())
        {
            startDate= DateTimeUtil.getFirstDayOfWeek(dayDate);
            endDate=DateTimeUtil.getLastDayOfWeek(dayDate);

        }
        if(dateType==DateType.MONTH.getValue()){
            startDate= DateTimeUtil.getFirstDayOfMonth(dayDate);
            endDate=DateTimeUtil.getLastDayOfMonth(dayDate);
        }
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
        Date date1 = DateTimeUtil.getDateforStr(startDate.replace("T", " "));
        if (dateNow.before(date2)) {
            endDate = dateFormat.format(dateNow);
        }
        if (dateNow.before(date1)) {
            startDate = dateFormat.format(dateNow);
        }
        return ResponseEntity.ok(pprService.getRecordsForPpr(warehouseId,clientId,startDate,endDate));
    }

    @RequestMapping(value = "/recordsForPprDetail",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //工作详情按钮触发事件
    public ResponseEntity<List> getRecordsForPprDetail(@RequestParam(required = false)String warehouseId,
                                                                          @RequestParam(required = false)String clientId,
                                                                          @RequestParam(required = false)String category,
                                                                          @RequestParam(required = false)String dayDate,
                                                                          @RequestParam(required = false)String startDate,
                                                                          @RequestParam(required = false)String endDate,
                                                                          @RequestParam(required = false)int dateType
    ) throws ParseException {
        if(dateType== DateType.DAY.getValue())
        {
            startDate= DateTimeUtil.getFirstTimeOfDay(dayDate);
            endDate=DateTimeUtil.getLastTimeOfDay(dayDate);
        }
        if(dateType==DateType.WEEK.getValue())
        {
            startDate= DateTimeUtil.getFirstDayOfWeek(dayDate);
            endDate=DateTimeUtil.getLastDayOfWeek(dayDate);

        }
        if(dateType==DateType.MONTH.getValue()){
            startDate= DateTimeUtil.getFirstDayOfMonth(dayDate);
            endDate=DateTimeUtil.getLastDayOfMonth(dayDate);
        }
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = DateTimeUtil.getDateforStr(endDate.replace("T", " "));
        Date date1 = DateTimeUtil.getDateforStr(startDate.replace("T", " "));
        if (dateNow.before(date2)) {
            endDate = dateFormat.format(dateNow);
        }
        if (dateNow.before(date1)) {
            startDate = dateFormat.format(dateNow);
        }
        return ResponseEntity.ok(pprService.getRecordsForPprDetail(warehouseId,clientId,category,startDate,endDate));
    }
}
