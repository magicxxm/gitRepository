package com.mushiny.wms.masterdata.mdbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveThreshold;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackWall;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MD_WORKSTATION")
public class WorkStation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private WorkStationType type;

    @Column(name = "XPOS")
    private int xpos;

    @Column(name = "YPOS")
    private int ypos;

    @ManyToOne
    @JoinColumn(name = "PICKPACKWALL_ID")
    private PickPackWall pickPackWall;

    @Column(name = "FIXED_SCANNER")
    private boolean fixedScanner;

    @Column(name = "WORKING_FACE_ORIENTATION")
    private int workingFaceOrientation;

    @Column(name = "PLACEMARK")
    private int placeMark;
    @Column(name = "PICK_OR_PACK")
    private String pickOrPack;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @Column(name = "STOPPOINT")
    private int stopPoint;

    @Column(name = "SCANPOINT")
    private int scanPoint;

    @Column(name = "BUFFERPOINT")
    private int bufferPoint;

    @Column(name = "ISCALLPOD")
    private boolean isCallPod;

    @Column(name = "OPERATOR_ID")
    private String operator;

    @Column(name = "STATION_NAME")
    private String stationName;
    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "MD_HARDWARE_WORKSTATION",
            joinColumns = @JoinColumn(name = "WORKSTATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "HARDWARE_ID"))
    private Set<HardWare> hardWares = new HashSet<>();

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "workStation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<WorkStationPosition> positions = new ArrayList<>();

    public void addPosition(WorkStationPosition position) {
        getPositions().add(position);
        position.setWorkStation(this);
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkStationType getType() {
        return type;
    }

    public void setType(WorkStationType type) {
        this.type = type;
    }

    public boolean getFixedScanner() {
        return fixedScanner;
    }

    public void setFixedScanner(boolean fixedScanner) {
        this.fixedScanner = fixedScanner;
    }

    public List<WorkStationPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<WorkStationPosition> positions) {
        this.positions = positions;
    }

    public PickPackWall getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(PickPackWall pickPackWall) {
        this.pickPackWall = pickPackWall;
    }

    public Set<HardWare> getHardWares() {
        return hardWares;
    }

    public void setHardWares(Set<HardWare> hardWares) {
        this.hardWares = hardWares;
    }

    public boolean isFixedScanner() {
        return fixedScanner;
    }

    public int getWorkingFaceOrientation() {
        return workingFaceOrientation;
    }

    public void setWorkingFaceOrientation(int workingFaceOrientation) {
        this.workingFaceOrientation = workingFaceOrientation;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }

    public int getStopPoint() {
        return stopPoint;
    }

    public void setStopPoint(int stopPoint) {
        this.stopPoint = stopPoint;
    }

    public int getScanPoint() {
        return scanPoint;
    }

    public void setScanPoint(int scanPoint) {
        this.scanPoint = scanPoint;
    }

    public int getBufferPoint() {
        return bufferPoint;
    }

    public void setBufferPoint(int bufferPoint) {
        this.bufferPoint = bufferPoint;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setisCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPickOrPack() {
        return pickOrPack;
    }

    public void setPickOrPack(String pickOrPack) {
        this.pickOrPack = pickOrPack;
    }
}
