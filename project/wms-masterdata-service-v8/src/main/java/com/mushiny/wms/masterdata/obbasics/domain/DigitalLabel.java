package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.RobotType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OB_DIGITALLABEL")
public class DigitalLabel extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "NUM_ORDER")
    private int numOrder=0;
    @ManyToOne(optional = false)
    @JoinColumn(name = "LABELCONTROLLER_ID", nullable = false)
    private LabelController labelController;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
    }

    public LabelController getLabelController() {
        return labelController;
    }

    public void setLabelController(LabelController labelController) {
        this.labelController = labelController;
    }
}
