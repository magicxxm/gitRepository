package com.mushiny.wms.outboundproblem.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OBProblemDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ItemNo;

    private String ItemSku;

    private BigDecimal Amount = BigDecimal.ZERO;

    private BigDecimal RebinAmount = BigDecimal.ZERO;

    private String StorageLocation;

    private String Wall;

    private String Cell;

    public String getItemSku() {
        return ItemSku;
    }

    public void setItemSku(String itemSku) {
        ItemSku = itemSku;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public BigDecimal getRebinAmount() {
        return RebinAmount;
    }

    public void setRebinAmount(BigDecimal rebinAmount) {
        RebinAmount = rebinAmount;
    }

    public String getStorageLocation() {
        return StorageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        StorageLocation = storageLocation;
    }

    public OBProblemDataDTO() {

    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    @Override
    public String toString() {
        return "OBProblemDataDTO{" +
                "itemNo='" + ItemNo + '\'' +
                ", itemSku='" + ItemSku + '\'' +
                ", storageLocation=" + StorageLocation + '\'' +
                ", amount=" + Amount + '\'' +
                ", rebinAmount=" + RebinAmount +'\'' +
                '}';
        }

    public String getWall() {
        return Wall;
    }

    public void setWall(String wall) {
        Wall = wall;
    }

    public String getCell() {
        return Cell;
    }

    public void setCell(String cell) {
        Cell = cell;
    }
}