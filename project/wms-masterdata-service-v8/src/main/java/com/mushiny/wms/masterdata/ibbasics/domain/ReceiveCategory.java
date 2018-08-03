package com.mushiny.wms.masterdata.ibbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_RECEIVECATEGORY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class ReceiveCategory extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex = 0;

    @Column(name = "CATEGORY_TYPE", nullable = false)
    private String categoryType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RECEIVEDESTINATION_ID")
    private ReceiveDestination receiveDestination;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "receivingCategory", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReceiveCategoryPosition> positions = new ArrayList<>();

    public void addPosition(ReceiveCategoryPosition position) {
        getPositions().add(position);
        position.setReceivingCategory(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ReceiveCategoryPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiveCategoryPosition> positions) {
        this.positions = positions;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public ReceiveDestination getReceiveDestination() {
        return receiveDestination;
    }

    public void setReceiveDestination(ReceiveDestination receiveDestination) {
        this.receiveDestination = receiveDestination;
    }
}
