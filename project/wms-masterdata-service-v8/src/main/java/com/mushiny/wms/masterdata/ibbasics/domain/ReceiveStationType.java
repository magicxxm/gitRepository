package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_RECEIVESTATIONTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class ReceiveStationType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;



    @Column(name = "RECEIVE_STATION_TYPE")
    private String receiveStationType;
    public String getReceiveStationType() {
        return receiveStationType;
    }

    public void setReceiveStationType(String receiveStationType) {
        this.receiveStationType = receiveStationType;
    }
    @OrderBy("positionIndex")
    @OneToMany(mappedBy = "receivingStationType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReceiveStationTypePosition> positions = new ArrayList<>();

    public void addPosition(ReceiveStationTypePosition position) {
        getPositions().add(position);
        position.setReceivingStationType(this);
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

    public List<ReceiveStationTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveStationTypePosition> positions) {
        this.positions = positions;
    }
}
