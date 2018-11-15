package wms.domain.common;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name="OB_PACKINGSTATION")
public class PackingStation extends BaseWarehouseAssignedEntity {

    @Column(name="NAME",nullable = false)
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

//    @ManyToOne
//    @JoinColumn(name="TYPE_ID")
//    private PackingStationType packingStationType;

    @ManyToOne
    @JoinColumn(name="OPERATOR_ID")
    private User operator;

    @ManyToOne
    @JoinColumn(name="WORKSTATION_ID")
    private WorkStation workStation;

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

//    public PackingStationType getPackingStationType() {
//        return packingStationType;
//    }
//
//    public void setPackingStationType(PackingStationType packingStationType) {
//        this.packingStationType = packingStationType;
//    }

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
}
