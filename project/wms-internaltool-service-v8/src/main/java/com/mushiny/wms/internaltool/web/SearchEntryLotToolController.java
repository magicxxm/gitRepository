package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.LotRecord;
import com.mushiny.wms.internaltool.service.SearchEntryLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal-tool/search-entry-lot")
public class SearchEntryLotToolController {

    private final SearchEntryLotService searchEntryLotService;

    @Autowired
    public SearchEntryLotToolController(SearchEntryLotService searchEntryLotService) {
        this.searchEntryLotService = searchEntryLotService;
    }

    @RequestMapping(value = "/records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LotRecord>> getBySearchTerm(@RequestParam(required = false) String searchTerm,
                                                           @RequestParam(required = false) String startDate,
                                                           @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(searchEntryLotService.getBySearchTerm(searchTerm, startDate, endDate));
    }
}
