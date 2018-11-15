package wms.business.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by 123 on 2017/8/18.
 */
public class ShipmentConfirmItemsDTO implements Serializable {

    //"lineNo": "商品行号，string (20)，必填",
    private String lineNo;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //""计划数量，int，必填",
    private int planQty;

    //实发数量
    private int quantity;

    //"inventorySts": "库存状态（ZP：良品、正品、CP：不良品、次品），string (2)，必填默认 ZP",
    private String inventorySts = "ZP";

    //商品单位,默认  EA
    private String unit = "EA";

    //生成日期
    private String manufactureDate;
//    private Date manufactureDate;

    //失效日期
//    private Date expirationDate;
    private String expirationDate;

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

    public int getPlanQty() {
        return planQty;
    }

    public void setPlanQty(int planQty) {
        this.planQty = planQty;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    //    public Date getManufactureDate() {
//        return manufactureDate;
//    }
//
//    public void setManufactureDate(Date manufactureDate) {
//        this.manufactureDate = manufactureDate;
//    }
//
//    public Date getExpirationDate() {
//        return expirationDate;
//    }
//
//    public void setExpirationDate(Date expirationDate) {
//        this.expirationDate = expirationDate;
//    }
}
