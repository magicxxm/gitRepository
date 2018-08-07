package com.mushiny.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
public class AdviceReceiptDTO implements Serializable {

    private String adviceNo;

    private String containerNo;

    private String type;

    private String warehouseNo;

    private List<AdviceReceiptPositionDTO> positions = new ArrayList<>();

    public String getAdviceNo() {
        return adviceNo;
    }

    public void setAdviceNo(String adviceNo) {
        this.adviceNo = adviceNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public List<AdviceReceiptPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<AdviceReceiptPositionDTO> positions) {
        this.positions = positions;
    }
}
