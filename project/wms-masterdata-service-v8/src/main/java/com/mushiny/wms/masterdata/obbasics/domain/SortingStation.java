package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_SORTINGSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"})
})
public class SortingStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID")
    private SortingStationType sortingStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    @OrderBy("workStationPosition")
    @OneToMany(mappedBy = "sortingStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<SortingStationPosition> positions = new ArrayList<>();

    public void addPosition(SortingStationPosition position) {
        getPositions().add(position);
        position.setSortingStation(this);
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

    public List<SortingStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<SortingStationPosition> positions) {
        this.positions = positions;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

    public SortingStationType getSortingStationType() {
        return sortingStationType;
    }

    public void setSortingStationType(SortingStationType sortingStationType) {
        this.sortingStationType = sortingStationType;
    }
}
