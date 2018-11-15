package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;
import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/31.
 */
@Entity
@Table(name = "IB_ADVICEREQUEST")
public class ReceiptRequest extends BaseClientAssignedEntity{

    @Column(name = "ADVICE_NO")
    private String receiptNo;

    @Column(name = "ADVICE_STATE")
    private String state;

    @Column(name = "EXPECTED_DELIVERY")
    private LocalDateTime expectedDelivery;

    @Column(name = "EXPIRE_BATCH")
    private boolean expireBatch;//是否是过期收货单

    @Column(name = "EXTERNAL_NO")
    private String externalNo;//外部编码

    @Column(name = "EXTERNAL_ID")
    private String externalId;//外部Id

    @Column(name = "FINISH_DATE")
    private LocalDateTime finishDate;

    @Column(name = "PROCESS_DATE")
    private LocalDateTime processDate;

    @Column(name = "SIZE")
    private int size;

    @OneToMany(mappedBy = "receipt", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReceiptRequestPosition> positions = new ArrayList<>();

    public void addPosition(ReceiptRequestPosition position){
        getPositions().add(position);
        position.setReceipt(this);
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDateTime expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public boolean isExpireBatch() {
        return expireBatch;
    }

    public void setExpireBatch(boolean expireBatch) {
        this.expireBatch = expireBatch;
    }

    public String getExternalNo() {
        return externalNo;
    }

    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public LocalDateTime getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDateTime processDate) {
        this.processDate = processDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ReceiptRequestPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ReceiptRequestPosition> positions) {
        this.positions = positions;
    }
}
