package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.GoodsReceiptDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.service.GoodsReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/masterdata/inbound/goods-receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    @Autowired
    public GoodsReceiptController(GoodsReceiptService goodsReceiptService) {
        this.goodsReceiptService = goodsReceiptService;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsReceiptDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(goodsReceiptService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GoodsReceiptDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(goodsReceiptService.getBySearchTerm(search, pageable));
    }
    @RequestMapping(value = "/closed/dn",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsReceiptDTO> updateDNClosed(@RequestParam String id) {
        return ResponseEntity.ok(goodsReceiptService.getByIdAndClosed(id));
    }
    @RequestMapping(value = "/open/dn",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsReceiptDTO> updateDNOpen(@RequestParam String id) {
        return ResponseEntity.ok(goodsReceiptService.getByIdAndOpen(id));
    }
}
