package com.mushiny.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/7.
 */
public class StockChangeInfo implements Serializable {

    private String system;

    private String warehouseNo;

    private String changeNo;

    private String changeType;

    private String fsTime;

    private List<StockChangePosition> positions= new ArrayList<>();

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getChangeNo() {
        return changeNo;
    }

    public void setChangeNo(String changeNo) {
        this.changeNo = changeNo;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getFsTime() {
        return fsTime;
    }

    public void setFsTime(String fsTime) {
        this.fsTime = fsTime;
    }

    public List<StockChangePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<StockChangePosition> positions) {
        this.positions = positions;
    }
}
