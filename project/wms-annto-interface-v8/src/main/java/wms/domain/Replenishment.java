package wms.domain;

import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/3.
 */
@Entity
@Table(name = "ANNTO_REPLENISHMENT")
public class Replenishment extends BaseWarehouseAssignedEntity{

    private static final long serialVersionUID = 1L;

    @Column(name = "ORDERCODE")
    private String ordercode;

    @Column(name = "REPLENISHMENTMODE")
    private String replenishmentmode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ORDERDATE")
    private String orderdate;

    @Column(name = "FINISHDATE")
    private String finishdate;

    @OrderBy("itemcode")
    @OneToMany(mappedBy = "replenishment", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReplenishmentPosition> positions = new ArrayList<>();

    public void addPosition(ReplenishmentPosition position) {
        getPositions().add(position);
        position.setReplenishment(this);
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getReplenishmentmode() {
        return replenishmentmode;
    }

    public void setReplenishmentmode(String replenishmentmode) {
        this.replenishmentmode = replenishmentmode;
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

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    public List<ReplenishmentPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ReplenishmentPosition> positions) {
        this.positions = positions;
    }
}
