package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.service.EntryLotService;
import com.mushiny.wms.internaltool.web.dto.EntryLotDTO;
import com.mushiny.wms.internaltool.web.dto.ItemDataAmountDTO;
import com.mushiny.wms.internaltool.web.dto.StorageLocationAmountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/internal-tool/entry-lot")
public class EntryLotToolController {

    private final EntryLotService entryLotService;

    @Autowired
    public EntryLotToolController(EntryLotService entryLotService) {
        this.entryLotService = entryLotService;
    }

    @RequestMapping(value = "/scanning/source",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationAmountDTO> scanningSource(@RequestParam String sourceName) {
        return ResponseEntity.ok(entryLotService.scanningSource(sourceName));
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataAmountDTO> scanningItemData(@RequestParam String sourceId,
                                                              @RequestParam String sku) {
        return ResponseEntity.ok(entryLotService.scanningItemData(sourceId, sku));
    }

    @RequestMapping(value = "/entering",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> entering(@RequestBody EntryLotDTO entryLotDTO) {
        return ResponseEntity.ok(entryLotService.entering(entryLotDTO));

    }
}
