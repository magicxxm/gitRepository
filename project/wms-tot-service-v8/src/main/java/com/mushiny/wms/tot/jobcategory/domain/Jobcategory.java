package com.mushiny.wms.tot.jobcategory.domain;


import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TOT_JOBCATEGORY")
public class Jobcategory extends BaseEntity {
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "JOB_TYPE", nullable = false)
    private String jobType;
    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;
    @Column(name = "SUBPRODATASOURCE")
    private String subproDataSource;
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
