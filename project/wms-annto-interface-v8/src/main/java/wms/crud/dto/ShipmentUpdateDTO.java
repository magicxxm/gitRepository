package wms.crud.dto;

import wms.common.crud.dto.BaseDTO;
import wms.domain.AnntoCustomerOrder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 出库单同步
 *
 */
public class ShipmentUpdateDTO extends BaseDTO{

    //"code": "出库单号，string (50)，必填",
    private String code;

    //"companyCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"warehouseCode": "出库仓库编码，string (50)，必填",
    private String warehouseCode;

    //"shipmentType": "出库单类型（SO：销售出库单、RVO：调拨出库单、RDO：退货返厂单、TO：运输单、HO：家居订单、SVO：服务订单），string (10)，必填",
    private String shipmentType;

    //"requestedDeliveryDate": "要求出库时间, yyyy-MM-dd HH:mm:ss，string (20)",
    private String requestedDeliveryDate;

    //"carrierCode": "承运商代码，string (50)",  sortCode
    private String carrierCode;

    //"carrierService": "承运服务类型，string (50)",
    private String carrierService;

    //"primaryWaybillCode": "承运单号，string (50)",
    private String primaryWaybillCode;

    //业务类型(B2B：B2B业务、B2C：B2C业务)，string (10)，必填 默认B2C
    private String businessType;

    private String remark;//备注

    //商品信息
    private List<ShipmentOrderItem> orderItems = new ArrayList<>();

    private ExtendFieldsDTO extendFields;

    public ShipmentUpdateDTO(AnntoCustomerOrder entity) {
        super(entity);
    }

    public ShipmentUpdateDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getRequestedDeliveryDate() {
        return requestedDeliveryDate;
    }

    public void setRequestedDeliveryDate(String requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierService() {
        return carrierService;
    }

    public void setCarrierService(String carrierService) {
        this.carrierService = carrierService;
    }

    public String getPrimaryWaybillCode() {
        return primaryWaybillCode;
    }

    public void setPrimaryWaybillCode(String primaryWaybillCode) {
        this.primaryWaybillCode = primaryWaybillCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ShipmentOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ShipmentOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}


