package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OBP_OBPSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class OBPStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATE")
    private String state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operatorId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private OBPStationType obpStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "obpStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OBPStationPosition> positions = new ArrayList<>();

    public void addPosition(OBPStationPosition position) {
        getPositions().add(position);
        position.setObpStation(this);
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

    public OBPStationType getObpStationType() {
        return obpStationType;
    }

    public void setObpStationType(OBPStationType obpStationType) {
        this.obpStationType = obpStationType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public List<OBPStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<OBPStationPosition> positions) {
        this.positions = positions;
    }
}
