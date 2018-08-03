package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.general.crud.dto.ClientDTO;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceDTO;
import com.mushiny.wms.masterdata.obbasics.business.dto.SemblenceListDTO;
import com.mushiny.wms.masterdata.obbasics.domain.Semblence;
import com.mushiny.wms.masterdata.obbasics.service.SemblenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by prestonmax on 2017/6/1.
 */
@RestController
@RequestMapping(value = "/masterdata/outbound/")
public class SemblenceController {
    private final SemblenceService semblenceService;

    @Autowired
    public SemblenceController(SemblenceService semblenceService,
                               ClientRepository clientRepository) {
        this.semblenceService = semblenceService;
    }

    @GetMapping(value = "getallsemblence",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SemblenceListDTO>> getAll(){
        return ResponseEntity.ok(semblenceService.getAll());
    }

    @GetMapping(value = "getbysearch",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Semblence> getBySearch(@RequestParam("search")String search){
        return ResponseEntity.ok(semblenceService.getBySerch(search));
    }

    @GetMapping(value = "semblence/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SemblenceDTO> getSemblence(@PathVariable String id){
        return ResponseEntity.ok(semblenceService.getById(id));
    }

    @PostMapping(value = "semblenceupdate",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@RequestBody SemblenceDTO semblenceDTO){
        semblenceService.update(semblenceDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "postsemblence",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postSemblence(@RequestBody SemblenceDTO semblenceDTO){
        semblenceService.insertSemblence(semblenceDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "delete/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSemblence(@PathVariable String id){
        semblenceService.deleteSemblence(id);
        return ResponseEntity.ok().build();
    };

    @GetMapping(value = "getallclient",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTO>> getAllClient(){
        return ResponseEntity.ok(semblenceService.getAllClient());
    }

}
