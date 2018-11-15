package wms.business.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2017/8/29.
 */
public class ItemCheckDTO implements Serializable {

    //"code": "商品编码，string (50)，必填",
    private String code;

    //"warehouseCode": "仓库编码，string (50)，必填",
    private String warehouseCode;

    //"companyCode": "货主编码，string (50)，必填",
      private String companyCode;

      //"name": "商品名称，string (200)，必填",
    private String name;

    public ItemCheckDTO() {
    }

    public ItemCheckDTO(String code, String warehouseCode, String companyCode, String name) {
        this.code = code;
        this.warehouseCode = warehouseCode;
        this.companyCode = companyCode;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
