package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_RECEIVESTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class ReceiveStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OPERATOR_ID")
    private String operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private ReceiveStationType receivingStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID", nullable = false)
    private WorkStation workStation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "receiveStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReceiveStationPosition> positions = new ArrayList<>();

    public void addPosition(ReceiveStationPosition position) {
        getPositions().add(position);
        position.setReceiveStation(this);
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

    public ReceiveStationType getReceivingStationType() {
        return receivingStationType;
    }

    public void setReceivingStationType(ReceiveStationType receivingStationType) {
        this.receivingStationType = receivingStationType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public List<ReceiveStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveStationPosition> positions) {
        this.positions = positions;
    }
}
