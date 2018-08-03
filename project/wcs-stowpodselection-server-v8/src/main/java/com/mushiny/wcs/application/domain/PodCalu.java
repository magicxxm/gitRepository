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
@Table(name = "SYS_STOW_POD_CAL")
public class PodCalu extends BaseEntity {
    @Column(name = "POD_INDEX")
    private Integer podIndex;

    @Column(name = "POD_MIN_WEIGHT")
    private BigDecimal podMiWeight;
    @Column(name = "POD_MIN_VOLUME")
    private BigDecimal podMinVolume;
    @Column(name = "POD_MIN_ITEMS")
    private Integer podMinItems;
    @Column(name = "POD_VOLUME_CONSTANT")
    private BigDecimal podVolumeConstant;
    @Column(name = "POD_WEIGHT_CONSTANT")
    private BigDecimal podWeightConstant;
    @Column(name = "POD_ITEMS_CONSTANT")
    private BigDecimal podItemsConstant;
    @Column(name = "BIN_TYPE")
    private String binType;
    @Column(name = "POD_AVAILABLE_VOLUME")
    private BigDecimal podAvailableVolume;
    @Column(name = "POD_AVAILABLE_WEIGHT")
    private BigDecimal podAvailableWeight;
    @Column(name = "POD_AVAILABLE_ITEMS")
    private Integer podvailableItems;

    public Integer getPodIndex() {
        return podIndex;
    }

    public BigDecimal getPodMiWeight() {
        return podMiWeight;
    }

    public void setPodMiWeight(BigDecimal podMiWeight) {
        this.podMiWeight = podMiWeight;
    }

    public void setPodIndex(Integer podIndex) {
        this.podIndex = podIndex;
    }



    public Integer getPodMinItems() {
        return podMinItems;
    }

    public void setPodMinItems(Integer podMinItems) {
        this.podMinItems = podMinItems;
    }


    public BigDecimal getPodWeightConstant() {
        return podWeightConstant;
    }

    public void setPodWeightConstant(BigDecimal podWeightConstant) {
        this.podWeightConstant = podWeightConstant;
    }

    public BigDecimal getPodItemsConstant() {
        return podItemsConstant;
    }

    public void setPodItemsConstant(BigDecimal podItemsConstant) {
        this.podItemsConstant = podItemsConstant;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public BigDecimal getPodMinVolume() {
        return podMinVolume;
    }

    public void setPodMinVolume(BigDecimal podMinVolume) {
        this.podMinVolume = podMinVolume;
    }

    public BigDecimal getPodVolumeConstant() {
        return podVolumeConstant;
    }

    public void setPodVolumeConstant(BigDecimal podVolumeConstant) {
        this.podVolumeConstant = podVolumeConstant;
    }

    public BigDecimal getPodAvailableVolume() {
        return podAvailableVolume;
    }

    public void setPodAvailableVolume(BigDecimal podAvailableVolume) {
        this.podAvailableVolume = podAvailableVolume;
    }

    public BigDecimal getPodAvailableWeight() {
        return podAvailableWeight;
    }

    public void setPodAvailableWeight(BigDecimal podAvailableWeight) {
        this.podAvailableWeight = podAvailableWeight;
    }

    public Integer getPodvailableItems() {
        return podvailableItems;
    }

    public void setPodvailableItems(Integer podvailableItems) {
        this.podvailableItems = podvailableItems;
    }
}
