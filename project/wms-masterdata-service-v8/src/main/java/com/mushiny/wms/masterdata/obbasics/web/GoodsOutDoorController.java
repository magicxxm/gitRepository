package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.GoodsOutDoorDTO;
import com.mushiny.wms.masterdata.obbasics.service.GoodsOutDoorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/goods-out-doors")
public class GoodsOutDoorController {

    private final GoodsOutDoorService goodsOutDoorService;

    @Autowired
    public GoodsOutDoorController(GoodsOutDoorService goodsOutDoorService) {
        this.goodsOutDoorService = goodsOutDoorService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsOutDoorDTO> create(@RequestBody GoodsOutDoorDTO dto) {
        return ResponseEntity.ok(goodsOutDoorService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        goodsOutDoorService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsOutDoorDTO> update(@RequestBody GoodsOutDoorDTO dto) {
        return ResponseEntity.ok(goodsOutDoorService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoodsOutDoorDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(goodsOutDoorService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsOutDoorDTO>> getAll() {
        return ResponseEntity.ok(goodsOutDoorService.getAll());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoodsOutDoorDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(goodsOutDoorService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GoodsOutDoorDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(goodsOutDoorService.getBySearchTerm(search, pageable));
    }
}
