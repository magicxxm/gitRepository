package wms.business.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by 123 on 2017/8/11.
 */
public class TransferOrderItemsDTO implements Serializable {

    //货主编码  必填
    private String companyCode;

    //商品编码
    private String itemCode;

    //商品名称
    private String itemName;

    //计划数量
    private int allocatedQty;

    //库存状态
    private int invantorySts;

    //批次
    private String batch;

    //批号
    private String lot;

    //生产日期
    private LocalDateTime manufactureDate;

    //失效日期
    private LocalDateTime expirationDate;

    //到库位
    private String toloc;

    //到区域
    private String toZone;

    //到托盘
    private String toLpn;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public int getAllocatedQty() {
        return allocatedQty;
    }

    public void setAllocatedQty(int allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public int getInvantorySts() {
        return invantorySts;
    }

    public void setInvantorySts(int invantorySts) {
        this.invantorySts = invantorySts;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public LocalDateTime getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getToZone() {
        return toZone;
    }

    public void setToZone(String toZone) {
        this.toZone = toZone;
    }

    public String getToLpn() {
        return toLpn;
    }

    public void setToLpn(String toLpn) {
        this.toLpn = toLpn;
    }
}
