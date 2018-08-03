package com.mushiny.wms.tot.job.web;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.tot.job.crud.dto.JobDTO;
import com.mushiny.wms.tot.job.service.JobService;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;
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
@RequestMapping("/tot/jobs")
//此类是直接工作间接工作页面 spring data jpa增删改查的合集controller (未注释的controller就是crud)
public class JobController {
    private final JobService jobService;
    private final ApplicationContext applicationContext;

    @Autowired
    public JobController(JobService jobService, ApplicationContext applicationContext) {
        this.jobService = jobService;
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/import/file",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //暂时没有用到
    public ResponseEntity<Void> importFile(@RequestParam("file") MultipartFile file) throws IOException {
        jobService.importFile(file);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobDTO> create(@RequestBody JobDTO dto) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        if (!StringUtils.isEmpty(warehouseId)) {
            dto.setWarehouseId(warehouseId);
        }
        return ResponseEntity.ok(jobService.create(dto));
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobService.delete(id);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobDTO> update(@RequestBody JobDTO dto) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        if (!StringUtils.isEmpty(warehouseId)) {
            dto.setWarehouseId(warehouseId);
        }
        JobDTO jobDTO = jobService.update(dto);
        return ResponseEntity.ok(jobDTO);
    }
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobDTO> get(@PathVariable String id) {
        JobDTO jobDTO = jobService.retrieve(id);
        return ResponseEntity.ok(jobDTO);
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = "search",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(jobService.getBySearchTerm(search, sort));
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<JobDTO>> getBySearchTerm(
            @RequestParam(required = false) String search,@RequestParam String jobType, Pageable pageable) {
        return ResponseEntity.ok(jobService.getBySearchTerm(search,jobType, pageable));
    }

    @RequestMapping(value = "/getJobByName",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //人员管理详情页面的甘特图点击的菜单下拉框数据获取
    public ResponseEntity<List<JobDTO>> getJobByName(@RequestParam String name) {
        return  ResponseEntity.ok(jobService.getJobByName(name));
    }

    @RequestMapping(value = "/checkJobType",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //没用到
    public ResponseEntity<String> checkJobType(@RequestParam String name) {
        return  ResponseEntity.ok(jobService.checkJobType(name));
    }
}
