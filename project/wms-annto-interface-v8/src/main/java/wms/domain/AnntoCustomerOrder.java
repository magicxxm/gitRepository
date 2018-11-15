package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_CUSTOMERORDER")
public class AnntoCustomerOrder extends BaseEntity{

    @Column(name = "CODE")
    private String code;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "WAREHOUSE_CODE")
    private String warehouseCode;

    @Column(name = "SHIPMENT_TYPE")
    private String shipmentType;

    @Column(name = "REQUESTED_DELIVERY_DATE")
    private String requesteddeliverydate;

    @Column(name = "CARRIER_CODE")
    private String carrierCode;

    @Column(name = "CARRIER_SERVICE")
    private String carrierService;

    @Column(name = "PRIMARY_WAYBILL_CODE")
    private String primaryWaybillCode;

    @Column(name = "BUSINESS_TYPE")
    private String businessType;

    @Column(name = "REMARK")
    private String remark;

    @OrderBy("lineNo")
    @OneToMany(mappedBy = "anntoCustomerOrder", cascade = {CascadeType.ALL}, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<AnntoCustomerOrderItems> orderItems = new ArrayList<>();

    public void addOrderItem(AnntoCustomerOrderItems orderItems){
        getOrderItems().add(orderItems);
        orderItems.setAnntoCustomerOrder(this);
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

    public String getRequesteddeliverydate() {
        return requesteddeliverydate;
    }

    public void setRequesteddeliverydate(String requesteddeliverydate) {
        this.requesteddeliverydate = requesteddeliverydate;
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

    public List<AnntoCustomerOrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<AnntoCustomerOrderItems> orderItems) {
        this.orderItems = orderItems;
    }
}
