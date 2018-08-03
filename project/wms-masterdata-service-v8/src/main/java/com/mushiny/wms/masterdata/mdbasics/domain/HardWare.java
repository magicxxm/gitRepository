package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import javax.persistence.*;

@Entity
@Table(name = "MD_HARDWARE")
public class HardWare extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IP_ADDRESS", nullable = false)
    private String ipAddress;

    @Column(name = "PORT_NUMBER")
    private int portNumber = 0;

    @Column(name = "HARDWARE_TYPE", nullable = false)
    private String hardWareType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getHardWareType() {
        return hardWareType;
    }

    public void setHardWareType(String hardWareType) {
        this.hardWareType = hardWareType;
    }
}
