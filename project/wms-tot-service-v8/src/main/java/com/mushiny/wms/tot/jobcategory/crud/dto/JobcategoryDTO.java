package com.mushiny.wms.tot.jobcategory.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;

public class JobcategoryDTO extends BaseDTO {
    private String code;
    private String name;
    private String description;
    private String jobType;
    private String warehouseId;
    private String subproDataSource;
    public JobcategoryDTO() {
    }
    public JobcategoryDTO(Jobcategory entity) {
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

    public String getSubproDataSource() {
        return subproDataSource;
    }

    public void setSubproDataSource(String subproDataSource) {
        this.subproDataSource = subproDataSource;
    }
}
