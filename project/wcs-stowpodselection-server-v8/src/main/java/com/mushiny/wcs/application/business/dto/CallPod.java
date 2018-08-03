package com.mushiny.wcs.application.business.dto;

import com.mushiny.wcs.application.domain.Pod;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
public class CallPod {
    private Pod pod;
    private List<String> podFaces=new ArrayList<>(6);
    private BigDecimal score;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public List<String> getPodFaces() {
        return podFaces;
    }

    public void setPodFaces(List<String> podFaces) {
        this.podFaces = podFaces;
    }
}
