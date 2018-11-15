package wms.crud.dto;

import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/28.
 */
public class ShipmentOrderItem {

    //"lineNo": "商品行号，string (20)，必填",
    private String lineNo;

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"itemName": "商品名称，string (200)，必填",
    private String itemName;

    //箱型编码
    private String containerCode;

    //""计划数量，int，必填",
    private int planQty;

    //"inventorySts": "库存状态（ZP：良品、正品、CP：不良品、次品），string (2)，必填默认 ZP",
    private String inventorySts;

    //商品单位,默认  EA
    private String unit = "EA";

    //是否套装  Y：是   N：不是  必填
    private String kitFlag;

    //备注
    private String remark;

    private ExtendFieldsDTO extendFields;


    public ShipmentOrderItem() {
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

    public int getPlanQty() {
        return planQty;
    }

    public void setPlanQty(int planQty) {
        this.planQty = planQty;
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

    public String getKitFlag() {
        return kitFlag;
    }

    public void setKitFlag(String kitFlag) {
        this.kitFlag = kitFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
