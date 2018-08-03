package com.mushiny.wms.tot.job.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.job.domain.Job;
import com.mushiny.wms.tot.jobcategory.crud.dto.JobcategoryDTO;

public class JobDTO extends BaseDTO {
    private String jobType;
    private String code;
    private String name;
    private String description;
    private String keyword;
    private String indirectType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String categoryId;
    private String warehouseId;
    private JobcategoryDTO categoryDTO;

    public JobDTO() {
    }
    public JobDTO(Job entity) {
        super(entity);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIndirectType() {
        return indirectType;
    }

    public void setIndirectType(String indirectType) {
        this.indirectType = indirectType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public JobcategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(JobcategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }
}
