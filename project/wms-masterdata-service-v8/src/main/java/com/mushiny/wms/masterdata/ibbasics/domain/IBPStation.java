package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IBP_IBPSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class IBPStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private IBPStationType ibpStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "ibpStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<IBPStationPosition> positions = new ArrayList<>();

    public void addPosition(IBPStationPosition position) {
        getPositions().add(position);
        position.setIbpStation(this);
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


    public User getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(User operatorId) {
        this.operatorId = operatorId;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public List<IBPStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<IBPStationPosition> positions) {
        this.positions = positions;
    }

    public IBPStationType getIbpStationType() {
        return ibpStationType;
    }

    public void setIbpStationType(IBPStationType ibpStationType) {
        this.ibpStationType = ibpStationType;
    }
}
