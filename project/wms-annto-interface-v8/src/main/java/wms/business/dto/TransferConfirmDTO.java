package wms.business.dto;

import wms.crud.common.dto.MovementPositionDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/18.
 */
public class TransferConfirmDTO implements Serializable {

    private String orderCode;

    private String status;

    private String remark;

    private String finishDate;

    private List<TransferOrderItemsDTO> orderItems = new ArrayList<>();

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public List<TransferOrderItemsDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TransferOrderItemsDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
