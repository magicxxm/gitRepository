package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Pod;

import javax.validation.constraints.NotNull;

public class PodDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String zoneId;

    private ZoneDTO zone;

    private int placeMark;

    private String sellingDegree;

    @NotNull
    private int podIndex = 0;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podTypeId;

    private PodTypeDTO podType;

    private int toWard;

    private String state;

    private int xPosTar;

    private int yPosTar;
    private int xPos;

    private int yPos;

    private int addrCodeIdTar;

    private String sectionId;

    private SectionDTO section;


    public PodDTO() {
    }

    public PodDTO(Pod entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PodTypeDTO getPodType() {
        return podType;
    }

    public void setPodType(PodTypeDTO podType) {
        this.podType = podType;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }

    public int getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(int podIndex) {
        this.podIndex = podIndex;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public int getPlaceMark() {
        return placeMark;
    }

    public void setPlaceMark(int placeMark) {
        this.placeMark = placeMark;
    }

    public String getSellingDegree() {
        return sellingDegree;
    }

    public void setSellingDegree(String sellingDegree) {
        this.sellingDegree = sellingDegree;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    public int getToWard() {
        return toWard;
    }

    public void setToWard(int toWard) {
        this.toWard = toWard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getxPosTar() {
        return xPosTar;
    }

    public void setxPosTar(int xPosTar) {
        this.xPosTar = xPosTar;
    }

    public int getyPosTar() {
        return yPosTar;
    }

    public void setyPosTar(int yPosTar) {
        this.yPosTar = yPosTar;
    }

    public int getAddrCodeIdTar() {
        return addrCodeIdTar;
    }

    public void setAddrCodeIdTar(int addrCodeIdTar) {
        this.addrCodeIdTar = addrCodeIdTar;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public SectionDTO getSection() {
        return section;
    }

    public void setSection(SectionDTO section) {
        this.section = section;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

}
