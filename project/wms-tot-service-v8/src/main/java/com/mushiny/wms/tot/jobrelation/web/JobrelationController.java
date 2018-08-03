package com.mushiny.wms.tot.jobrelation.web;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.jobrelation.crud.dto.JobrelationDTO;
import com.mushiny.wms.tot.jobrelation.service.JobrelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tot/jobrelations")
//此类是关系表页面 spring data jpa增删改查的合集controller
public class JobrelationController {
    private final JobrelationService jobrelationService;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobrelationController(JobrelationService jobrelationService, ApplicationContext applicationContext) {
        this.jobrelationService = jobrelationService;
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobrelationDTO> create(@RequestBody JobrelationDTO dto) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        return ResponseEntity.ok(jobrelationService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobrelationService.delete(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobrelationDTO> update(@RequestBody JobrelationDTO dto) {
        JobrelationDTO jobrelationDTO = jobrelationService.update(dto);
        return ResponseEntity.ok(jobrelationDTO);
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobrelationDTO> get(@PathVariable String id) {
        JobrelationDTO jobrelationDTO = jobrelationService.retrieve(id);
        return ResponseEntity.ok(jobrelationDTO);
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobrelationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(jobrelationService.getBySearchTerm(search, sort));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobrelationDTO>> getBySearchTerm(
            @RequestParam(required = false) String search,Pageable pageable) {
        return ResponseEntity.ok(jobrelationService.getBySearchTerm(search,pageable));
    }
}
