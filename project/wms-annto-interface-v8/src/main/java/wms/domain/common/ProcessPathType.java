package wms.domain.common;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/4/25.
 */
@Entity
@Table(name="OB_PROCESSPATHTYPE")
public class ProcessPathType extends BaseWarehouseAssignedEntity {

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

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
}
