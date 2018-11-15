package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGORDER")
public class PickingOrder extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "PICKINGORDER_NO", nullable = false)
    private String pickingOrderNo;

    @Column(name = "CUSTOMERSHIPMENT_NO")
    private String customerShipmentNo;

    @Column(name = "MANUAL_CREATION", nullable = false)
    private boolean manualCreation;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

    @Column(name = "STATE", nullable = false)
    private int state;

    @ManyToOne
    @JoinColumn(name = "DESTINATION_ID")
    private StorageLocation storageLocation;

    @Column(name = "OPERATOR_ID")
    private String operatorId;

    @OneToMany(mappedBy = "pickingOrder")
    private List<PickingOrderPosition> positions = new ArrayList<>();

    public String getPickingOrderNo() {
        return pickingOrderNo;
    }

    public void setPickingOrderNo(String pickingOrderNo) {
        this.pickingOrderNo = pickingOrderNo;
    }

    public String getCustomerShipmentNo() {
        return customerShipmentNo;
    }

    public void setCustomerShipmentNo(String customerShipmentNo) {
        this.customerShipmentNo = customerShipmentNo;
    }

    public boolean isManualCreation() {
        return manualCreation;
    }

    public void setManualCreation(boolean manualCreation) {
        this.manualCreation = manualCreation;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public List<PickingOrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingOrderPosition> positions) {
        this.positions = positions;
    }
}
