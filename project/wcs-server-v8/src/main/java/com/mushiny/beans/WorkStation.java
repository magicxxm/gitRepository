package com.mushiny.beans;

import com.mushiny.beans.order.Order;
import com.mushiny.comm.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Tank.li on 2017/6/26.
 */
public class WorkStation {

    public AtomicLong inAddr1 = new AtomicLong();//旋转区入口1计数器
    public AtomicLong inAddr2 = new AtomicLong();//旋转区入口2计数器

    //public Queue<Pod> podQueue = new LinkedList<>();//排队的POD

    /*private Pod onScanPod;

    private Pod onMidPod;

    private Pod onStopPod;*/

    /*public Pod getOnScanPod() {
        return onScanPod;
    }

    public void setOnScanPod(Pod onScanPod) {
        this.onScanPod = onScanPod;
    }*/

    /*public Pod getOnMidPod() {
        return onMidPod;
    }

    public void setOnMidPod(Pod onMidPod) {
        this.onMidPod = onMidPod;
    }*/

    /*public Pod getOnStopPod() {
        return onStopPod;
    }

    public void setOnStopPod(Pod onStopPod) {
        this.onStopPod = onStopPod;
    }*/



    private String workStationId;
    private String wareHouseId;

    private String sectionId;

    private String rotatePoint;

    public String getRotatePoint() {
        return rotatePoint;
    }

    public void setRotatePoint(String rotatePoint) {
        this.rotatePoint = rotatePoint;
    }

    private String scanPoint;
    //STOP点是工作站的计算点
    private String stopPoint;

    private String bufferPoint;

    private String midPoint;//扫描点和停止点中间那个

    public String getMidPoint() {
        return midPoint;
    }

    public void setMidPoint(String midPoint) {
        this.midPoint = midPoint;
    }

    //旋转区外部入口
    public List<String> rotateOutInAddrs = new ArrayList<>();
    //旋转区从工作站方向入口
    private String station2RotateAddr;

    private int face;//工作站朝向

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public String getStation2RotateAddr() {
        return station2RotateAddr;
    }

    public void setStation2RotateAddr(String station2RotateAddr) {
        this.station2RotateAddr = station2RotateAddr;
    }
    //旋转区出口
    private String rotateInOutAddr;

    public String getRotateInOutAddr() {
        return rotateInOutAddr;
    }

    public void setRotateInOutAddr(String rotateInOutAddr) {
        this.rotateInOutAddr = rotateInOutAddr;
    }

    public String getScanPoint() {
        return scanPoint;
    }

    public void setScanPoint(String scanPoint) {
        this.scanPoint = scanPoint;
    }

    public String getStopPoint() {
        return stopPoint;
    }

    public void setStopPoint(String stopPoint) {
        this.stopPoint = stopPoint;
    }

    public String getBufferPoint() {
        return bufferPoint;
    }

    public void setBufferPoint(String bufferPoint) {
        this.bufferPoint = bufferPoint;
    }

    public String getWorkStationId() {
        return workStationId;
    }

    public void setWorkStationId(String workStationId) {
        this.workStationId = workStationId;
    }

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }


    @Override
    public String toString() {
        return "WorkStation{" +
                ", workStationId='" + workStationId + '\'' +
                ", inAddrs='" + JsonUtils.list2Json(rotateOutInAddrs) + '\'' +
                ", rotateInOutAddr='" + rotateInOutAddr + '\'' +
                ", rotatePoint='" + rotatePoint + '\'' +
                ", scanPoint='" + scanPoint + '\'' +
                ", stopPoint='" + stopPoint + '\'' +
                ", bufferPoint='" + bufferPoint + '\'' +
                ", midPoint='" + midPoint + '\'' +
                ", face=" + face +
                '}';
    }
}
