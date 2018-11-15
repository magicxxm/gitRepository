package wms.crud.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 盘点单同步
 *
 */
public class CheckUpdateDTO {

    //"originalCountId": "盘点单号，string (50)，必填",
    private String originalCountId;

    //"countType":"盘点方式（静盘：0,动盘 ：1，动碰：2）,int,必填",
    private int countType;

    //"remark": "备注，string (500)",
    private String remark;

    private String warehouseCode;

    private List<CheckPositionDTO> orderItems = new ArrayList<>();

    private ExtendFieldsDTO extendFields;


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

    public List<CheckPositionDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CheckPositionDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
