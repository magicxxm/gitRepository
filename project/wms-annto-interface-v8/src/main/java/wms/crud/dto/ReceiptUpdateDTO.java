package wms.crud.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 入库单同步
 *
 */
public class ReceiptUpdateDTO {

    //"warehouseCode": "入库仓库编码，string (50)，必填 统仓统配不需指定仓库编码填NONE",
    private String warehouseCode;

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"Code": "入库单号，string (50)，必填",
    private String code;

    //ANNTO WMS 单号  一个anntoCode 对应一个或多个code入库单号
    private String anntoCode;

    //"receiptType": "入库单类型（PI：采购入库、TI：移库入库、RI：补货入库），string (10)，必填",
    private String receiptType;

    //"PLN": "托盘号，string (50), 必填",
    private String pln;

    //"receiptNote": "入库单备注，string (500)",
    private String receiptNote;

    private List<ReceiptItemsDTO> receiptItems = new ArrayList<>();

    private ExtendFieldsDTO extendFields;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnntoCode() {
        return anntoCode;
    }

    public void setAnntoCode(String anntoCode) {
        this.anntoCode = anntoCode;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getPln() {
        return pln;
    }

    public void setPln(String pln) {
        this.pln = pln;
    }

    public String getReceiptNote() {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote) {
        this.receiptNote = receiptNote;
    }

    public List<ReceiptItemsDTO> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<ReceiptItemsDTO> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
