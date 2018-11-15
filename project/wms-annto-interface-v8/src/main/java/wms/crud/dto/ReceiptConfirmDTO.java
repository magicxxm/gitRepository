package wms.crud.dto;

import wms.business.dto.ReceiptConfirmItemsDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 入库单确认
 *
 */
public class ReceiptConfirmDTO {

    //"Code": "入库单号，string (50)，必填",
    private String Code;

    //ANNTO WMS 单号  一个anntoCode 对应一个或多个code入库单号
    private String anntoCode;

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"warehouseCode": "入库仓库编码，string (50)，必填",
    private String warehouseCode;

    //"receiptType": "入库单类型（PO：采购入库、ASO：调拨入库、RO：退货入库单），string (50)，必填",
    private String receiptType;

    //"operateTime": "操作时间(当confirmType=0, operateTime为入库完结时间)",
    private LocalDateTime operateTime;

    //"operatorCode": "操作员编码，string (20)",
    private String operatorCode;

    //"operatorName": "操作员姓名，string (50)",
    private String operatorName;

    private List<ReceiptConfirmItemsDTO> orderItems = new ArrayList<>();

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getAnntoCode() {
        return anntoCode;
    }

    public void setAnntoCode(String anntoCode) {
        this.anntoCode = anntoCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<ReceiptConfirmItemsDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ReceiptConfirmItemsDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
