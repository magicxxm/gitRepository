package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.obbasics.domain.DigitalLabel;

import javax.persistence.*;

@Entity
@Table(name = "MD_WORKSTATIONPOSITION")
public class WorkStationPosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private String positionNo;

    @Column(name = "POSITION_INDEX")
    private int positionIndex;

    @ManyToOne
    @JoinColumn(name = "DIGITALLABEL_ID")
    private DigitalLabel digitalLabel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID", nullable = false)
    private WorkStation workStation;

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public DigitalLabel getDigitalLabel() {
        return digitalLabel;
    }

    public void setDigitalLabel(DigitalLabel digitalLabel) {
        this.digitalLabel = digitalLabel;
    }
}
