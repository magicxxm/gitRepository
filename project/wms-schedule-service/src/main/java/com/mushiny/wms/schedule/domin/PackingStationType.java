package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_PACKINGSTATIONTYPE")
public class PackingStationType extends BaseWarehouseAssignedEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IF_SCAN")
    private boolean ifScan;

    @Column(name = "IF_PRINT")
    private boolean ifPrint;

    @Column(name = "IF_SCAN_INVOICE")
    private boolean ifScanInvoice;

    @Column(name = "IF_WEIGHT")
    private boolean ifWeight;

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

    public boolean isIfScan() {
        return ifScan;
    }

    public void setIfScan(boolean ifScan) {
        this.ifScan = ifScan;
    }

    public boolean isIfPrint() {
        return ifPrint;
    }

    public void setIfPrint(boolean ifPrint) {
        this.ifPrint = ifPrint;
    }

    public boolean isIfScanInvoice() {
        return ifScanInvoice;
    }

    public void setIfScanInvoice(boolean ifScanInvoice) {
        this.ifScanInvoice = ifScanInvoice;
    }

    public boolean isIfWeight() {
        return ifWeight;
    }

    public void setIfWeight(boolean ifWeight) {
        this.ifWeight = ifWeight;
    }
}
