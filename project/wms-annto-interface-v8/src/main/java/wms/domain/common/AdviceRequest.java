package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_ADVICEREQUEST")
public class AdviceRequest extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ADVICE_NO", nullable = false, unique = true)
    private String adviceNo;

    @Column(name = "ADVICE_STATE")
    private String adviceState;

    @Column(name = "EXPECTED_DELIVERY")
    private String expectedDelivery;

    @Column(name = "EXPIRE_BATCH")
    private boolean expireBatch = false;

    @Column(name = "EXTERNAL_NO")
    private String externalNo;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @Column(name = "FINISH_DATE")
    private LocalDateTime finishDate;

    @Column(name = "PROCESS_DATE")
    private LocalDateTime processDate;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "adviceRequest", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<AdviceRequestPosition> positions = new ArrayList<>();
    //只是为了汇总显示每个DN 对应的posiition预约数量
    @Column(name = "SIZE")
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addPosition(AdviceRequestPosition position) {
        getPositions().add(position);
        position.setAdviceRequest(this);
    }

    public String getAdviceNo() {
        return adviceNo;
    }

    public void setAdviceNo(String adviceNo) {
        this.adviceNo = adviceNo;
    }

    public String getAdviceState() {
        return adviceState;
    }

    public void setAdviceState(String adviceState) {
        this.adviceState = adviceState;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
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

    public List<AdviceRequestPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<AdviceRequestPosition> positions) {
        this.positions = positions;
    }
}
