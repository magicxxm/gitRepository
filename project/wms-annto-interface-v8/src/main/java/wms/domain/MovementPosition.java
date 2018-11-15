package wms.domain;

import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by PC-4 on 2017/8/3.
 */
@Entity
@Table(name = "ANNTO_MOVEMENTPOSITION")
public class MovementPosition extends BaseWarehouseAssignedEntity{

    private static final long serialVersionUID = 1L;

    @Column(name="COMPANY_CODE")
    private String companyCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "MOVEMENT_ID")
    private Movement movement;

    @Column(name = "ITEMCODE")
    private String itemcode;

    @Column(name = "ITEMNAME")
    private String itemname;

    @Column(name = "ALLOCATEDQTY")
    private int allocatedqty;

    @Column(name = "INVENTORYSTS")
    private int inventorysts;

    @Column(name = "BATCH")
    private String batch;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "MANUFACTUREDATE")
    private Date manufacturedate;

    @Column(name = "EXPIRATIONDATE")
    private Date expirationdate;

    @Column(name = "FROMLOC")
    private String fromloc;

    @Column(name = "TOLOC")
    private String toloc;

    @Column(name = "FROMZONE")
    private String fromzone;

    @Column(name = "TOZONE")
    private String tozone;

    @Column(name = "FROMLPN")
    private String fromlpn;

    @Column(name = "TOLPN")
    private String tolpn;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getAllocatedqty() {
        return allocatedqty;
    }

    public void setAllocatedqty(int allocatedqty) {
        this.allocatedqty = allocatedqty;
    }

    public int getInventorysts() {
        return inventorysts;
    }

    public void setInventorysts(int inventorysts) {
        this.inventorysts = inventorysts;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Date getManufacturedate() {
        return manufacturedate;
    }

    public void setManufacturedate(Date manufacturedate) {
        this.manufacturedate = manufacturedate;
    }

    public Date getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(Date expirationdate) {
        this.expirationdate = expirationdate;
    }

    public String getFromloc() {
        return fromloc;
    }

    public void setFromloc(String fromloc) {
        this.fromloc = fromloc;
    }

    public String getToloc() {
        return toloc;
    }

    public void setToloc(String toloc) {
        this.toloc = toloc;
    }

    public String getFromzone() {
        return fromzone;
    }

    public void setFromzone(String fromzone) {
        this.fromzone = fromzone;
    }

    public String getTozone() {
        return tozone;
    }

    public void setTozone(String tozone) {
        this.tozone = tozone;
    }

    public String getFromlpn() {
        return fromlpn;
    }

    public void setFromlpn(String fromlpn) {
        this.fromlpn = fromlpn;
    }

    public String getTolpn() {
        return tolpn;
    }

    public void setTolpn(String tolpn) {
        this.tolpn = tolpn;
    }
}
