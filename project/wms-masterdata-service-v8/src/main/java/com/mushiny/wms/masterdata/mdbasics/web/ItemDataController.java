package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import com.mushiny.wms.masterdata.mdbasics.service.ItemDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/masterdata/item-datas")
public class ItemDataController {

    private final ItemDataService itemDataService;

    @Autowired
    public ItemDataController(ItemDataService itemDataService) {
        this.itemDataService = itemDataService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataDTO> create(@RequestBody ItemDataDTO dto) {
        return ResponseEntity.ok(itemDataService.create(dto));
    }

    @RequestMapping(value = "/import/file",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> importFile(@RequestParam("file") MultipartFile file) throws IOException {
        itemDataService.importFile(file);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        itemDataService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataDTO> update(@RequestBody ItemDataDTO dto) {
        return ResponseEntity.ok(itemDataService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(itemDataService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(itemDataService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDataDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(itemDataService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ItemDataDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(itemDataService.getBySearchTerm(search, pageable));
    }
}
