package com.mushiny.wms.tot.ppr.domain;


import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "TOT_JOBCATEGORYRELATION")
public class JobCategoryRelation extends BaseEntity {

    @Column(name = "MAINPROCESSES", nullable = false)
    private String mainProcesses;
    @Column(name = "COREPROCESSES", nullable = false)
    private String coreProcesses;
    @Column(name = "PROJECT", nullable = false)
    private String project;
    @Column(name = "LINEITEMS", nullable = false)
    private String lineItems;
    @Column(name = "PLANRATE")
    private Double planRate;
    @Column(name = "JOB_TYPE")
    private String jobType;
    @Column(name = "REORDER", nullable = false)
    private String reorder;

    public String getMainProcesses() {
        return mainProcesses;
    }

    public void setMainProcesses(String mainProcesses) {
        this.mainProcesses = mainProcesses;
    }
    public String getCoreProcesses() {
        return coreProcesses;
    }

    public void setCoreProcesses(String coreProcesses) {
        this.coreProcesses = coreProcesses;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLineItems() {
        return lineItems;
    }

    public void setLineItems(String lineItems) {
        this.lineItems = lineItems;
    }

    public Double getPlanRate() {
        return planRate;
    }

    public void setPlanRate(Double planRate) {
        this.planRate = planRate;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getReorder() {
        return reorder;
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }
}
