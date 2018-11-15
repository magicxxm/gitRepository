package wms.business.dto;

import wms.domain.common.Lot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/11.
 */
public class InventoryDTO implements Serializable {

    private BigDecimal amountTotel;

    private BigDecimal amountReserved;

    private String itemNo;

    private String state;

    private String lotId;

    public InventoryDTO(BigDecimal amountTotel, BigDecimal amountReserved, String itemNo, String state,String lotId) {
        this.amountTotel = amountTotel;
        this.amountReserved = amountReserved;
        this.itemNo = itemNo;
        this.state = state;
        this.lotId = lotId;
    }

    public BigDecimal getAmountTotel() {
        return amountTotel;
    }

    public void setAmountTotel(BigDecimal amountTotel) {
        this.amountTotel = amountTotel;
    }

    public BigDecimal getAmountReserved() {
        return amountReserved;
    }

    public void setAmountReserved(BigDecimal amountReserved) {
        this.amountReserved = amountReserved;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }
}
