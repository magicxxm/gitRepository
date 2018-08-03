package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.MeasureRecord;
import com.mushiny.wms.internaltool.service.SearchMeasureGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal-tool/search-measure-goods")
public class SearchMeasureGoodsToolController {

    private final SearchMeasureGoodsService searchMeasureGoodsService;

    @Autowired
    public SearchMeasureGoodsToolController(SearchMeasureGoodsService searchMeasureGoodsService) {
        this.searchMeasureGoodsService = searchMeasureGoodsService;
    }

    @RequestMapping(value = "/records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeasureRecord>> getBySearchTerm(@RequestParam(required = false) String searchTerm,
                                                               @RequestParam(required = false) String startDate,
                                                               @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(searchMeasureGoodsService.getBySearchTerm(searchTerm, startDate, endDate));
    }
}
