package wms.business.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2017/12/7.
 */
public class CydhDTO implements Serializable {

    //仓库编码
    private String warehouseCode;

    //货主编码
    private String companyCode;

    //承运服务商代码
    private String carrierCode;

    //承运服务商名称
    private String carrierName;

    //订单号
    private String shipmentCode;

    //承运单号
    private String primaryWaybillCode;

    public CydhDTO() {
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

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
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
}
