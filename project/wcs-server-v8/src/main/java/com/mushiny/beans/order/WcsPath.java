package com.mushiny.beans.order;

import com.mushiny.comm.JsonUtils;

import java.util.List;

/**
 * Created by Tank.li on 2017/9/5.
 */
public class WcsPath implements java.io.Serializable {
    private Long robotID;
    private Long time;
    private Long sectionID;
    private Integer rotateTheta = 0;
    private List<Long> seriesPath;
    private Long podUpAddress = 0L;
    private Long podDownAddress = 0L;
    private Boolean isRotatePod = Boolean.FALSE;

    private Long srcAddr;
    private Long endAddr;

    public Long getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(Long srcAddr) {
        this.srcAddr = srcAddr;
    }

    public Long getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(Long endAddr) {
        this.endAddr = endAddr;
    }

    public Long getRobotID() {
        return robotID;
    }

    public void setRobotID(Long robotID) {
        this.robotID = robotID;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public Integer getRotateTheta() {
        return rotateTheta;
    }

    public void setRotateTheta(Integer rotateTheta) {
        this.rotateTheta = rotateTheta;
    }

    public List<Long> getSeriesPath() {
        return seriesPath;
    }

    public void setSeriesPath(List<Long> seriesPath) {
        this.seriesPath = seriesPath;
    }

    public Long getPodUpAddress() {
        return podUpAddress;
    }

    public void setPodUpAddress(Long podUpAddress) {
        this.podUpAddress = podUpAddress;
    }

    public Long getPodDownAddress() {
        return podDownAddress;
    }

    public void setPodDownAddress(Long podDownAddress) {
        this.podDownAddress = podDownAddress;
    }

    public Boolean getRotatePod() {
        return isRotatePod;
    }

    public void setRotatePod(Boolean rotatePod) {
        isRotatePod = rotatePod;
    }

    @Override
    public String toString() {
        return "WcsPath{" +
                "robotID=" + robotID +
                ", podUpAddress=" + podUpAddress +
                ", podDownAddress=" + podDownAddress +
                ", endAddr=" + endAddr +
                ", srcAddr=" + srcAddr +
                ", seriesPath=" + JsonUtils.list2Json(seriesPath) +
                '}';
    }

    public static void main(String[] args) {
        WcsPath path = new WcsPath();
        path.setPodDownAddress(0L);
        path.setPodUpAddress(0L);
        System.out.println(path);
    }
}
