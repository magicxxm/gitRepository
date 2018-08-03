package com.mushiny.wms.masterdata.obbasics.common.report;

import java.io.Serializable;

public class BarcodeLabelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;

    public BarcodeLabelDTO(String label) {
        this.label = label;
    }

    public BarcodeLabelDTO() {
        this.label = null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
