package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.StockUnit;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class StockUnitDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private BigDecimal amount;

    private BigDecimal reservedAmount;

    @NotNull
    private LocalDate strategyDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemDataId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lotId;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String storageLocationId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String containerId;

    @NotNull
    private ItemDataDTO itemData;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private LotDTO lot;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StorageLocationDTO storageLocation;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private ContainerDTO container;

    public StockUnitDTO() {
    }

    public StockUnitDTO(StockUnit entity) {
        super(entity);
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }

    public LocalDate getStrategyDate() {
        return strategyDate;
    }

    public void setStrategyDate(LocalDate strategyDate) {
        this.strategyDate = strategyDate;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

//    public String getStorageLocationId() {
//        return storageLocationId;
//    }
//
//    public void setStorageLocationId(String storageLocationId) {
//        this.storageLocationId = storageLocationId;
//    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

//    public LotDTO getLot() {
//        return lot;
//    }
//
//    public void setLot(LotDTO lot) {
//        this.lot = lot;
//    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
    }

//    public ContainerDTO getContainer() {
//        return container;
//    }
//
//    public void setContainer(ContainerDTO container) {
//        this.container = container;
//    }
}
