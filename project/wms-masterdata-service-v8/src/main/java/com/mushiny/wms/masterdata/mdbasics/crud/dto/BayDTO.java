package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Bay;

import javax.validation.constraints.NotNull;

public class BayDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private String aisle;

    @NotNull
    private int bayIndex = 0;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podTypeId;

    private PodTypeDTO podType;

    public BayDTO() {
    }

    public BayDTO(Bay entity) {
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

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public int getBayIndex() {
        return bayIndex;
    }

    public void setBayIndex(int bayIndex) {
        this.bayIndex = bayIndex;
    }

    public String getPodTypeId() {
        return podTypeId;
    }

    public void setPodTypeId(String podTypeId) {
        this.podTypeId = podTypeId;
    }

    public PodTypeDTO getPodType() {
        return podType;
    }

    public void setPodType(PodTypeDTO podType) {
        this.podType = podType;
    }
}
