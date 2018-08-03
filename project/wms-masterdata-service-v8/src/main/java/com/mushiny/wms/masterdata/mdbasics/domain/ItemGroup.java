package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "MD_ITEMGROUP")
public class ItemGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex = 0;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private ItemGroup parentItemGroup;

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

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public ItemGroup getParentItemGroup() {
        return parentItemGroup;
    }

    public void setParentItemGroup(ItemGroup parentItemGroup) {
        this.parentItemGroup = parentItemGroup;
    }
}
