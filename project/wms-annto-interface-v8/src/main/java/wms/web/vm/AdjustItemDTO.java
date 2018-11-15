package wms.web.vm;

import java.io.Serializable;

/**
 * Created by 123 on 2018/1/3.
 */
public class AdjustItemDTO implements Serializable {

    private String pendingAdjustId;

    private String operateTime;

    private String problemType;

    private String companyCode;

    private String itemCode;

    private String itemName;

    private int amount;

    private String reportBy;

    private int state;

    public String getPendingAdjustId() {
        return pendingAdjustId;
    }

    public void setPendingAdjustId(String pendingAdjustId) {
        this.pendingAdjustId = pendingAdjustId;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
