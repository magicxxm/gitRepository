package com.mushiny.web;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.StockService;
import com.mushiny.web.dto.StocktakingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 123 on 2018/2/2.
 */
@RestController
@RequestMapping("/wms/mushiny")
public class StocktakingController {

    private final StockService stockService;

    @Autowired
    public StocktakingController(StockService stockService){
        this.stockService = stockService;
    }

    @RequestMapping(value = "/stocktaking/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> create(@RequestBody StocktakingDTO dto) {

        AccessDTO accessDTO = stockService.createStock(dto);

        return ResponseEntity.ok(accessDTO);
    }
}
