package wms.crud.dto;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class PackageItemsDTO {

    //"itemCode": "商品编码，string (20)，必填",
    private String itemCode;

    //"quantity": "包裹内该商品的数量，int，必填"
    private int quantity;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
