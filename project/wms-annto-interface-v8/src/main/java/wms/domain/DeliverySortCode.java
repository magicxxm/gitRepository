package wms.domain;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/6/13.
 */
@Entity
@Table(name = "OB_DELIVERYSORTCODE")
public class DeliverySortCode extends BaseWarehouseAssignedEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    public DeliverySortCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public DeliverySortCode() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
