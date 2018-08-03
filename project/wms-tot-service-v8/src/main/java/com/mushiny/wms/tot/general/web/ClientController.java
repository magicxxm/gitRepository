package com.mushiny.wms.tot.general.web;

import com.mushiny.wms.tot.general.crud.dto.ClientDTO;
import com.mushiny.wms.tot.general.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tot/general/clients")
//各模块的公共部分client相关查询
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO dto) {
        return ResponseEntity.ok(clientService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        clientService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO dto) {
        return ResponseEntity.ok(clientService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(clientService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getByCurrentWarehouseId() {
        return ResponseEntity.ok(clientService.getByCurrentWarehouseId());
    }

    @RequestMapping(value = "/getClients",
            method = RequestMethod.GET,
            params = {"warehouseId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getByWarehouseId(@RequestParam String warehouseId) {
        return ResponseEntity.ok(clientService.getByWarehouseId(warehouseId));
    }

    @RequestMapping(value = "/getClientByCurrentWarehouse",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getClientByCurrentWarehouse() {
        return ResponseEntity.ok(clientService.getClientByCurrentWarehouse());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(clientService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ClientDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(clientService.getBySearchTerm(search, pageable));
    }
}
