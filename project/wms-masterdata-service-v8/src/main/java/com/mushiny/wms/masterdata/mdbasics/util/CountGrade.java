package com.mushiny.wms.masterdata.mdbasics.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Laptop-11 on 2017/12/27.
 */
@Component
public class CountGrade {
   private final CountGradeService countGradeService;
    @Autowired
    public CountGrade(CountGradeService countGradeService) {
       this.countGradeService = countGradeService;
    }
//    @Scheduled(cron = "50 5 10 * * ?")
    public ResponseEntity<Void> getRule() {
        countGradeService.getRule();
        return ResponseEntity.ok().build();
    }
}
