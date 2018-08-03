package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/6/4.
 */
@Entity
@Table(name = "SYS_STOW_POD_FACE_CAL")
public class PodFaceCalu extends BaseEntity {
    @Column(name = "POD_INDEX")
    private Integer podIndex;

    @Column(name = "POD_FACE_MIN_WEIGHT")
    private BigDecimal podFaceMiWeight;
    @Column(name = "POD_FACE_MIN_VOLUME")
    private BigDecimal podFaceMinVolume;
    @Column(name = "POD_FACE_MIN_ITEMS")
    private Integer podFaceMinItems;

    @Column(name = "BIN_TYPE")
    private String binType;
    @Column(name = "POD_FACE")
    private String podFace;
    @Column(name = "POD_FACE_AVAILABLE_VOLUME")
    private BigDecimal podFaceAvailableVolume;
    @Column(name = "POD_FACE_AVAILABLE_WEIGHT")
    private BigDecimal podFaceAvailableWeight;
    @Column(name = "POD_FACE_AVAILABLE_ITEMS")
    private Integer podFaceAvailableItems;

    public String getPodFace() {
        return podFace;
    }

    public void setPodFace(String podFace) {
        this.podFace = podFace;
    }

    public Integer getPodIndex() {
        return podIndex;
    }

    public void setPodIndex(Integer podIndex) {
        this.podIndex = podIndex;
    }

    public BigDecimal getPodFaceMiWeight() {
        return podFaceMiWeight;
    }

    public void setPodFaceMiWeight(BigDecimal podFaceMiWeight) {
        this.podFaceMiWeight = podFaceMiWeight;
    }

    public BigDecimal getPodFaceMinVolume() {
        return podFaceMinVolume;
    }

    public void setPodFaceMinVolume(BigDecimal podFaceMinVolume) {
        this.podFaceMinVolume = podFaceMinVolume;
    }

    public Integer getPodFaceMinItems() {
        return podFaceMinItems;
    }

    public void setPodFaceMinItems(Integer podFaceMinItems) {
        this.podFaceMinItems = podFaceMinItems;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public BigDecimal getPodFaceAvailableVolume() {
        return podFaceAvailableVolume;
    }

    public void setPodFaceAvailableVolume(BigDecimal podFaceAvailableVolume) {
        this.podFaceAvailableVolume = podFaceAvailableVolume;
    }

    public BigDecimal getPodFaceAvailableWeight() {
        return podFaceAvailableWeight;
    }

    public void setPodFaceAvailableWeight(BigDecimal podFaceAvailableWeight) {
        this.podFaceAvailableWeight = podFaceAvailableWeight;
    }

    public Integer getPodFaceAvailableItems() {
        return podFaceAvailableItems;
    }

    public void setPodFaceAvailableItems(Integer podFaceAvailableItems) {
        this.podFaceAvailableItems = podFaceAvailableItems;
    }
}
