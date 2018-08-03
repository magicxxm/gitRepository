package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_STOWSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class StowStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private StowStationType type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID", nullable = false)
    private WorkStation workstation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "stowStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<StowStationPosition> stowStation = new ArrayList<>();

    public void addPosition(StowStationPosition stowStation) {
        getStowStation().add(stowStation);
        stowStation.setStowStation(this);
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

    public StowStationType getType() {
        return type;
    }

    public void setType(StowStationType type) {
        this.type = type;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public WorkStation getWorkstation() {
        return workstation;
    }

    public void setWorkstation(WorkStation workstation) {
        this.workstation = workstation;
    }

    public List<StowStationPosition> getStowStation() {
        return stowStation;
    }

    public void setStowStation(List<StowStationPosition> stowStation) {
        this.stowStation = stowStation;
    }
}
