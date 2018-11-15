package wms.crud.dto;

/**
 * Created by 123 on 2017/9/3.
 */
public class PackDTO {

    private String warehouseCode;

    private String companyCode;

    private String stationCode;

    private String pickPackCellCode;

    private String user;

    public PackDTO() {
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
}
