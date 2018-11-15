package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by PC-4 on 2017/8/4.
 */
@Entity
@Table(name="OB_CUSTOMERORDERPOSITION")
public class CustomerOrderPosition extends BaseClientAssignedEntity {

    @Column(name="AMOUNT",nullable = false)
    private BigDecimal amount;

    @Column(name="ORDER_INDEX",nullable = false)
    private int orderIndex;

    @Column(name="POSITION_NO",nullable = false)
    private int positionNo;

    @Column(name="STATE",nullable = false)
    private int state;

    @ManyToOne
    @JoinColumn(name="ITEMDATA_ID",nullable = false)
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name="ORDER_ID",nullable = false)
    private CustomerOrder customerOrder;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

}
