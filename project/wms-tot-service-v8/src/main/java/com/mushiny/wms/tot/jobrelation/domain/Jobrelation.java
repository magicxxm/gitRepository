package com.mushiny.wms.tot.jobrelation.domain;


import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TOT_JOBRELATION")
public class Jobrelation extends BaseEntity {
    @Column(name = "OPERATION", nullable = false)
    private String operation;
    @Column(name = "TOOL", nullable = false)
    private String tool;
    @Column(name = "JABCATEGORY_NAME", nullable = false)
    private String jobcategoryName;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getJobcategoryName() {
        return jobcategoryName;
    }

    public void setJobcategoryName(String jobcategoryName) {
        this.jobcategoryName = jobcategoryName;
    }
}
