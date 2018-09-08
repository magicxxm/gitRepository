package com.mushiny.wms.application.web;

import com.mushiny.wms.application.test.CommonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * Created by Tank.li on 2018/2/2.
 */
@RestController
@RequestMapping("/agvTest")
public class testController {
    @Autowired
    private CommonTest commonTest;



    @GetMapping(path = "/start")
    public ResponseEntity<Integer> test() throws IOException {
        commonTest.start(true);
        return ResponseEntity.ok(1);
    }
    @GetMapping(path = "/startAll")
    public ResponseEntity<Integer> testAll() throws IOException {
        commonTest.startAll(true);
        return ResponseEntity.ok(1);
    }
    @GetMapping(path = "/stop")
    public ResponseEntity<Integer> stop() throws IOException {
        commonTest.stop();
        return ResponseEntity.ok(1);
    }

    @GetMapping(path = "/autoRemove")
    public ResponseEntity<Integer> autoRemove() throws IOException  {
        commonTest.autoRemove(true);
        return ResponseEntity.ok(1);
    }
    @GetMapping(path = "/autoRemoveStop")
    public ResponseEntity<Integer> autoRemoveStop() throws IOException {
        commonTest.autoRemoveStop();
        return ResponseEntity.ok(1);
    }

}
