package wms.crud.dto;

import wms.business.dto.TransferOrderItemsDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 *
 * 移库单确认
 *
 */
public class TransferConfirmDTO implements Serializable {

    //  "Code": "移库单号，string (50)，必填",
    @NotNull
    private String code;

    //"status": "状态(新建 new , 关闭 closed)，string (50)，必填",
    @NotNull
    private String status;

    //"remark": "备注，string (500)",
    private String remark;

    //"finishDate": "wanc时间, yyyy-MM-dd HH:mm:ss，string (20)，必填",
    private String finishDate;

    private List<TransferOrderItemsDTO> orderItems = new ArrayList<>();

    public TransferConfirmDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
