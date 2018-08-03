package com.mushiny.wms.tot.jobrelation.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.jobrelation.domain.Jobrelation;

public class JobrelationDTO extends BaseDTO {

    private String operation;
    private String tool;
    private String jobcategoryName;
    public JobrelationDTO() {
    }
    public JobrelationDTO(Jobrelation entity) {
        super(entity);
    }

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

    public void setJobcategory_name(String jobcategoryName) {
        this.jobcategoryName = jobcategoryName;
    }
}
