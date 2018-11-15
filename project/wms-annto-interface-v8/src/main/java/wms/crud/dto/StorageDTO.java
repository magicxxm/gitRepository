package wms.crud.dto;

/**
 * Created by 123 on 2017/9/3.
 */
public class StorageDTO {

    private String warehouseCode;

    private String companyCode;

    private String stationCode;

    private String pickPackCellCode;

    private String user;

    private String shipmentCode;

    private String problemType;

    private String storageLocation;

    private String primaryWaybillCode;

    private String digitalId;

    private String code;

    private int lossAmount;

    private String pickPackWallId;


    public StorageDTO() {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getPrimaryWaybillCode() {
        return primaryWaybillCode;
    }

    public void setPrimaryWaybillCode(String primaryWaybillCode) {
        this.primaryWaybillCode = primaryWaybillCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(int lossAmount) {
        this.lossAmount = lossAmount;
    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }
}
