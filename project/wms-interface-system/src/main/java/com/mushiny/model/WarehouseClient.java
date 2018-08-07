package com.mushiny.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by 123 on 2018/3/31.
 */
@Entity
@Table(name = "SYS_WAREHOUSE_CLIENT")
public class WarehouseClient implements Serializable {

    @Id
    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @Id
    @Column(name = "CLIENT_ID")
    private String clientId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
