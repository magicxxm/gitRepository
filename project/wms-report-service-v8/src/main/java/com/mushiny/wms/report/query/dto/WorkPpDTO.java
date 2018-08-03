package com.mushiny.wms.report.query.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkPpDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ppName;

    private List<WorkFlowDTO> workflows = new ArrayList<>();

    public WorkPpDTO() {
    }

    public WorkPpDTO(String ppName) {
        this.ppName = ppName;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public List<WorkFlowDTO> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<WorkFlowDTO> workflows) {
        this.workflows = workflows;
    }

    public WorkPpDTO(String ppName, List<WorkFlowDTO> workflows) {
        this.ppName = ppName;
        this.workflows = workflows;
    }
}
