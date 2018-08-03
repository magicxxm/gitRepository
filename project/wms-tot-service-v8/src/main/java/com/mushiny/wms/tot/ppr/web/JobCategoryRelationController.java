package com.mushiny.wms.tot.ppr.web;

import com.mushiny.wms.tot.ppr.query.dto.JobCategoryRelationDTO;
import com.mushiny.wms.tot.ppr.service.JobCategoryRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tot/pprplanconfigs")
public class JobCategoryRelationController {
    private final JobCategoryRelationService service;

    @Autowired
    public JobCategoryRelationController(JobCategoryRelationService jobCategoryRelationService) {
        this.service = jobCategoryRelationService;
    }

    @RequestMapping(value = "/import/file",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂时没用
    public ResponseEntity<Void> importFile(@RequestParam("file") MultipartFile file) throws IOException {
        service.importFile(file);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/getRelation", method = RequestMethod.GET)
    //工作计划页面查询触发
    public ResponseEntity<List<JobCategoryRelationDTO>> getRelation() {
        List<JobCategoryRelationDTO> result = service.getJobCategoryRelation();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //工作计划页面修改按钮触发
    public ResponseEntity<JobCategoryRelationDTO> update(@RequestBody JobCategoryRelationDTO dto) {
        JobCategoryRelationDTO jobCategoryRelationDTO = service.update(dto);
        return ResponseEntity.ok(jobCategoryRelationDTO);
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂无用
    public ResponseEntity<JobCategoryRelationDTO> get(@PathVariable String id) {
        JobCategoryRelationDTO jobCategoryRelationDTO = service.retrieve(id);
        return ResponseEntity.ok(jobCategoryRelationDTO);
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂无用
    public ResponseEntity<List<JobCategoryRelationDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(service.getBySearchTerm(search, sort));
    }

}
