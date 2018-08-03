package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.ItemDataGlobal;
import com.mushiny.wms.internaltool.service.PrintBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal-tool/print-barcode")
public class PrintBarcodeToolController {

    private final PrintBarcodeService printBarcodeService;

    @Autowired
    public PrintBarcodeToolController(PrintBarcodeService printBarcodeService) {
        this.printBarcodeService = printBarcodeService;
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemData>> scanningSKU(@RequestParam String sku) {
        return ResponseEntity.ok(printBarcodeService.scanningSKU(sku));
    }
}
