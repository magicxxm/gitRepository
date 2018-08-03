package com.mushiny.wms.report.query.dto.pp_work;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PpNames implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ppName;

    private List<WorkPpNameTime> workPpNameTime = new ArrayList<>();

    public PpNames() {
    }

    public PpNames(String ppName) {
        this.ppName = ppName;
    }

    public PpNames(String ppName, List<WorkPpNameTime> workPpNameTime) {
        this.ppName = ppName;
        this.workPpNameTime = workPpNameTime;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public List<WorkPpNameTime> getWorkPpNameTime() {
        return workPpNameTime;
    }

    public void setWorkPpNameTime(List<WorkPpNameTime> workPpNameTime) {
        this.workPpNameTime = workPpNameTime;
    }

    @Override
    public String toString() {
        return "PpNames{" +
                "ppName='" + ppName + '\'' +
                ", ppNameTime=" + workPpNameTime +
                '}';
    }
}
