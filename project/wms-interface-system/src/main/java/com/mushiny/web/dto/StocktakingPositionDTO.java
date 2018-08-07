package com.mushiny.web.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2018/2/2.
 */
public class StocktakingPositionDTO implements Serializable {

    private String itemNo;//商品唯一编码

    private String clientNo;//客户编号

    private String warehouseNo;//仓库编号

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }
}
