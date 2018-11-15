package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/22.
 */
@Entity
@Table(name = "OB_TRANSFERSHIPMENT")
public class TransferShipment extends BaseClientAssignedEntity {

    @Column(name="TRANSFER_NAME")
    private String customerName;

    @Column(name="TRANSFER_NO")
    private String customerNo;

    @Column(name="DELIVERY_DATE")
    private LocalDateTime deliveryDate;//发货点

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name="TRANSFERSHIPMENT_NO",nullable = false,unique = true)
    private String shipmentNo;

    @Column(name="PRIORITY")
    private int priority;

    @Column(name="STATE",nullable = false)
    private int state = 0;

    @ManyToOne
    @JoinColumn(name="STRATEGY_ID")
    private OrderStrategy orderStrategy;

    @Column(name = "PICK_MODE")
    private String pickMode;

    @ManyToOne
    @JoinColumn(name = "PICKINGCATEGORY_ID")
    private PickingCategory pickingCategory;

    @Column(name = "PASSED_OVER_COUNT")
    private int passedOverCount = 0;//未选中的次数  not null

    @Column(name = "ACTIVATED")
    private boolean activated = false; //是否激活  not null

    @Column(name = "ACTIVATION_DATE")
    private LocalDateTime activationDate = LocalDateTime.now();//激活时间 not null

    @Column(name = "SELECTED")
    private boolean selected = false;//是否进入selected not null

    @Column(name = "COMPLETED")
    private boolean completed = false;//是否已全部分配拣货任务 not null

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "transferShipment", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<TransferShipmentPosition> positions = new ArrayList<>();

    public void addPosition(TransferShipmentPosition position) {
        getPositions().add(position);
        position.setTransferShipment(this);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public OrderStrategy getOrderStrategy() {
        return orderStrategy;
    }

    public void setOrderStrategy(OrderStrategy orderStrategy) {
        this.orderStrategy = orderStrategy;
    }

    public String getPickMode() {
        return pickMode;
    }

    public void setPickMode(String pickMode) {
        this.pickMode = pickMode;
    }

    public PickingCategory getPickingCategory() {
        return pickingCategory;
    }

    public void setPickingCategory(PickingCategory pickingCategory) {
        this.pickingCategory = pickingCategory;
    }

    public int getPassedOverCount() {
        return passedOverCount;
    }

    public void setPassedOverCount(int passedOverCount) {
        this.passedOverCount = passedOverCount;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public LocalDateTime getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDateTime activationDate) {
        this.activationDate = activationDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<TransferShipmentPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TransferShipmentPosition> positions) {
        this.positions = positions;
    }
}
