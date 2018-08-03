package com.mushiny.wcs.application.domain;


import javax.persistence.*;

@Entity
@Table(name = "OBP_OBPSTATION", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class OBPStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private OBPStationType obpStationType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WORKSTATION_ID")
    private WorkStation workStation;

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
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

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }
}
