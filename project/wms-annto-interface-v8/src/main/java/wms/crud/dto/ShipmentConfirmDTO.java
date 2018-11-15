package wms.crud.dto;

import wms.business.dto.ShipmentConfirmItemsDTO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 出库单确认
 *
 */
public class ShipmentConfirmDTO {

    //"code": "出库单号，string (50)，必填",
    private String code;

    //拆分后的订单号
    private String shipmentCode;

    //承运单号
    private String primaryWaybillCode;

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"warehouseCode": "出库仓库编码，string (50)，必填",
    private String warehouseCode;

    //"shipmentType": "出库单类型（SO：销售出库单、RVO：调拨出库单、RDO：退货返厂单、TO：运输单、HO：家居订单、SVO：服务订单），string (10)，必填",
    private String shipmentType;

    //箱型编码
    private String containerCode;

    //"operateTime": "出库完结时间, yyyy-MM-dd HH:mm:ss，string (20)，必填",
    private String operateTime;

    //"operatorCode": "操作员编码，string (20)",
    private String operatorCode;

    //"operatorName": "操作员姓名，string (50)",
    private String operatorName;

    private List<ShipmentConfirmItemsDTO> orderItems = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getPrimaryWaybillCode() {
        return primaryWaybillCode;
    }

    public void setPrimaryWaybillCode(String primaryWaybillCode) {
        this.primaryWaybillCode = primaryWaybillCode;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
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

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
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

    public List<ShipmentConfirmItemsDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ShipmentConfirmItemsDTO> orderItems) {
        this.orderItems = orderItems;
    }
}


