package wms.crud.common.dto;

import wms.common.crud.dto.BaseWarehouseAssignedDTO;
import wms.domain.Replenishment;

import java.util.ArrayList;
import java.util.List;

public class ReplenishmentDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String orderCode;

    private String replenishmentMode;

    private String status;

    private String remark;

    private String orderDate;

    private String finishDate;

    private List<ReplenishmentPositionDTO> orderItems = new ArrayList<>();

    public ReplenishmentDTO() {
    }

    public ReplenishmentDTO(Replenishment entity) {
        super(entity);
    }

    public List<ReplenishmentPositionDTO> getPositions() {
        return orderItems;
    }

    public void setPositions(List<ReplenishmentPositionDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReplenishmentMode() {
        return replenishmentMode;
    }

    public void setReplenishmentMode(String replenishmentMode) {
        this.replenishmentMode = replenishmentMode;
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

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public List<ReplenishmentPositionDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<ReplenishmentPositionDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
