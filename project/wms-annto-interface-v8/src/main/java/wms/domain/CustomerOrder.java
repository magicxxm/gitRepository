package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.common.OrderStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Entity
@Table(name="OB_CUSTOMERORDER")
public class CustomerOrder extends BaseClientAssignedEntity {

    @Column(name="CUSTOMER_NAME")
    private String customerName;

    @Column(name="CUSTOMER_NO")
    private String customerNo;

    @Column(name="DELIVERY_DATE")
    private String deliveryDate;

    @Column(name="SORT_CODE")
    private String sortCode;

    @Column(name="ORDER_NO")
    private String orderNo;

    @Column(name="PRIORITY")
    private int priority;

    @Column(name="STATE")
    private int state;

    @ManyToOne
    @JoinColumn(name="STRATEGY_ID")
    private OrderStrategy strategy;


    @OrderBy("positionNo")
    @OneToMany(mappedBy = "customerOrder", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CustomerOrderPosition> positions = new ArrayList<>();
    public void addPosition(CustomerOrderPosition position) {
        getPositions().add(position);
        position.setCustomerOrder(this);
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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public OrderStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(OrderStrategy strategy) {
        this.strategy = strategy;
    }

    public List<CustomerOrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<CustomerOrderPosition> positions) {
        this.positions = positions;
    }
}
