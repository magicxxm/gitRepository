package com.mushiny.model;


import com.mushiny.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/8/11.
 */
@Entity
@Table(name = "WD_SECTION")
public class WdSection extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
