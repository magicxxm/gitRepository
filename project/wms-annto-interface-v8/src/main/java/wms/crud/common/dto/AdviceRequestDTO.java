package wms.crud.common.dto;

import wms.common.crud.dto.BaseClientAssignedDTO;
import wms.domain.common.AdviceRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdviceRequestDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String adviceNo;
    private int size;
    private String adviceState;

    private String expectedDelivery;

    private boolean expireBatch;

    private String externalNo;

    private String externalId;

    private LocalDateTime finishDate;

    private LocalDateTime processDate;

    private List<AdviceRequestPositionDTO> positions = new ArrayList<>();

    public AdviceRequestDTO() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public AdviceRequestDTO(AdviceRequest entity) {
        super(entity);
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

    public List<AdviceRequestPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<AdviceRequestPositionDTO> positions) {
        this.positions = positions;
    }
}
