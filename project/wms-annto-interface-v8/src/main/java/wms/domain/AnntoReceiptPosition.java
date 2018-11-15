package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_RECEIPTPOSITION")
public class AnntoReceiptPosition extends BaseEntity{

    @ManyToOne
    @JoinColumn(name="RECEIPT_ID",nullable = false)
    private AnntoReceipt anntoReceipt;

    @Column(name = "LINE_NO")
    private String lineNo;

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "TOTAL_QTY")
    private int totalQty;

    @Column(name = "OPEN_QTY")
    private int openQty;

    @Column(name = "INVENTORY_STS")
    private String inventorySts;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "MANUFACTURE_DATE")
    private Date manufactureDate;

    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Column(name = "KITFLAG")
    private String kitflag;

    @Column(name = "REMARK")
    private String remark;

    public AnntoReceipt getAnntoReceipt() {
        return anntoReceipt;
    }

    public void setAnntoReceipt(AnntoReceipt anntoReceipt) {
        this.anntoReceipt = anntoReceipt;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getOpenQty() {
        return openQty;
    }

    public void setOpenQty(int openQty) {
        this.openQty = openQty;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getKitflag() {
        return kitflag;
    }

    public void setKitflag(String kitflag) {
        this.kitflag = kitflag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
