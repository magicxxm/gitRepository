package com.mushiny.wms.tot.job.domain;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.tot.jobcategory.domain.Jobcategory;

import javax.persistence.*;

@Entity
@Table(name = "TOT_JOB")
public class Job extends BaseEntity {
    @Column(name = "JOB_TYPE", nullable = false)
    private String jobType;
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "KEYWORD")
    private String keyword;
    @Column(name = "INDIRECT_TYPE")
    private String indirectType;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Jobcategory category;
    @Column(name = "WAREHOUSE_ID", nullable = false)
    private String warehouseId;



    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
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


    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Jobcategory getCategory() {
        return category;
    }

    public void setCategory(Jobcategory category) {
        this.category = category;
    }
}
