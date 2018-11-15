package wms.business.dto;

/**
 * Created by 123 on 2017/9/3.
 */
public class PackInfo {

    //仓库编码
    private String warehouseCode;

    //货主编码
    private String companyCode;

    //包装台编码
    private String stationCode;

    //cell格编码
    private String pickPackCellCode;

    //订单号
    private String code;

    private String shipmentCode;

    private String primaryWaybillCode;

    public PackInfo() {
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

    public String getPickPackCellCode() {
        return pickPackCellCode;
    }

    public void setPickPackCellCode(String pickPackCellCode) {
        this.pickPackCellCode = pickPackCellCode;
    }

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

    @Override
    public String toString() {
        return "packInfo = {warehouseCode:"+getWarehouseCode()+",code:"+getCode()+",stationCode:"+getStationCode()+"}";
    }
}
