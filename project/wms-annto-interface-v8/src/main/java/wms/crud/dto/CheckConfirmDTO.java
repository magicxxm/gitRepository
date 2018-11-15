package wms.crud.dto;

import wms.business.dto.CheckConfirmItemsDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 盘点单确认
 *
 */
public class CheckConfirmDTO implements Serializable{

    //"originalCountId": "盘点单号，string (50)，必填",
    private String originalCountId;

    //"countType":"盘点方式（静盘：0,动盘 ：1，动碰：2）,int,必填",
    private int countType;

    //"remark": "备注，string (500)",
    private String remark;

    private String warehouseCode;

    private List<CheckConfirmItemsDTO> orderItems = new ArrayList<>();


    public String getOriginalCountId() {
        return originalCountId;
    }

    public void setOriginalCountId(String originalCountId) {
        this.originalCountId = originalCountId;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public List<CheckConfirmItemsDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CheckConfirmItemsDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
