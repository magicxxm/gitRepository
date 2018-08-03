package com.mushiny.wms.tot.jobthreshold.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.tot.jobthreshold.domain.Jobthreshold;

public class JobthresholdDTO extends BaseWarehouseAssignedDTO {

    private int thresholdA;
    private int thresholdB;
    public JobthresholdDTO() {
    }
    public JobthresholdDTO(Jobthreshold entity) {
        super(entity);
    }

    public int getThresholdA() {
        return thresholdA;
    }

    public void setThresholdA(int thresholdA) {
        this.thresholdA = thresholdA;
    }

    public int getThresholdB() {
        return thresholdB;
    }

    public void setThresholdB(int thresholdB) {
        this.thresholdB = thresholdB;
    }
}
