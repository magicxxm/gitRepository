package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickPackCellTypeObBoxTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PickPackCellTypeObBoxTypeController {

    private final PickPackCellTypeObBoxTypeService pickPackCellTypeObBoxTypeService;

    @Autowired
    public PickPackCellTypeObBoxTypeController(PickPackCellTypeObBoxTypeService pickPackCellTypeObBoxTypeService) {
        this.pickPackCellTypeObBoxTypeService = pickPackCellTypeObBoxTypeService;
    }

    @RequestMapping(value = "/masterdata/outbound/pick-pack-cell-types/{id}/pick-pack-cell-types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPickPackCellTypeObBoxType(@PathVariable String id,
                                                             @RequestBody List<String> users) {
        pickPackCellTypeObBoxTypeService.createPickPackCellTypeObBoxType(id, users);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/masterdata/outbound/pick-pack-cell-type/pick-pack-cell-types",
            method = RequestMethod.GET,
            params = {"clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickPackCellTypeDTO>> getList(@RequestParam String clientId) {
        return ResponseEntity.ok(pickPackCellTypeObBoxTypeService.getList(clientId));
    }

    @RequestMapping(value = "/masterdata/outbound/pick-pack-cell-types/pick-pack-cell-types/assigned",
            method = RequestMethod.GET,
            params = {"id","clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoxTypeDTO>> getAssignedUserByReBinCellTypeId(@RequestParam String id,String clientId) {
        return ResponseEntity.ok(pickPackCellTypeObBoxTypeService.getAssignedUserByReBinCellTypeId(id,clientId));
    }

    @RequestMapping(value = "/masterdata/outbound/pick-pack-cell-types/pick-pack-cell-types/unassigned",
            method = RequestMethod.GET,
            params = {"id","clientId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoxTypeDTO>> getUnassignedUserByReBinCellTypeId(@RequestParam String id,String clientId) {
        return ResponseEntity.ok(pickPackCellTypeObBoxTypeService.getUnassignedUserByReBinCellTypeId(id,clientId));
    }
}
