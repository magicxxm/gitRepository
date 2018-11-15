package wms.crud.common.dto;

import wms.common.crud.dto.BaseWarehouseAssignedDTO;
import wms.domain.Movement;

import java.util.ArrayList;
import java.util.List;

public class MovementDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String orderCode;

    private String status;

    private String remark;

    private String orderDate;

    private List<MovementPositionDTO> orderItems = new ArrayList<>();

    public MovementDTO() {
    }

    public MovementDTO(Movement entity) {
        super(entity);
    }

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<MovementPositionDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<MovementPositionDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
