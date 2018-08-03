package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;
import com.mushiny.wms.masterdata.mdbasics.service.StorageLocationService;
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
@RequestMapping("/masterdata/storage-locations")
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    @Autowired
    public StorageLocationController(StorageLocationService storageLocationService) {
        this.storageLocationService = storageLocationService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationDTO> create(@RequestBody StorageLocationDTO dto) {
        return ResponseEntity.ok(storageLocationService.create(dto));
    }
    @RequestMapping(value = "/import/file",
            method = RequestMethod.POST)
    public ResponseEntity<Void> importFile(@RequestParam("file") MultipartFile file) throws IOException {
        storageLocationService.importFile(file);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        storageLocationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StorageLocationDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(storageLocationService.retrieve(id));
    }
    @RequestMapping(value = "/exportname",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageLocationDTO>> getName() {
        return ResponseEntity.ok(storageLocationService.getName());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageLocationDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(storageLocationService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StorageLocationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(storageLocationService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<StorageLocationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(storageLocationService.getBySearchTerm(search, pageable));
    }
}
