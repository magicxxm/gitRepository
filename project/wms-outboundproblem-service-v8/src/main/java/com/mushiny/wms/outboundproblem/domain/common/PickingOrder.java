package com.mushiny.wms.outboundproblem.domain.common;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="OB_PICKINGORDER")
public class PickingOrder extends BaseClientAssignedEntity {

    @ManyToOne
    @JoinColumn(name="PROCESSPATH_ID")
    private ProcessPath processPath;

    public ProcessPath getProcessPath() {
        return processPath;
    }

    public void setProcessPath(ProcessPath processPath) {
        this.processPath = processPath;
    }
}
