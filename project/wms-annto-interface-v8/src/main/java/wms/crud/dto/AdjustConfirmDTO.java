package wms.crud.dto;

public class AdjustConfirmDTO {

    //"pendingAdjustId": "调整行号，string (20)
    private String pendingAdjustId;

    //"state": "问题状态 ，int ，(0-待调整 1-已调整 2-未批准 3-已批准)
    private int state;

    //"agreeTime": "批准时间，datetime
    private String agreeTime;

    public String getPendingAdjustId() {
        return pendingAdjustId;
    }

    public void setPendingAdjustId(String pendingAdjustId) {
        this.pendingAdjustId = pendingAdjustId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAgreeTime() {
        return agreeTime;
    }

    public void setAgreeTime(String agreeTime) {
        this.agreeTime = agreeTime;
    }
}
