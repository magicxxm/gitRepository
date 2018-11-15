package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "MD_ITEMDATA_SERIALNUMBER")
public class ItemDataSerialNumber extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SERIAL_NO", nullable = false)
    private String serialNo;

    @Column(name = "ITEMDATA_ID")
    private String itemDataId;

    @Column(name = "PRODUCT_DATE")
    private Date productDate;

    @Column(name = "STOCK_STATE")
    private String stockState;

    @Column(name = "GOODSRECEIPT_ID")
    private String goodsReceiptId;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public void setGoodsReceiptId(String goodsReceiptId) {
        this.goodsReceiptId = goodsReceiptId;
    }
}
