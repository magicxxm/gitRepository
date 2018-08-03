package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
//import com.mushiny.wms.masterdata.domain.ReceiveDestination;
//import com.mushiny.wms.masterdata.domain.ReceiveStation;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;

import javax.persistence.*;

@Entity
@Table(name = "IB_RECEIVEPROCESS")
public class ReceiveProcess extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_INDEX", nullable = false)
    private int positionIndex;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVESTORAGE_ID")
    private StorageLocation storageLocation;

    @Column(name = "RECEIVEUNITLOAD_ID")
    private String receiveUnitLoad;

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getReceiveUnitLoad() {
        return receiveUnitLoad;
    }

    public void setReceiveUnitLoad(String receiveUnitLoad) {
        this.receiveUnitLoad = receiveUnitLoad;
    }

}
