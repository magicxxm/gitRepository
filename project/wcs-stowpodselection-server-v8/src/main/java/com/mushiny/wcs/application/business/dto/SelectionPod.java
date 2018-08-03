package com.mushiny.wcs.application.business.dto;

import com.mushiny.wcs.application.domain.Pod;

import java.io.Serializable;
import java.math.BigDecimal;

public class SelectionPod implements Serializable {
    private static final long serialVersionUID = 1L;

    private Pod pod;

    private BigDecimal podAvailableVolume = BigDecimal.ZERO;

    private int podAvailableItems = 0;

    private BigDecimal calculationResults = BigDecimal.ZERO;

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public BigDecimal getPodAvailableVolume() {
        return podAvailableVolume;
    }

    public void setPodAvailableVolume(BigDecimal podAvailableVolume) {
        this.podAvailableVolume = podAvailableVolume;
    }

    public int getPodAvailableItems() {
        return podAvailableItems;
    }

    public void setPodAvailableItems(int podAvailableItems) {
        this.podAvailableItems = podAvailableItems;
    }

    public BigDecimal getCalculationResults() {
        return calculationResults;
    }

    public void setCalculationResults(BigDecimal calculationResults) {
        this.calculationResults = calculationResults;
    }
}
