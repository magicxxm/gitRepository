package wms.crud.dto;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 库存查询
 *
 */
public class InventoryGetDTO {

    //"warehouseCode": "仓库编码，string (50)，必填",
    private String warehouseCode;

    //"customerCode": "货主编码，string (50)，必填",
    private String customerCode;

    //"itemCode": "itemCode，商品编码",
    private String itemCode;

    //"itemType": "商品类型（ZC：正常商品、ZH：组合商品、ZP：赠品、BC：包材、HC：耗材、FL：辅料、CC：残次品、OTHER：其它），string (10)",
    private String itemType;

    //"inventoryType": "库存类型（ZP：良品、正品、CP：不良品、次品），string (2)",
    private String inventoryType;

    //"page": "当前页数（从1开始），int",
    private int page;

    //"pageNo": "每页条数，int"
    private int pageNo;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
