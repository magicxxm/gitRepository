package wms.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 123 on 2017/8/10.
 */
public class StockUnitDTO implements Serializable {

    //仓库编码  warehouseNo
    private String warehouseCode;

    //商品编码 itemNo
    private String itemCode;

    //商品类型（ZC：正常商品，ZH：组合商品，ZP：赠品，BC：包材，HC：耗材，FL：辅料，CC：残次品，OTHER：其他）
//    private String itemType;

    //库存类型（ZP：良品、正品；CP：不良品、次品）
    private String inventoryType;

    private Date manufactureDate;//生产日期

    private Date expirationDate;//失效日期

    //可用库存
    private BigDecimal quantity;

    public StockUnitDTO() {
    }

    public StockUnitDTO(String warehouseCode, String itemCode,
                        String inventoryType, BigDecimal quantity) {
        this.warehouseCode = warehouseCode;
        this.itemCode = itemCode;
        this.inventoryType = inventoryType;
        this.quantity = quantity;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

}
