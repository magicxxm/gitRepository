package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.GoodsReceiptDTO;
import com.mushiny.wms.masterdata.ibbasics.service.AdviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/advice-requests")
public class AdviceRequestController {

    private final AdviceRequestService adviceRequestService;

    @Autowired
    public AdviceRequestController(AdviceRequestService adviceRequestService) {
        this.adviceRequestService = adviceRequestService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> create(@RequestBody AdviceRequestDTO dto) {
        return ResponseEntity.ok(adviceRequestService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        adviceRequestService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> update(@RequestBody AdviceRequestDTO dto) {
        return ResponseEntity.ok(adviceRequestService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(adviceRequestService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdviceRequestDTO>> getByClientId(@RequestParam String clientId) {
        return ResponseEntity.ok(adviceRequestService.getByClientId(clientId));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdviceRequestDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(adviceRequestService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AdviceRequestDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(adviceRequestService.getBySearchTerm(search, pageable));
    }
    @RequestMapping(value = "/lock/dn",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> updateDNLock(@RequestParam String id) {
        return ResponseEntity.ok(adviceRequestService.getByIdAndLockDN(id));
    }
}
