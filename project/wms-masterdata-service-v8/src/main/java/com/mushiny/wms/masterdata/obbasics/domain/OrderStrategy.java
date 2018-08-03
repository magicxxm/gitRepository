package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;

import javax.persistence.*;

@Entity
@Table(name = "OB_ORDERSTRATEGY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class OrderStrategy extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CREATE_SHIPPING_ORDER", nullable = false)
    private boolean createShippingOrder;

    @Column(name = "CREATE_FOLLOWUP_PICKS", nullable = false)
    private boolean createFollowupPicks;

    @Column(name = "MANUAL_CREATION_INDEX", nullable = false)
    private boolean manualCreationIndex;

    @Column(name = "PREFER_MATCHING_STOCK", nullable = false)
    private boolean preferMatchingStock;

    @Column(name = "PREFER_UNOPENED", nullable = false)
    private boolean preferUnopened;

    @Column(name = "USE_LOCKED_LOT", nullable = false)
    private boolean useLockedLot;

    @Column(name = "USE_LOCKED_STOCK", nullable = false)
    private boolean useLockedStock;

    @ManyToOne
    @JoinColumn(name = "DEFAULTDESTINATION_ID")
    private StorageLocation storageLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCreateShippingOrder() {
        return createShippingOrder;
    }

    public void setCreateShippingOrder(boolean createShippingOrder) {
        this.createShippingOrder = createShippingOrder;
    }

    public boolean isCreateFollowupPicks() {
        return createFollowupPicks;
    }

    public void setCreateFollowupPicks(boolean createFollowupPicks) {
        this.createFollowupPicks = createFollowupPicks;
    }

    public boolean isManualCreationIndex() {
        return manualCreationIndex;
    }

    public void setManualCreationIndex(boolean manualCreationIndex) {
        this.manualCreationIndex = manualCreationIndex;
    }

    public boolean isPreferMatchingStock() {
        return preferMatchingStock;
    }

    public void setPreferMatchingStock(boolean preferMatchingStock) {
        this.preferMatchingStock = preferMatchingStock;
    }

    public boolean isPreferUnopened() {
        return preferUnopened;
    }

    public void setPreferUnopened(boolean preferUnopened) {
        this.preferUnopened = preferUnopened;
    }

    public boolean isUseLockedLot() {
        return useLockedLot;
    }

    public void setUseLockedLot(boolean useLockedLot) {
        this.useLockedLot = useLockedLot;
    }

    public boolean isUseLockedStock() {
        return useLockedStock;
    }

    public void setUseLockedStock(boolean useLockedStock) {
        this.useLockedStock = useLockedStock;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }
}
