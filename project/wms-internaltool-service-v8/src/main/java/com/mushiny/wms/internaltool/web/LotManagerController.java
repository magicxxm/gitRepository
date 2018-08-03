package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.service.LotManagerService;
import com.mushiny.wms.internaltool.web.dto.LotManagerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 123 on 2017/11/10.
 */
@RestController
@RequestMapping("/internal-tool")
public class LotManagerController {

    private final LotManagerService lotManagerService;

    public LotManagerController(LotManagerService lotManagerService){
        this.lotManagerService = lotManagerService;
    }

    //暂不用
    @RequestMapping(value = "findStock-all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LotManagerDTO>> getAllStock(){

        return ResponseEntity.ok(lotManagerService.getAllStockUnits());
    }

    @RequestMapping(value = "findStock",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<LotManagerDTO>> getStock(Pageable pageable){
        return ResponseEntity.ok(lotManagerService.getStockUnits(pageable));
    }

    @RequestMapping(value = "findByParam",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LotManagerDTO>> getByParam(@RequestParam String param){

        return ResponseEntity.ok(lotManagerService.getByParam(param));
    }
}
