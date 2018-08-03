package com.mushiny.wcs.application.business.dto;

import com.mushiny.wcs.application.business.enums.StationType;
import com.mushiny.wcs.application.domain.WorkStation;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/9/25.
 */
public class CallStation {
    private String logicalStation;

    private WorkStation workStation;

    private StationType stationType;
    //工作站最大工作量
    private int workMaxWorkLoad;
    private int workStationWorkLoad;

    public int getWorkStationWorkLoad() {
        return workStationWorkLoad;
    }

    public void setWorkStationWorkLoad(int workStationWorkLoad) {
        this.workStationWorkLoad = workStationWorkLoad;
    }

    //工作站允许最大的pod
    private int workStationMaxPod;
    //处理pod的时间
    private int podCycleTime;
    public int getWorkStaionMaxPod() {
        return workStationMaxPod;
    }

    public void setWorkStationMaxPod(int workStationMaxPod) {
        this.workStationMaxPod = workStationMaxPod;
    }


    public int getWorkMaxWorkLoad() {
        return workMaxWorkLoad;
    }

    public void setWorkMaxWorkLoad(int workMaxWorkLoad) {
        this.workMaxWorkLoad = workMaxWorkLoad;
    }

    public int getWorkStationMaxPod() {
        return workStationMaxPod;
    }

    public int getPodCycleTime() {
        return podCycleTime;
    }

    public void setPodCycleTime(int podCycleTime) {
        this.podCycleTime = podCycleTime;
    }

    public String getLogicalStation() {
        return logicalStation;
    }

    public void setLogicalStation(String logicalStation) {
        this.logicalStation = logicalStation;
    }
    public StationType getStationType() {
        return stationType;
    }

    public void setStationType(StationType stationType) {
        this.stationType = stationType;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }

}
