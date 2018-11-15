package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_RECEIPT")
public class AnntoReceipt extends BaseEntity{

    @Column(name = "WAREHOUSE_CODE")
    private String warehouseCode;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "CODE")
    private String code;

    @Column(name = "ANNTO_CODE")
    private String anntoCode;

    @Column(name = "RECEIPT_TYPE")
    private String receiptType;

    @Column(name = "PLN")
    private String pln;

    @Column(name = "RECEIPT_NOTE")
    private String receiptNote;

    @OrderBy("lineNo")
    @OneToMany(mappedBy = "anntoReceipt", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<AnntoReceiptPosition> positions = new ArrayList<>();

    public void addPosition(AnntoReceiptPosition position) {
        getPositions().add(position);
        position.setAnntoReceipt(this);
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnntoCode() {
        return anntoCode;
    }

    public void setAnntoCode(String anntoCode) {
        this.anntoCode = anntoCode;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getPln() {
        return pln;
    }

    public void setPln(String pln) {
        this.pln = pln;
    }

    public String getReceiptNote() {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote) {
        this.receiptNote = receiptNote;
    }

    public List<AnntoReceiptPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<AnntoReceiptPosition> positions) {
        this.positions = positions;
    }
}
