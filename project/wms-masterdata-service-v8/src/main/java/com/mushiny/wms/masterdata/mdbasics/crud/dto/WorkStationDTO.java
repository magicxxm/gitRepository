package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class WorkStationDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;
    private int xpos;
    private int ypos;


    @NotNull
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pickPackWallId;

    private boolean fixedScanner;

    private WorkStationTypeDTO workstationType;

    private PickPackWallDTO pickPackWall;

    private int workingFaceOrientation;
    private String pickOrPack;

    private int placeMark;

    private String sectionId;

    private SectionDTO section;

    private int stopPoint;

    private int scanPoint;

    private int bufferPoint;

    private boolean IsCallPod;

    private String useName;
    private String stationName;
    private String labelController;
    private List<WorkStationPositionDTO> positions = new ArrayList<>();

    public WorkStationDTO() {
    }

    public WorkStationDTO(WorkStation entity) {
        super(entity);
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean getFixedScanner() {
        return fixedScanner;
    }

    public void setFixedScanner(boolean fixedScanner) {
        this.fixedScanner = fixedScanner;
    }

    public List<WorkStationPositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(List<WorkStationPositionDTO> positions) {
        this.positions = positions;
    }

    public WorkStationTypeDTO getWorkstationType() {
        return workstationType;
    }

    public void setWorkstationType(WorkStationTypeDTO workstationType) {
        this.workstationType = workstationType;
    }

    public String getPickPackWallId() {
        return pickPackWallId;
    }

    public void setPickPackWallId(String pickPackWallId) {
        this.pickPackWallId = pickPackWallId;
    }

    public PickPackWallDTO getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(PickPackWallDTO pickPackWall) {
        this.pickPackWall = pickPackWall;
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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
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

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getLabelController() {
        return labelController;
    }

    public void setLabelController(String labelController) {
        this.labelController = labelController;
    }

    public boolean isCallPod() {
        return IsCallPod;
    }

    public void setCallPod(boolean callPod) {
        IsCallPod = callPod;
    }

    public String getPickOrPack() {
        return pickOrPack;
    }

    public void setPickOrPack(String pickOrPack) {
        this.pickOrPack = pickOrPack;
    }
}
