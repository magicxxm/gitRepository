package wms.crud.dto;

/**
 * Created by 123 on 2017/9/1.
 */
public class StockUnitCheckDTO {

    private String warehouseCode;

    private String companyCode;

    private String itemCodeArray;

    private String inventoryStsArray;

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

    public String getItemCodeArray() {
        return itemCodeArray;
    }

    public void setItemCodeArray(String itemCodeArray) {
        this.itemCodeArray = itemCodeArray;
    }

    public String getInventoryStsArray() {
        return inventoryStsArray;
    }

    public void setInventoryStsArray(String inventoryStsArray) {
        this.inventoryStsArray = inventoryStsArray;
    }
}
