package wms.crud.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 订单取消
 *
 */
public class ShipmentCancelDTO {

    //"orderCode": "订单号，string (50)，必填",
    private String code;

    private String warehouseCode;

    //"customerCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"orderType": "订单类型（PO：采购入库、ASO：调拨入库、RO：退货入库、SO：销售出库单、RVO：调拨出库单、RDO：退货返厂单、TO：运输单、HO：家居订单、SVO：服务订单），string (10)，必填",
    private String orderType;

    //"cancelReason": " 取消原因， string (200)"
    private String cancelReason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
