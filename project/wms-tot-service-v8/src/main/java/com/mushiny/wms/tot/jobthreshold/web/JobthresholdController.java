package com.mushiny.wms.tot.jobthreshold.web;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.jobthreshold.crud.dto.JobthresholdDTO;
import com.mushiny.wms.tot.jobthreshold.service.JobthresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tot/jobthresholds")
//此类是阈值表页面 spring data jpa增删改查的合集controller
public class JobthresholdController {
    private final JobthresholdService jobthresholdService;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobthresholdController(JobthresholdService jobthresholdService, ApplicationContext applicationContext) {
        this.jobthresholdService = jobthresholdService;
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobthresholdDTO> create(@RequestBody JobthresholdDTO dto) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        return ResponseEntity.ok(jobthresholdService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobthresholdService.delete(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobthresholdDTO> update(@RequestBody JobthresholdDTO dto) {
        JobthresholdDTO jobthresholdDTO = jobthresholdService.update(dto);
        return ResponseEntity.ok(jobthresholdDTO);
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobthresholdDTO> get(@PathVariable String id) {
        JobthresholdDTO jobthresholdDTO = jobthresholdService.retrieve(id);
        return ResponseEntity.ok(jobthresholdDTO);
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobthresholdDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(jobthresholdService.getBySearchTerm(search, sort));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobthresholdDTO>> getBySearchTerm(
            @RequestParam(required = false) String search,Pageable pageable) {
        jobthresholdService.checkJobthreshold();
        return ResponseEntity.ok(jobthresholdService.getBySearchTerm(search,pageable));
    }
}
