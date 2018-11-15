package wms.business.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2018/1/3.
 */
public class AdjustConfirmDTO implements Serializable {

    private String pendingAdjustId;

    private int state;

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
