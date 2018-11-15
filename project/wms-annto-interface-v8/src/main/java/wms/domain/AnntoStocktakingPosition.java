package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_STOCKTAKINGPOSITION")
public class AnntoStocktakingPosition extends BaseEntity{

    @ManyToOne
    @JoinColumn(name="STOCKTAKING_ID",nullable = false)
    private AnntoStocktaking anntoStocktaking;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "INVENTORY_STS")
    private String inventorySts;

    @Column(name = "SYSTEM_QTY")
    private int systemQty;

    public AnntoStocktaking getAnntoStocktaking() {
        return anntoStocktaking;
    }

    public void setAnntoStocktaking(AnntoStocktaking anntoStocktaking) {
        this.anntoStocktaking = anntoStocktaking;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getInventorySts() {
        return inventorySts;
    }

    public void setInventorySts(String inventorySts) {
        this.inventorySts = inventorySts;
    }

    public int getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(int systemQty) {
        this.systemQty = systemQty;
    }
}
