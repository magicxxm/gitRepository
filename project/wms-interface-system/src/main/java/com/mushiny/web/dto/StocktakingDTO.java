package com.mushiny.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/2.
 */
public class StocktakingDTO implements Serializable {

    private String stockNo;//盘点单号

//    private String stockType;//盘点类型

    private String warehouseNo;//仓库编号

    private List<StocktakingPositionDTO> positionDTOS = new ArrayList<>();

    public String getStockNo() {
        return stockNo;
    }

    public void setStockNo(String stockNo) {
        this.stockNo = stockNo;
    }

//    public String getStockType() {
//        return stockType;
//    }
//
//    public void setStockType(String stockType) {
//        this.stockType = stockType;
//    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public List<StocktakingPositionDTO> getPositionDTOS() {
        return positionDTOS;
    }

    public void setPositionDTOS(List<StocktakingPositionDTO> positionDTOS) {
        this.positionDTOS = positionDTOS;
    }
}
