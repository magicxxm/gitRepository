package wms.crud.dto;

/**
 * Created by 123 on 2017/9/5.
 */
public class PackConfirmDTO {

    private String warehouseCode;

    private String companyCode;

    private String stationCode;

    private String shipmentCode;//出库订单号

    private String primaryWaybillCode;//箱型

    private String packingTime;//复核时间

    private String user;

    public PackConfirmDTO() {
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

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    public String getPackingTime() {
        return packingTime;
    }

    public void setPackingTime(String packingTime) {
        this.packingTime = packingTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
