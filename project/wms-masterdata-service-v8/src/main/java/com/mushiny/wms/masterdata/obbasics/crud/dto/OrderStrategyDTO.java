package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OrderStrategy;

import javax.validation.constraints.NotNull;

public class OrderStrategyDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private boolean createShippingOrder;

    @NotNull
    private boolean createFollowupPicks;

    @NotNull
    private boolean manualCreationIndex;

    @NotNull
    private boolean preferMatchingStock;

    @NotNull
    private boolean preferUnopened;

    @NotNull
    private boolean useLockedLot;

    @NotNull
    private boolean useLockedStock;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String defaultDestinationId;

    private StorageLocationDTO storageLocation;

    public OrderStrategyDTO() {
    }

    public OrderStrategyDTO(OrderStrategy entity) {
        super(entity);
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

    public String getDefaultDestinationId() {
        return defaultDestinationId;
    }

    public void setDefaultDestinationId(String defaultDestinationId) {
        this.defaultDestinationId = defaultDestinationId;
    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
    }
}
