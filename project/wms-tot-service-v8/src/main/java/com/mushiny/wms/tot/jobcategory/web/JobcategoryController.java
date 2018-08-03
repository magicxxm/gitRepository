package com.mushiny.wms.tot.jobcategory.web;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
import com.mushiny.wms.tot.jobcategory.domain.enums.JobType;
import com.mushiny.wms.tot.jobcategory.service.JobcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tot/jobcategorys")
public class JobcategoryController {
    private final JobcategoryService jobcategoryService;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobcategoryController(JobcategoryService jobcategoryService, ApplicationContext applicationContext) {
        this.jobcategoryService = jobcategoryService;
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/import/file",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂时没有用到
    public ResponseEntity<Void> importFile(@RequestParam("file") MultipartFile file) throws IOException {
        jobcategoryService.importFile(file);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //直接间接项目页面的新增按钮
    public ResponseEntity<JobcategoryDTO> create(@RequestBody JobcategoryDTO dto) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        if (!StringUtils.isEmpty(warehouseId)) {
            dto.setWarehouseId(warehouseId);
        }
        return ResponseEntity.ok(jobcategoryService.create(dto));
    }
    @RequestMapping(value = "/getJobcategorys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //间接工作页面查询
    public ResponseEntity<List<JobcategoryDTO>> getJobcategorys() {
        return  ResponseEntity.ok(jobcategoryService.findJobcategoryListByJobType(JobType.INDIRECT.name()));
    }
    @RequestMapping(value = "/getDJobcategorys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //直接项目页面查询
    public ResponseEntity<List<JobcategoryDTO>> getDJobcategorys() {
        return  ResponseEntity.ok(jobcategoryService.findJobcategoryListByJobType(JobType.DIRECT.name()));
    }
    @RequestMapping(value = "/getAllJobcategorys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂无用
    public ResponseEntity<List<JobcategoryDTO>> getAllJobcategory() {
        return  ResponseEntity.ok(jobcategoryService.findJobcategoryList());
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //直接间接项目页面的删除按钮
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobcategoryService.delete(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //直接间接项目页面的修改按钮
    public ResponseEntity<JobcategoryDTO> update(@RequestBody JobcategoryDTO dto) {
        JobcategoryDTO jobcategoryDTO = jobcategoryService.update(dto);
        return ResponseEntity.ok(jobcategoryDTO);
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobcategoryDTO> get(@PathVariable String id) {
        JobcategoryDTO jobcategoryDTO = jobcategoryService.retrieve(id);
        return ResponseEntity.ok(jobcategoryDTO);
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobcategoryDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(jobcategoryService.getBySearchTerm(search, sort));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobcategoryDTO>> getBySearchTerm(
            @RequestParam(required = false) String search,@RequestParam String jobType, Pageable pageable) {
        return ResponseEntity.ok(jobcategoryService.getBySearchTerm(search,jobType, pageable));
    }
}
