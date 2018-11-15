package wms.crud.common.dto;

import wms.common.crud.dto.BaseDTO;

/**
 * Created by PC-4 on 2017/8/4.
 */
public class OrderStrategyDTO extends BaseDTO {

    private String name;

    private boolean createShippingOrder;

    private boolean createFollowupPicks;

    private boolean manualCreationIndex;

    private boolean preferMatchingStock;

    private boolean preferUnopened;

    private boolean useLockedLot;

    private boolean useLockedStock;

//    private StorageLocationDTO storageLocation;

    public OrderStrategyDTO() {
    }
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
//    public StorageLocationDTO getStorageLocation() {
//        return storageLocation;
//    }
//    public void setStorageLocation(StorageLocationDTO storageLocation) {
//        this.storageLocation = storageLocation;
//    }
}